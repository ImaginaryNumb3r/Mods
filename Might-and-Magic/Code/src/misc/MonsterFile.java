package misc;

import monster.Monster;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Creator: Patrick
 * Created: 11.05.2019
 * Purpose:
 */
public class MonsterFile {

    public static void mutate(Function<Monster, Monster> mutator, Path inFile, Path outFile) {
        try {
            var input = Files.readAllLines(inFile);
            var output = new ArrayList<String>();

            for (String line : input) {
                // Ignore all non-monster entries
                boolean parse = !line.isEmpty() && isNumber(line.charAt(0));
                if (parse) {
                    Monster monster = Monster.parse(line);
                    monster = mutator.apply(monster);
                    output.add(monster.toString());
                } else {
                    output.add(line);
                }
            }

            Files.delete(outFile);
            Files.write(outFile, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> mutate(Path inFile, Function<Monster, Monster> action) throws IOException {
        var input = Files.readAllLines(inFile);
        var output = new ArrayList<String>();

        for (String line : input) {
            // Ignore all non-monster entries
            boolean parse = !line.isEmpty() && isNumber(line.charAt(0));
            if (parse) {
                Monster monster = Monster.parse(line);
                monster = action.apply(monster);
                output.add(monster.toString());
            } else {
                output.add(line);
            }
        }

        return output;
    }

    private static boolean isNumber(char ch) {
        return ch >= '0' && ch <= '9';
    }

    public static void main(String[] args) {
        // Mutate treasures
        Path inFile = Path.of("target\\monsters.txt");
        Path outFile = Path.of("target\\monsters.custom");
        mutate(Monster::scaleTreasure, inFile, outFile);
    }
}
