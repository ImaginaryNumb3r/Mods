package misc;

/**
 * Creator: Patrick
 * Created: 10.05.2019
 * Purpose:
 */
public class DiceDamage extends DiceThrow<Integer> {

    public DiceDamage(int dice, int faces) {
        this(dice, faces, 0);
    }

    public DiceDamage(int dice, int faces, int attackExtra) {
        super(dice, faces, attackExtra);
    }

    public static DiceDamage parse(String str) {
        int dice, faces, attackExtra = 0;

        String[] parts = str.split("[+]");
        if (parts.length == 2) attackExtra = Integer.parseInt(parts[1]);
        parts = parts[0].toLowerCase().split("d");
        dice = Integer.parseInt(parts[0]);
        faces = Integer.parseInt(parts[1]);

        return new DiceDamage(dice, faces, attackExtra);
    }

    @Override
    public DiceDamage make(int dice, int faces) {
        return new DiceDamage(dice, faces);
    }

    @Override
    public void scaleExtra(double scalar, int add) {
        extra = (int) (extra * scalar) + add;
    }
}
