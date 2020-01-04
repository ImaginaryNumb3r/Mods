package misc;

/**
 * Creator: Patrick
 * Created: 10.05.2019
 * Purpose:
 */
public class DiceTreasure extends DiceThrow<String> {

    public DiceTreasure(int dice, int faces) {
        this(dice, faces, "");
    }

    @Override
    public DiceTreasure make(int dice, int faces) {
        return new DiceTreasure(dice, faces);
    }

    public DiceTreasure(int dice, int faces, String item) {
        super(dice, faces, item);
    }

    @Override
    public void scaleExtra(double scalar, int add) {
        // Noop
    }

    public static DiceTreasure parse(String str) {
        // Hack: Last minute hack
        if (str.equals("300D10L5")) return DiceTreasure.parse("220D13+L5");

        int dice, faces;
        String attackExtra = "";

        String[] parts = str.split("[+]");
        if (parts.length == 2) attackExtra = parts[1];
        parts = parts[0].toLowerCase().split("d");
        try {
            dice = Integer.parseInt(parts[0]);
            faces = parts.length == 1
                    ? 0
                    :  Integer.parseInt(parts[1]);
        } catch (NumberFormatException ex) {
            dice = 0;
            faces = 0;
        }

        return new DiceTreasure(dice, faces, attackExtra);
    }

}
