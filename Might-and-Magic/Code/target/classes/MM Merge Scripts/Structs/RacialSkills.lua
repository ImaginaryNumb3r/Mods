
local OldGame = structs.f.GameStructure
function structs.f.GameStructure(define)

	OldGame(define)
	define
	[0].array(6).array(39).i1  'RaceSkills'

end

local function ProcessTxt()

	if not const.Race then
		return false
	end

	local TxtTable = io.open("Data/Tables/Race Skills.txt", "r")
	if not TxtTable then
		TxtTable = io.open("Data/Tables/Race Skills.txt", "w")
		local line = ""
		local RaceCount = 0
		for k,v in pairs(const.Race) do
			line = line .. "\9" .. k .. "(" .. RaceCount .. ")"
			RaceCount = RaceCount + 1
		end
		TxtTable:write(line .. "\n")

		for k,v in pairs(const.Skills) do
			line = k
			for i = 0, RaceCount do
				line = line .. "\9-"
			end
			TxtTable:write(line .. "\n")
		end

		io.close(TxtTable)
		TxtTable = io.open("Data/Tables/Race Skills.txt", "r")
	end

	local LineIt = TxtTable:lines()
	LineIt() -- skip header

	local SkillConns = {B = 1, E = 2, M = 3, G = 4}
	local T = {}

	for line in LineIt do
		local Words = string.split(line, "\9")
		if string.len(Words[1]) > 0 then
			local t = {}
			for i = 2, #Words do
				table.insert(t, tonumber(Words[i]) or SkillConns[Words[i]] or 0)
			end
			table.insert(T, t)
		end
	end

	local SkillsCount = #T
	local RaceCount = #T[SkillsCount]
	local NewSpace = mem.StaticAlloc(SkillsCount * RaceCount)

	for S,L in ipairs(T) do
		for R,v in ipairs(L) do
			mem.i1[NewSpace + (S-1) + (R-1)*SkillsCount] = v
		end
	end

	io.close(TxtTable)

	structs.o.GameStructure.RaceSkills = NewSpace
	internal.SetArrayUpval(Game.RaceSkills, "o", NewSpace)
	internal.SetArrayUpval(Game.RaceSkills, "count", RaceCount)
	internal.SetArrayUpval(Game.RaceSkills, "size", SkillsCount)
	for i,v in Game.RaceSkills do
		internal.SetArrayUpval(Game.RaceSkills[i], "count", SkillsCount)
	end

	return true

end

local function SetHooks()

	local min, max = math.min, math.max
	local ClassSkillsPtr = Game.Classes.Skills["?ptr"]
	local RaceSkllsPtr = Game.RaceSkills["?ptr"]

	-- Base functions

	local GetRaceSkill = mem.asmproc([[
	; start:
	; eax - player ptr
	; ecx - skill

	; Get face
	movzx eax, byte [ds:eax+0x353]

	; Get race
	imul eax, eax, ]] .. Game.CharacterPortraits[0]["?size"] ..[[;
	add eax, ]] .. Game.CharacterPortraits["?ptr"] .. [[;
	add eax, 0x3f; race value offset
	movzx eax, byte [ds:eax]

	; Get skill
	imul eax, eax, 0x27; ]] .. Game.RaceSkills[0]["?size"] .. [[;
	add eax, ecx
	movsx eax, byte [ds:]] .. Game.RaceSkills["?ptr"] .. [[ + eax]
	retn]])

	-- Changed mechanic:
	-- Instead of choosing maximum out of two, function will choose maximum out of sum of two and 4, -
	-- Racial skills will be addition to base.
	local ChooseHighest = mem.asmproc([[
	; start:
	; eax - v1
	; edx - v2

	push ecx
	lea ecx, dword[ds:eax+edx]
	cmp ecx, 0
	jl @neg

	cmp ecx, 0x4
	jle @end

	mov ecx, 0x4

	@end:
	mov eax, ecx

	pop ecx
	retn

	@neg:
	mov eax, 0

	pop ecx
	retn
	]])

	-- 0x4171a0, 0x4171ba, 0x4171c6
	function events.ShowSkillDescr(t)
		local SkillLevel = Game.RaceSkills[Game.CharacterPortraits[t.Player.Face].Race][t.Skill] + t.MaxLevel

		if SkillLevel > 4 then
			SkillLevel = 4
		elseif SkillLevel < 0 then
			SkillLevel = 0
		end
		t.MaxLevel = SkillLevel
	end

	-- Can get new tier

	-- 0x4b0e6b
	mem.asmpatch(0x4b0e6b, [[
	movzx eax, byte [ds:ecx+eax+]] .. ClassSkillsPtr .. [[]
	mov edx, eax
	mov eax, ebx
	call absolute ]] .. GetRaceSkill .. [[;
	call absolute ]] .. ChooseHighest .. [[;]])

	-- 0x4b0e99
