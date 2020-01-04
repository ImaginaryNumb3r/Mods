package monster;

import misc.DamageCalc;
import misc.DiceDamage;
import misc.FieldHandle;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static monster.MonsterTable.NA;

/**
 * Creator: Patrick
 * Created: 08.05.2019
 * Purpose:
 */
public class Monster {
    public static final Path FILE = Path.of("monsters.txt");

    public Integer Nr; public String Name; public String Picture; public Integer LVL; public Integer HP; public Integer AC;
    public Integer EXP; public String Treasure; public Integer Quest; public String Fly; public String Move;
    public String AiType; public Integer Hst; public Integer Spd; public Integer Rec; public String Pref; public String Bonus;

    // Close Combat attack
    public String AttType;
    public String AttDamage;
    public String AttMiss;

    // Ranged Combat attack
    public Integer RangedUse;
    public String RangedType;
    public String RangedDamage;
    public String RangedMiss;

    // Spell 1
    public Integer Spell1Use;
    public String Spell1Details;

    // Spell 2
    public Integer Spell2Use;
    public String Spell2Details;

    // Resistances
    public Resistance Fire;
    public Resistance Air;
    public Resistance Water;
    public Resistance Earth;
    public Resistance Mind;
    public Resistance Spirit;
    public Resistance Body;
    public Resistance Light;
    public Resistance Dark;
    public Resistance Phys;
    public String Special;

    private Monster() { }

    public static Monster parse(String line) {
        String[] parts = line.split("\t"); /*
        Object[] objects = Arrays.stream(parts).map(Monster::parseItem).toArray();
        Object[] newObjects = new Object[objects.length - 1];
        for (int i = 0; i != objects.length - 1; ++i) {
            newObjects[i] = objects[i + 1];
        } */

        return FieldHandle.setFields(parts, Monster::new);
    }

    public Monster scaleTreasure() {
        String newTreasure = monster.Treasure.scale(1.5, Treasure);
        Treasure = newTreasure;

        return this;
    }

    public double naiveStatRating() {
        double dps = DiceDamage.parse(AttDamage).average() / Rec;
        return dps * HP;
    }

    public void normalizeSpells() {
        Function<String, String> normalize = spell -> spell.equals(NA)
                ? "0"
                : "\"" + spell + "\"";
        Spell1Details = normalize.apply(Spell1Details);
        Spell2Details = normalize.apply(Spell2Details);
    }

    public Monster scaleDifficulty() {
        if (flies()) Spd = scale(1.4, 3, Spd);
        if (!flies()) Spd = scale(1.2, 2.5, Spd);
        // HP = scale(3, 4, HP);
        // EXP = scale(1.45, EXP);
        // AC = scale(2, 3, AC);
        RangedUse = scale(1.45, RangedUse);
        Spell1Use = scale(1.45, Spell1Use);
        Spell2Use = scale(1.45, Spell2Use);

        /*
        FieldHandle.visitFields(this, (type, value) -> {
            Object val = value;
            if (type.equals(Resistance.class)) {
                var res = (Resistance) val;
                res.scale(1.2);
            }

            return val;
        }); */


        if (!AttDamage.equals("0")) {
            DiceDamage targetDamage = DamageCalc.scaleToClosest(getScalar(1.3, 1.75), DiceDamage.parse(AttDamage));
            AttDamage = normalize(DiceDamage.parse(AttDamage), targetDamage).toString();
        }
        if (!RangedDamage.equals("0")) {
            DiceDamage rangedDamage = DamageCalc.scaleToClosest(getScalar(1.3, 1.75), DiceDamage.parse(RangedDamage));
            RangedDamage = normalize(DiceDamage.parse(RangedDamage), rangedDamage).toString();
        }

        List<Integer> chances = Arrays.asList(RangedUse, Spell1Use, Spell2Use);
        for (Integer chance : chances) {
            if (chance > 99) {
                throw new IllegalStateException("Cannot have a chance that is 100% or more");
            }
        }

        return this;
    }

    private DiceDamage normalize(DiceDamage original, DiceDamage newDD) {
        newDD.extra = scale(1.0, 1.3, newDD.extra) + 5;
        if (!original.equals(newDD)) return newDD;

        int faces = original.faces;
        int dice = original.dice;

        if (faces > dice) {
            ++dice;
        } else {
            ++faces;
        }

        return new DiceDamage(dice, faces, newDD.extra);
    }

    public boolean flies() {
        return Fly.equals("Y");
    }

    private int scale(double low, double high, int value) {
        return (int) scale(low, high, (double) value);
    }

    private double getScalar(double low, double high) {
        double range = high - low;
        double relativeScalar = range / 100 * LVL;

        return (low + (range * relativeScalar));
    }

    private double scale(double low, double high, double value) {
        double range = high - low;
        double relativeScalar = range / 100 * LVL;

        return (low + (range * relativeScalar)) * value;
    }

    private int scale(double scale, int value) {
        return (int) (scale * value);
    }

    public String[] toArray() {
        String string = toString();

        return string.split("\t");
    }

    private static Object parseItem(String item) {
        try  {
            return Integer.valueOf(item);
        } catch (NumberFormatException ex) {
            return item;
        }
    }

    public String groupName() {
        String[] parts = Picture.split("[ ]");
        String[] groupNameParts = new String[parts.length - 1];
        for (int i = 0; i != parts.length - 1; ++i) {
            groupNameParts[i] = parts[i];
        }

        return String.join(" ", groupNameParts);
    }

    public Ranking getRanking() {
        String[] split = Picture.split("[ ]");
        String ranking = split[split.length - 1];

        return Ranking.parse(ranking);
    }

    @Override
    public String toString() {
        var fields = FieldHandle.getFields(this);
        List<String> fieldNames = fields.stream().map(Object::toString).collect(Collectors.toList());

        return String.join("\t", fieldNames);
    }

    public enum Ranking {
        MINOR, NORMAL, MAJOR;

        public static Ranking parse(String str) {
            return switch (str.toLowerCase()) {
                case "a" -> MINOR;
                case "b" -> NORMAL;
                case "c" -> MAJOR;
                default -> throw new IllegalArgumentException("Cannot parse ranking of Monster for value: " + str);
            };
        }
    }
}
