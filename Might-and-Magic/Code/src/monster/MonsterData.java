package monster;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;

/**
 * Creator: Patrick
 * Created: 12.05.2019
 * Purpose:
 */
public class MonsterData {
    public static Path OUTPUT_FILE = Path.of("target\\monsters.txt");
    public static Path BASE_FILE = Path.of("Code", "MM7", "monsters.base");
    public static Path ORIGINAL_FILE = Path.of("Code", "MM7", "monsters_original.txt");
    public static Path SPECIAL_FILE = Path.of("Code", "MM7", "monsters.special");
    public static final Path HEADER_FILE = Path.of("monster.header");

    /**
     * @return List of HP implicitly mapped to their level through their respective indices.
     */
    public static List<Integer> getNewHPList() throws IOException {
        int sequence = 0;
        int sequenceSteps = 2;
        int hpAdd = 8;

        var hpIndex = new ArrayList<>(asList(1, 23, 26, 29));
        int hp = hpIndex.get(3);

        for (int level = 4; level <= 100; ++level) {
            hp += hpAdd;
            hpIndex.add(hp);

            ++sequence;
            if (sequence == sequenceSteps) {
                ++hpAdd;
                sequence = 0;
            }
        }
        return hpIndex;
    }

    public static List<String[]> loadSpecial() throws IOException {
        return Files.lines(SPECIAL_FILE)
                .map(line -> line.split("\t"))
                .collect(Collectors.toList());
    }

    public static List<Integer> getNewEXPList() {
        List<Integer> expList = new ArrayList<>();

        int exp = 0;
        int expStep = 11;
        int expStepStep = 4;

        for (int i = 0; i <= 100; ++i) {
            expList.add(exp);

            exp += expStep;
            expStep += expStepStep;
        }

        return expList;
    }
}
