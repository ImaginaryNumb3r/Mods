package monster;

import misc.Matrices;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Creator: Patrick
 * Created: 08.05.2019
 * Purpose:
 */
public class MonsterExecutor {
    private static final Path OUT = Path.of("monsters.out");

    public static void main(String[] args) throws IOException {
        List<String> monsterList = Files.readAllLines(Monster.FILE);
        monsterList.remove(0);
        var header = monsterList.remove(0);
        monsterList.remove(0);
        monsterList.remove(0);

        var stringMatrix = monsterList.stream()
                .map(Monster::parse)
                .sorted(Comparator.comparing(mon -> mon.LVL))
                .map(Monster::toArray)
                .filter(mon -> Integer.parseInt(mon[2]) > 3 )
                // .map(line -> line.split("\t"))
                .toArray(String[][]::new);

        stringMatrix = merge(header.split("\t"), stringMatrix);
        var str = Matrices.align(stringMatrix); /*
        System.out.println(header); */
        System.out.println(str);

        // Process monsters.
        List<Monster> monsters = monsterList.stream()
                .map(Monster::parse)
                .map(Monster::scaleDifficulty)
                .collect(Collectors.toList());

        int i = 0;
        String outStr = MonsterTable.loadHeader();
        for (Monster monster : monsters) {
            outStr += i;
            outStr += '\t';
            outStr = outStr + monster.toString() + "\n";
            ++i;
        }

        Files.deleteIfExists(OUT);
        Files.writeString(OUT, outStr);
    }

    private static String[][] merge(String[] array, String[][] matrix) {
        int length = matrix.length + 1;
        String[][] output = new String[length][];

        String[] newArray = new String[array.length - 1];
        for (int i = 1; i < array.length; ++i) {
            newArray[i - 1] = array[i];
        }
        array = newArray;

        output[0] = array;

        int i = 1;
        for (String[] strings : matrix) {
            output[i] = strings;
            ++i;
        }

        return output;
    }
}

/**
 * Range:       from level 1 to 100
 * Speed:       from 1.2 to 2.5
 * Fly Speed:   from 1.4 to 3
 * HP:          from 1.5 to 3.5
 * EXP:         straight 1.45
 * AC:          from 1.5 to 2.5
 * Range Usage: 1.5x
 * Spell Usage: 1.5x (spell 1 and 2)
 * Damage:      from 1.15 to 1.75
 *
 * Resistances:
 * Change monsters so they have unique weaknesses
 * Angel - strong: air, light, body, mind, spirit - ok: physical, dark, fire - weak: water
 * Archer - strong: earth, water, air - ok: light, dark, spirit, mind - weak: fire, physical, body
 * Behemoth - strong: fire, water, body, mind - ok: physical, light, dark, earth, spirit - weak: air
 *
 * Status Modifier:
 * Minor Status:    Drunk, Afraid, Asleep, Weak
 * Medicore Status: Disease1/2/3, Poison1/2/3/3x2, Curse, Agex2, Insane, Steal1x2
 * Severe Status:   BrkArmor, BrkWeapon, BrkItem, Stone, Uncon, Paralize
 * Major Status:    Dead, DrainSP, Errad
 *
 * Floating Eye: Insane
 * Gazer:    Curse
 * Evil Eye: Paralize (or drain SP?)
 * Minotaur: Insane/Uncon/Dead
 *
 *
 */
