
local function GetPlayer(ptr)
	local PLId = (ptr - Party.PlayersArray[0]["?ptr"]) / Party.PlayersArray[0]["?size"]
	local PL = Party.PlayersArray[PLId]
	return PL, PlId
end

local function GetMonster(ptr)
	local MonId = (ptr - Map.Monsters[0]["?ptr"]) / Map.Monsters[0]["?size"]
	local Mon = Map.Monsters[MonId]
	return Mon, MonId
end

-- Change chance calculation for "slow" and "mass distortion" spells to be applied.
local function CanApplySpell(Skill, Mastery, Resistance)
	if Resistance == const.MonsterImmune then
		return false
	else
		return (math.random(5, 100) + Skill + Mastery*2.5) > Resistance
	end
end

local function CanApplySlowMassDistort(d)
	local PL = GetPlayer(mem.u4[d.ebp-0x1c])
	local Skill, Mastery = SplitSkill(PL:GetSkill(const.Skills.Earth))

	local Mon = GetMonster(d.eax)
	local Res = Mon.Resistances[const.Damage.Earth]

	if CanApplySpell(Skill, Mastery, Res) then
		d.eax = 1
	else
		d.eax = 0
	end
end

mem.nop(0x426f97, 3)
mem.hook(0x426fa2, CanApplySlowMassDistort)
mem.nop(0x426910, 2)
mem.nop(0x426918, 1)
mem.hook(0x42691e, CanApplySlowMassDistort)

-- Make Stun paralyze target for very small duration
mem.autohook2(0x437751, function(d)
	local Player = GetPlayer(d.ebx)
	local Skill, Mas = SplitSkill(Player:GetSkill(const.Skills.Earth))
	local mon = GetMonster(d.ecx)

	local Buff = mon.SpellBuffs[const.MonsterBuff.Paralyze]
	Buff.ExpireTime = Game.Time + const.Minute/3 + Skill*Mas
end)

-- Change chance of monster being stunned
mem.autohook2(0x437713, function(d)
	local Player = GetPlayer(d.ebx)
	local Mon = GetMonster(d.esi)
	local Skill, Mastery = SplitSkill(Player:GetSkill(const.Skills.Earth))

	if CanApplySpell(Skill, Mastery, Mon.Resistances[const.Damage.Earth]) then
		d.eax = 1
	else
		d.eax = 0
	end
end)
