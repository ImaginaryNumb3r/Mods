package model;

/**
 * Creator: Patrick
 * Created: 28.05.2019
 * Purpose:
 */
public enum Faction {
    Academy, Dungeon, Dwarf, Fortress, Haven, Inferno, Necropolis, Neutrals, Orcs, Preserve;

    public static int count() {
        return values().length;
    }
}
