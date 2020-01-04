package spell;

/**
 * Creator: Patrick
 * Created: 02.05.2019
 * Purpose:
 */
public enum Mastery {
    NORMAL { int level = 0; },
    EXPERT { int level = 4; },
    MASTER { int level = 8; },
    GRANDMASTER { int level = 10; };

    public int level;

    public static Mastery parseLevel(int level) {
        if (level < 0) throw new IllegalArgumentException();

        Mastery mastery;
        if (level < 4) mastery = Mastery.NORMAL;
        else if (level < 8) mastery = Mastery.EXPERT;
        else if (level < 10) mastery = Mastery.MASTER;
        else mastery = Mastery.GRANDMASTER;

        return mastery;
    }



}
