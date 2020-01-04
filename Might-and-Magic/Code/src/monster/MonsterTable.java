package monster;

import lib.Numbers;
import misc.FieldHandle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static monster.MonsterData.*;

/**
 * Creator: Patrick
 * Created: 13.05.2019
 * Purpose:
 */
public class MonsterTable {
    public static final String NA = "0";
    public static final String EOL = "\r\n";
    private final String HEADER;
    private final List<MonsterGroup> _monsterGroups;

    public MonsterTable(List<Monster> monsters) throws IOException {
        this(monsters, loadHeader());
    }

    public MonsterTable(List<Monster> monsters, String header) {
        HEADER = header;
        _monsterGroups = new ArrayList<>();

        while (!monsters.isEmpty()) {
            _monsterGroups.add(MonsterGroup.reduce(monsters));
        }
    }

    @Override
    public String toString() {
        forEachMonster(Monster::normalizeSpells);

        var content = _monsterGroups.stream()
                .map(MonsterGroup::toString)
                .collect(Collectors.joining(EOL));

        return HEADER + content;
    }

    public static String loadHeader() throws IOException {
        return Files.readString(MonsterData.HEADER_FILE);
    }

    public void applySpecials() {
        try {
            List<String[]> monsterSpecials = MonsterData.loadSpecial();
            HashMap<String, String[]> specialMap = new HashMap<>();
            for (String[] special : monsterSpecials) {
                if (special.length > 2) {
                    specialMap.put(special[2], special);
                }
            }

            for (MonsterGroup group : _monsterGroups) {
                String[] specials = specialMap.get(group.getMinor().Picture);
                var prev = applySpecials(group.getMinor(), specials, null);

                // With each step, overwrite the chances from the entries from the previous data set.
                specials = specialMap.get(group.getNormal().Picture);
                prev = applySpecials(group.getNormal(), specials, prev);

                specials = specialMap.get(group.getMajor().Picture);
                applySpecials(group.getMajor(), specials, prev);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] applySpecials(Monster monster, String[] elements, String[] fallback){
        // Ignore monsters without resistances such as peasants.
        // Full elements
        if (fallback == null) {
            fallback = IntStream.range(0, 14).boxed()
                    .map(val -> NA)
                    .toArray(String[]::new);
        }
        if (elements.length < 14) {
            var newElements = new String[14];
            System.arraycopy(elements, 0, newElements, 0, elements.length);
            for (int i = elements.length; i < 14; i++) {
                newElements[i] = "0";
            }
            elements = newElements;
        }

        // Start at 4, this is where the resistances start (we don't want to copy bonuses)
        for (int i = 4; i < elements.length; ++i) {
            String element = elements[i];
            if (element.isBlank() || element.equals(NA)) {
                elements[i] = fallback[i];
            }
        }

        var fieldMap = new HashMap<String, Object>();

        fieldMap.put("Bonus", elements[3]);
        int i = 4;
        for (String resistance : Resistance.VALUES) {
            try {
                fieldMap.put(resistance, elements[i++]);
            } catch (Exception ex) {
                System.out.printf("");
            }
        }

        var extremeRes = Resistance.valueOf(monster.LVL * 3);
        var veryHighPlusRes = Resistance.valueOf(monster.LVL * 1.5);
        var veryHighRes = Resistance.valueOf(monster.LVL);
        var highRes = Resistance.valueOf(monster.LVL * 2 / 3);
        var medRes = Resistance.valueOf(monster.LVL * 0.5);
        var LowRes = Resistance.valueOf(monster.LVL * 0.2);
        monster.Name = elements[1];

        FieldHandle.setFields(monster, fieldMap, (type, curVal, newVal) -> {
            if (type == String.class) {
                return newVal;
            }

            String valueStr = newVal.toString().toLowerCase();
            var res =  switch (valueStr) {
                case "extreme" -> extremeRes;
                case "vhigh+" -> veryHighPlusRes;
                case "vhigh" -> veryHighRes;
                case "high" -> highRes;
                case "med" -> medRes;
                case "low" -> LowRes;
                case "imm" -> Resistance.Imm;
                case "none", NA -> Resistance.None;
                // Fallback: to old variable
                case "" -> (Resistance) curVal;
                default -> Resistance.valueOf(valueStr);
            };

            res.add(2);
            res.round();
            return res;
        });

        return elements;
    }

    public void applyNewStats() {
        try {
            var hpList = MonsterData.getNewHPList();
            var expList = MonsterData.getNewEXPList();
            for (MonsterGroup monsterGroup : _monsterGroups) {
                for (Monster monster : monsterGroup) {
                    monster.HP = Numbers.round(hpList.get(monster.LVL), 5);
                    monster.AC = (int) (monster.AC * 1.2);
                    monster.EXP = expList.get(monster.LVL);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static MonsterTable parse(Path file) throws IOException {
        Path path = file.toAbsolutePath();
        List<String> lines = Files.readAllLines(file);
        String header = "";

        int i = 0;
        while (i++ != 4) {
            header += lines.remove(0) + EOL;
        }

        var monsters = lines.stream()
                .map(Monster::parse)
                .collect(Collectors.toList());

        return new MonsterTable(monsters, header);
    }

    public void adjustLevel() {
        for (MonsterGroup group : _monsterGroups) {
            int majorLevel = group.getMajor().LVL;
            int delta = majorLevel - group.getNormal().LVL;
            group.getNormal().LVL += (int) (delta / 3d);

            delta = majorLevel - group.getMinor().LVL;
            group.getMinor().LVL += (int) (delta / 3d);
        }
    }

    public void forEachMonster(Consumer<Monster> consumer) {
        for (MonsterGroup monsterGroup : _monsterGroups) {
            for (Monster monster : monsterGroup) {
                consumer.accept(monster);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        MonsterTable table = parse(BASE_FILE);
        table.adjustLevel();
        table.applySpecials();
        table.applyNewStats();
        table.forEachMonster(Monster::scaleTreasure);
        table.forEachMonster(Monster::scaleDifficulty);

        System.out.println(table.toString());
    }
}