--~ 	mem.asmpatch(0x4b0e99, [[
--~ 	lea eax, dword [ds:ecx+edx+]] .. ClassSkillsPtr .. [[]
--~ 	mov edx, eax
--~ 	mov eax, ebx
--~ 	call absolute ]] .. GetRaceSkill .. [[;
--~ 	call absolute ]] .. ChooseHighest .. [[;
--~ 	mov ecx, eax
--~ 	mov eax, edi]])

	-- Can learn in shop

	-- 0x4b32ee - can not trigger yet.
	mem.asmpatch(0x4b32ee, [[
	movzx eax, byte[ds:edi+eax+]] .. ClassSkillsPtr .. [[]
	mov edx, eax
	mov eax, ebx
	mov ecx, edi
	call absolute ]] .. GetRaceSkill .. [[;
	call absolute ]] .. ChooseHighest .. [[;
	mov ecx, esi
	test eax, eax]])

	-- 0x4b33ea
	mem.asmpatch(0x4b33ea, [[
	movzx eax, byte[ds:ebx+eax+]] .. ClassSkillsPtr .. [[]
	mov edx, eax
	mov eax, esi
	mov ecx, ebx
	call absolute ]] .. GetRaceSkill .. [[;
	call absolute ]] .. ChooseHighest .. [[;
	mov ecx, esi
	test eax, eax]])

	-- 0x4b4bb0
	mem.asmpatch(0x4b4bb0, [[
	movzx eax, byte [ds:esi+eax+]] .. ClassSkillsPtr .. [[]
	mov edx, eax
	mov eax, ecx
	mov ecx, esi
	call absolute ]] .. GetRaceSkill .. [[;
	call absolute ]] .. ChooseHighest .. [[;
	mov ecx, dword [ss:ebp-0x14]
	test eax, eax]])

	-- 0x4b4ca7
	mem.asmpatch(0x4b4ca7, [[
	movzx eax, byte [ds:edi+eax+]] .. ClassSkillsPtr .. [[]
	mov edx, eax
	mov eax, ecx
	mov ecx, edi
	call absolute ]] .. GetRaceSkill .. [[;
	call absolute ]] .. ChooseHighest .. [[;
	mov ecx, dword [ss:ebp-0x14]
	test eax, eax]])

	-- 0x4b9382
	mem.asmpatch(0x4b9382, [[
	movzx eax, byte [ds:esi+eax+]] .. ClassSkillsPtr .. [[]
	mov edx, eax
	mov eax, ecx
	mov ecx, esi
	call absolute ]] .. GetRaceSkill .. [[;
	call absolute ]] .. ChooseHighest .. [[;
	mov ecx, dword [ss:ebp-0x10]
	test eax, eax]])

	-- 0x4b9477
	mem.asmpatch(0x4b9477, [[
	movzx eax, byte [ds:edi+eax+]] .. ClassSkillsPtr .. [[]
	mov edx, eax
	mov eax, ecx
	mov ecx, edi
	call absolute ]] .. GetRaceSkill .. [[;
	call absolute ]] .. ChooseHighest .. [[;
	mov ecx, dword [ss:ebp-0x10]
	test eax, eax]])

	-- 0x4b7bee
	mem.asmpatch(0x4b7bee, [[
	movzx eax, byte [ds:esi+eax+]] .. ClassSkillsPtr .. [[]
	mov edx, eax
	mov eax, ecx
	mov ecx, esi
	call absolute ]] .. GetRaceSkill .. [[;
	call absolute ]] .. ChooseHighest .. [[;
	mov ecx, dword [ss:ebp-0x14]
	test eax, eax]])

	-- 0x4b7ce3
	mem.asmpatch(0x4b7ce3, [[
	movzx eax, byte [ds:edi+eax+]] .. ClassSkillsPtr .. [[]
	mov edx, eax
	mov eax, ecx
	mov ecx, edi
	call absolute ]] .. GetRaceSkill .. [[;
	call absolute ]] .. ChooseHighest .. [[;
	mov ecx, dword [ss:ebp-0x14]
	test eax, eax]])

	-- 0x4b590d -- temple -- mistake
	mem.asmpatch(0x4b590d, [[
	movzx eax, byte [ds:esi+eax+]] .. ClassSkillsPtr .. [[]
	mov edx, eax
	mov eax, ecx
	mov ecx, esi
	call absolute ]] .. GetRaceSkill .. [[;
	call absolute ]] .. ChooseHighest .. [[;
	mov ecx, dword [ss:ebp-0xc]
	test eax, eax]])

	-- 0x4b5a07 -- temple
	mem.asmpatch(0x4b5a07, [[
	movzx eax, byte [ds:edi+eax+]] .. ClassSkillsPtr .. [[]
	mov edx, eax
	mov eax, ecx
	mov ecx, edi
	call absolute ]] .. GetRaceSkill .. [[;
	call absolute ]] .. ChooseHighest .. [[;
	mov ecx, dword [ss:ebp-0xc]
	test eax, eax]])

	-- 0x4b3f64 -- magic shop
	mem.asmpatch(0x4b3f64, [[
	movzx eax, byte [ds:esi+eax+]] .. ClassSkillsPtr .. [[]
	mov edx, eax
	mov eax, ecx
	mov ecx, esi
	call absolute ]] .. GetRaceSkill .. [[;
	call absolute ]] .. ChooseHighest .. [[;
	mov ecx, dword [ss:ebp-0x14]
	test eax, eax]])

	-- 0x4b4059 -- magic shop
	mem.asmpatch(0x4b4059, [[
	movzx eax, byte [ds:edi+eax+]] .. ClassSkillsPtr .. [[]
	mov edx, eax
	mov eax, ecx
	mov ecx, edi
	call absolute ]] .. GetRaceSkill .. [[;
	call absolute ]] .. ChooseHighest .. [[;
	mov ecx, dword [ss:ebp-0x14]
	test eax, eax]])

	-- 0x4b82b2 -- alchemist
	mem.asmpatch(0x4b82b2, [[
	movzx eax, byte [ds:esi+eax+]] .. ClassSkillsPtr .. [[]
	mov edx, eax
	mov eax, ecx
	mov ecx, esi
	call absolute ]] .. GetRaceSkill .. [[;
	call absolute ]] .. ChooseHighest .. [[;
	mov ecx, dword [ss:ebp-0x18]
	test eax, eax]])

	-- 0x4b83a7 -- alchemist
	mem.asmpatch(0x4b83a7, [[
	movzx eax, byte [ds:edi+eax+]] .. ClassSkillsPtr .. [[]
	mov edx, eax
	mov eax, ecx
	mov ecx, edi
	call absolute ]] .. GetRaceSkill .. [[;
	call absolute ]] .. ChooseHighest .. [[;
	mov ecx, dword [ss:ebp-0x18]
	test eax, eax]])

	-- 0x4b6948 -- tavern
	mem.asmpatch(0x4b6948, [[
	movzx eax, byte [ds:esi+eax+]] .. ClassSkillsPtr .. [[]
	mov edx, eax
	mov eax, ecx
	mov ecx, esi
	call absolute ]] .. GetRaceSkill .. [[;
	call absolute ]] .. ChooseHighest .. [[;
	mov ecx, dword [ss:ebp-0x18]
	test eax, eax]])

	-- 0x4b6a43 -- tavern
	mem.asmpatch(0x4b6a43, [[
	movzx eax, byte [ds:edi+eax+]] .. ClassSkillsPtr .. [[]
	mov edx, eax
	mov eax, ecx
	mov ecx, edi
	call absolute ]] .. GetRaceSkill .. [[;
	call absolute ]] .. ChooseHighest .. [[;
	mov ecx, dword [ss:ebp-0x18]
	test eax, eax]])

	-- 0x4baf6c -- Can learn 1
	mem.asmpatch(0x4baf6c, [[
	movzx eax, byte [ds:ebp+eax+]] .. ClassSkillsPtr - 0x24 .. [[]
	mov edx, eax
	mov eax, ecx
	lea ecx, dword [ss:ebp-0x24]
	call absolute ]] .. GetRaceSkill .. [[;
	call absolute ]] .. ChooseHighest .. [[;
	mov ecx, edi
	test eax, eax]])

	-- 0x4bbcf1, 0x4b4df5, 0x4b4f8c


end

function events.GameInitialized2()
	if ProcessTxt() then
		SetHooks()
	end
end
