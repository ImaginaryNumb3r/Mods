package monster;

import lib.Numbers;

/**
 * Creator: Patrick
 * Created: 09.05.2019
 * Purpose:
 */
public class Resistance {
    public static final Resistance Imm = valueOf("Imm");
    public static final Resistance None = valueOf(0);
    public static final String[] VALUES = new String[]{
            "Fire", "Air", "Water", "Earth", "Mind",
            "Spirit", "Body", "Light", "Dark", "Phys" };
    private static final String IMMUNE = "Imm";
    public Integer value;

    public Resistance(Integer value) {
        this.value = value;
    }

    public void scale(double scalar) {
        if (value != null) {
            value = (int) (value * scalar);
        }
    }

    public void roundUp() {
        if (value != null) {
            value = Numbers.roundUp(value, 5);
        }
    }

    public void round() {
        if (value != null) {
            value = Numbers.round(value, 5);
        }
    }

    public void add(int add) {
        if (value != null) {
            value += add;
        }
    }

    public static Resistance valueOf(double val) {
        return valueOf((int) val);
    }

    public static Resistance valueOf(int val) {
        return valueOf(String.valueOf(val));
    }

    public static Resistance valueOf(String str) {
        Integer val;
        if (str.equals(IMMUNE)) {
            val = null;
        } else {
            val = Integer.valueOf(str);
        }

        return new Resistance(val);
    }

    @Override
    public String toString() {
        return value == null ? IMMUNE : value.toString();
    }
}
