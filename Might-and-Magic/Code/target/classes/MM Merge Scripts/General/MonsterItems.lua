local random, floor, ceil = math.random, math.floor, math.ceil
local MonsterItems = {
[20] = 1004,
[21] = 1004,
[24] = 1006,
[64] = 1006,
[74] = 1016,
[75] = 1006,
[81] = 207,
[92] = 202,
[95] = 1009,
[150] = 1004,
[167] = 1016,
[168] = 1016,
[172] = 1006,
[186] = 1009

}

function events.MonsterKilled(mon)
	if mon.Ally == 9999 then -- no drop from reanimated monsters
		return
	end

	local monKind = ceil(mon.Id/3)
	local Mul = 1 - (monKind - mon.Id/3)
	local ItemId = MonsterItems[monKind]
	if ItemId and random(100) < (33 + 33*Mul) then
		evt.SummonObject(ItemId, mon.X, mon.Y, mon.Z + 10, 100)
	end
end

-- Make monsters in indoor maps active once party sees them.
local IsIndoor, NeedMonHostileUpd = false, false

local function ActiveMonTimer()
	if NeedMonHostileUpd then
		local MonList = Game.GetMonstersInSight()
		local mon
		local lim = Map.Monsters.count
		for k,v in pairs(MonList) do
			if v < lim then
				mon = Map.Monsters[v]
				mon.Active = true
				mon.ShowOnMap = true
			end
		end
		NeedMonHostileUpd = false
	end
end

local function SetMonUpdFlag()
	if IsIndoor then
		NeedMonHostileUpd = true
	end
end

function events.AfterLoadMap()
	IsIndoor = Map.IsIndoor()
	if IsIndoor then
		Timer(ActiveMonTimer, const.Minute*2, false)
		events.StepSound = SetMonUpdFlag
		events.CalcDamageToMonster = SetMonUpdFlag
	else
		events.Remove("StepSound", SetMonUpdFlag)
		events.Remove("CalcDamageToMonster", SetMonUpdFlag)
	end
end
