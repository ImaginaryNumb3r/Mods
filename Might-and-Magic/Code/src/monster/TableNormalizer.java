package monster;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static monster.MonsterTable.EOL;

/**
 * Creator: Patrick
 * Created: 10.05.2019
 * Purpose:
 */
public class TableNormalizer {
    private static final int Spell1Index = 25;
    private static final int Spell2Index = 27;

    public static void normalize(List<String[]> matrix) {

        int i = -1;
        for (String[] line : matrix) {
            ++i;
            if (i < 4) continue;

            String spell1 = line[Spell1Index];
            String spell2 = line[Spell2Index];

            if (!spell1.startsWith("\"") && !spell1.equals("0") ) {
                spell1 = "\"" + spell1 + "\"";
            }
            if (!spell2.startsWith("\"") && !spell2.equals("0") ) {
                spell2 = "\"" + spell2 + "\"";
            }

            line[Spell1Index] = spell1;
            line[Spell2Index] = spell2;
        } /*

        return matrix.stream().map(parts -> {
            if (parts.length == 0) return "";

            String line = parts[0];
            for (int j = 1; j != parts.length; j++) {
                line += "\t" + parts[j];
            }

            return line;
        }).collect(Collectors.joining(EOL)); */
    }

    public static void main(String[] args) throws IOException {
        Path file = Path.of("D:\\Spiele\\Legacy\\Might and Magic VII\\Projects\\Spell Calc\\target\\monsters.txt");
        String fileStr = Files.readString(file);

        List<String> lines = Files.readAllLines(file);
        List<String[]> matrix = lines.stream()
                .map(line -> line.split("\t"))
                .collect(Collectors.toList());

        // String string = TableNormalizer.normalize(matrix);

        /*
        Files.delete(file);
        Files.writeString(file, string); */
    }
}
