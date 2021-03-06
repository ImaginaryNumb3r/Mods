-- Config:

local BinFolder = "DataFiles/"

-- /Config

BinFolder = path.addslash(BinFolder)
path.CreateDirectory(BinFolder)
path.CreateDirectory("Data/Tables/")

local UpdateMode
local TimesTable = {}
local timebuf = mem.StaticAlloc(8)

local function SetFileTime(fname, time1, time2)
	mem.u4[timebuf] = time1
	mem.u4[timebuf + 4] = time2
	local kernel32 = mem.dll.kernel32
	-- fname, FILE_WRITE_ATTRIBUTES, all share flags, nil, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, nil
	local h = kernel32.CreateFileA(fname, 0x0100, 7, 0, 3, 0x80, 0)
	kernel32.SetFileTime(h, 0, 0, timebuf)
	kernel32.CloseHandle(h)
end

local function FixFileTimes()
	for _, t in ipairs(TimesTable) do
		SetFileTime(unpack(t))
	end
	TimesTable = {}
end

local function DataTable(name, f, checkBin)
	nameTxt = "Data/Tables/"..name..'.txt'
	errorinfo('file "'..nameTxt..'"')
	local time1, time2
	if not UpdateMode then
		for _, a in path.find(nameTxt) do
			time1, time2 = a.LastWriteTimeLow, a.LastWriteTimeHigh
		end
	end
	if not time1 then
		if name == 'SFT' then
			MessageBox("MMExtension is about to generate text tables for binary files. This will take a few minutes. On the next run of the game you will also experience a delay.")
		end
		io.SaveString(nameTxt, f())
	else
		if checkBin then
			for _, a in path.find(BinFolder.."d"..name..'.bin') do
				local t1, t2 = a.LastWriteTimeLow, a.LastWriteTimeHigh
				time1 = (time2 ~= t2 or time1 ~= t1) and time1
			end
		end
		if time1 then
			f(io.LoadString(nameTxt))
			errorinfo('')
			if checkBin then
				TimesTable[#TimesTable + 1] = {BinFolder.."d"..name..'.bin', time1, time2}
			end
			return true
		end
	end
	errorinfo('')
end

local function StructsArray(arr, offs, tabl)
	tabl = tabl or {}
	return function(str)
		return DataTables.StructsArray(arr, offs, table.copy(tabl, {Resisable = true, IgnoreFields = {SFTIndex = true, Bits = true}}, true), str)
	end
end

local function SaveBin(name, t)
	io.SaveString(BinFolder.."d"..name..".bin", DataTables.ToBin(t))
	FixFileTimes()
end

local function update()
	local sameSFT = true
	DataTable('Class HP SP', DataTables.HPSP)
	if Game.Version ~= 6 then
		DataTable('Class Skills', DataTables.Skills)
	end
	DataTable('Class Starting Skills', DataTables.StartingSkills)
	DataTable('Class Starting Stats', DataTables.StartingStats)
	DataTable('House Movies', DataTables.HouseMovies)
	local FtIgnore = {TotalTime = true, NotGroupEnd = true, Bits = true, SpriteIndex = true, PaletteIndex = true, IconIndex = true, Index = true}
	if DataTable('SFT', StructsArray(Game.SFTBin.Frames, structs.o.SFTItem, {IgnoreFields = FtIgnore}), true) then
		sameSFT = false
		DataTables.UpdateSFTGroups()
		io.SaveString(BinFolder.."dsft.bin", DataTables.STFToBin())
		FixFileTimes()
	end
	if DataTable('DecList', StructsArray(Game.DecListBin, structs.o.DecListItem), sameSFT) then
		SaveBin('declist', Game.DecListBin)
	end
	if DataTable('PFT', StructsArray(Game.PFTBin, structs.o.PFTItem, {NoRowHeaders = true, IgnoreFields = FtIgnore}), true) then
		DataTables.UpdatePFTGroups()
		SaveBin('pft', Game.PFTBin)
	end
	if DataTable('IFT', StructsArray(Game.IFTBin, structs.o.IFTItem, {IgnoreFields = FtIgnore}), true) then
		DataTables.UpdateIFTGroups()
		SaveBin('ift', Game.IFTBin)
	end
	if DataTable('TFT', StructsArray(Game.TFTBin, structs.o.TFTItem, {IgnoreFields = FtIgnore}), true) then
		DataTables.UpdateTFTGroups()
		SaveBin('tft', Game.TFTBin)
	end
	if DataTable('Chest', StructsArray(Game.ChestBin, structs.o.DChestItem), true) then
		SaveBin('chest', Game.ChestBin)
	end
	if DataTable('Overlay', StructsArray(Game.OverlayBin, structs.o.OverlayItem, {NoRowHeaders = true}), sameSFT) then
		SaveBin('overlay', Game.OverlayBin)
	end
	local param = {NoRowHeaders = true, IgnoreFields = {SFTIndex = true, Bits = true, LoadedParticlesColor = true}}
	if DataTable('ObjList', StructsArray(Game.ObjListBin, structs.o.ObjListItem, param), sameSFT) then
		SaveBin('objlist', Game.ObjListBin)
	end
	if DataTable('MonList', StructsArray(Game.MonListBin, structs.o.MonListItem, {IgnoreFields = {Tint = true}}), sameSFT) then
		SaveBin('monlist', Game.MonListBin)
	end
	local param = {NoRowHeaders = true, IgnoreFields = {Locked = true, Bits = true, Data3D = true, Decompressed = true},
	               Alias = {Type = {system = 1, swap = 2, lock = 4}}}
	if DataTable('Sounds', StructsArray(Game.SoundsBin, structs.o.SoundsItem, param), true) then
		SaveBin('sounds', Game.SoundsBin)
	end
	if DataTable('Tile', StructsArray(Game.TileBin, structs.o.TileItem, {IgnoreFields = {Bits = true, Bitmap = true}}), true) then
		SaveBin('tile', Game.TileBin)
	end
	if Game.Version == 8 then
		if DataTable('Tile2', StructsArray(Game.Tile2Bin, structs.o.TileItem, {IgnoreFields = {Bits = true, Bitmap = true}}), true) then
			SaveBin('tile2', Game.Tile2Bin)
		end
		if DataTable('Tile3', StructsArray(Game.Tile3Bin, structs.o.TileItem, {IgnoreFields = {Bits = true, Bitmap = true}}), true) then
			SaveBin('tile3', Game.Tile3Bin)
		end
	end
	FixFileTimes()  -- just to be sure
end

local function update2()
	DataTable('Town Portal', DataTables.TownPortal)
	do  -- Spells2
		local hdr = {}
		hdr[-1] = 'Spell'
		for i, sp in Game.SpellsTxt do
			hdr[i] = sp.Name
		end
		local is6 = (Game.Version == 6) or nil
		DataTable('Spells2', StructsArray(Game.Spells, structs.o.SpellInfo, {Resisable = false, RowHeaders = hdr, IgnoreFields = 
		          {CastByMonster = true, CastByEvent = true, CauseDamage = true, SpecialDamage = true, Bits = true,
		           SpellPointsNormal = is6, SpellPointsExpert = is6, SpellPointsMaster = is6}}))
	end
	DataTable('Transport Index', StructsArray(Game.TransportIndex, {[1] = 1, [2] = 2, [3] = 3},
		{Resisable = false, RowHeaders = {[Game.TransportIndex.low - 1] = "2D Event"}}))
	DataTable('Transport Locations', StructsArray(Game.TransportLocations, structs.o.TravelInfo,
		{Resisable = false, IgnoreFields = {MapIndex = true}}))
	FixFileTimes()  -- just to be sure
end

function events.GameInitialized1()
	update()
end

function events.GameInitialized2()
	update2()
	UpdateMode = true
end

function UpdateDataTables()
	update()
	update2()
end
