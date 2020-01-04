package monster;

import misc.DamageCalc;
import misc.DiceDamage;
import misc.DiceTreasure;

import java.util.function.Consumer;

import static java.lang.Integer.parseInt;

/**
 * Creator: Patrick
 * Created: 11.05.2019
 * Purpose:
 */
public class Treasure {

    /**
     * The following cases are possible:
     * 	    - Item Chance + Treasure
     * 	       5%50D20+L4Sword (Angel)
     * 	    - Item Chance
     * 	       5%L1Ring (Vampire Bat)
     * 	    - Treasure
     * 	       50D10 (Young Behemoth)
     * 	    - Treasure Chance
     * 	       10%5D4 (Zombie)
     * 	    - Item + Treasure
     * 	       50D10+L3 (Fire Hydra)
     */
    public static String scale(double scalar, final String itemTreasureStr) {
        if (itemTreasureStr.equals("0") || itemTreasureStr.isBlank()) return itemTreasureStr;

        String[] parts = itemTreasureStr.split("%");
        if (parts.length > 2) throw new MonsterParseException("Monster treasure contains more than one \"%\".");

        String item;
        Integer dropChance;
        String treasureStr;
        if (parts.length == 2) {
            dropChance = parseInt(parts[0]);
            treasureStr = parts[1];
        } else {
            dropChance = null;
            treasureStr = parts[0];
        }

        // If the first character is alphabetic, there will be no gold treasure, but only an item.
        char firstChar = treasureStr.charAt(0);
        if (Character.isAlphabetic(firstChar)) {
            item = treasureStr;
            treasureStr = null;
        } // Parse with gold treasure.
        else {
            String[] treasureItem = treasureStr.split("[+]");
            if (treasureItem.length > 2) throw new MonsterParseException("Monster treasure contains more than one \"+\".");

            DiceTreasure diceTreasure = DiceTreasure.parse(treasureItem[0]);
            diceTreasure = DamageCalc.scaleToClosestRange(scalar, diceTreasure);
            treasureStr = diceTreasure.toString();

            item = treasureItem.length == 2 ? treasureItem[1] : null;
        }

        String newTreasure = "";
        if (dropChance != null) {
            dropChance = (int) (dropChance * 1.2);
            newTreasure = dropChance.toString() + "%";
        }
        if (treasureStr != null) {
            newTreasure += treasureStr;
            if (item != null) newTreasure += "+";
        }

        if (item != null) newTreasure += item;

        return newTreasure;
    }

    /*
    public static String scale(double scalar, String treasureStr) {
        if (treasureStr.equals("0")) return treasureStr;

        String[] parts = treasureStr.split("%");
        String diceTreasureStr;
        String item = null;
        Integer percent;
        if (parts.length == 2) {
            percent = parseInt(parts[0]);
            diceTreasureStr = parts[1];

            parts = diceTreasureStr.split("[+]");
            if (parts.length == 2){
                diceTreasureStr = parts[0];
                item = parts[1];
            } else if (parts.length == 1) {
                item = parts[0];
                diceTreasureStr = null;
            } else throw new IllegalStateException();

        } else if (parts.length == 1) {
            percent = null;

            String temp = parts[0];
            parts = temp.split("[+]");

            diceTreasureStr = parts[0];
            if (parts.length == 2) {
                item = parts[0];
            }
            // parts array of length 1 is valid.
            else if (parts.length > 2) throw new IllegalStateException();
        }
        else throw new IllegalStateException();

        if (diceTreasureStr != null) {
            DiceTreasure newTreasure = DamageCalc.scaleToClosestRange(scalar, DiceTreasure.parse(diceTreasureStr));
            diceTreasureStr = newTreasure.toString();
        } else {
            diceTreasureStr = "";
        }
        if (percent != null) {
            percent = ((int) (percent * 1.2)) + 5;
            diceTreasureStr = percent + "%" + diceTreasureStr;
        }
        if (item != null) {
            if (!diceTreasureStr.endsWith("%")) {
                diceTreasureStr += "+";
            }
            diceTreasureStr += item;
        }

        return diceTreasureStr;
    } */

}
