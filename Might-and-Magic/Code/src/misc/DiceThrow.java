package misc;

import java.util.Objects;

/**
 * Creator: Patrick
 * Created: 11.05.2019
 * Purpose:
 */
public abstract class DiceThrow<T> {
    public int dice;
    public int faces;
    public T extra;

    public abstract DiceThrow<T> make(int dice, int faces);

    public DiceThrow(int dice, int faces, T extra) {
        this.dice = dice;
        this.faces = faces;
        this.extra = extra;
    }

    private boolean hasExtra() {
        return extra != null
                && !extra.toString().isEmpty()
                && !extra.toString().equals("0");
    }

    public double average() {
        return DamageCalc.averageDamage(dice, faces);
    }

    @Override
    public String toString() {
        String postfix = hasExtra() ? "+" + extra : "";

        return dice + "D" + faces + postfix;
    }

    public abstract void scaleExtra(double scalar, int add);

    public void scaleExtra(double scalar) {
        this.scaleExtra(scalar, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiceThrow that = (DiceThrow) o;
        return dice == that.dice &&
                faces == that.faces;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dice, faces);
    }

    public Dist distance(DiceThrow<?> other) {
        int dice = other.dice - this.dice;
        int faces  = other.faces - this.faces;

        return new Dist(Math.abs(dice), Math.abs(faces));
    }

    public class Dist{
        public int diceDist;
        public int facesDist;

        public Dist(int diceDist, int facesDist) {
            this.diceDist = diceDist;
            this.facesDist = facesDist;
        }

        public Dist() { }

        public int dist() {
            return diceDist + facesDist * 2;
        }

        @Override
        public String toString() {
            return "Dist{" +
                    "diceDist=" + diceDist +
                    ", facesDist=" + facesDist +
                    '}';
        }
    }
}
