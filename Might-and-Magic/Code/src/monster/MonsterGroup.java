package monster;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Creator: Patrick
 * Created: 13.05.2019
 * Purpose:
 */
public class MonsterGroup implements Iterable<Monster> {
    public final String groupName;
    private final Monster minor;
    private final Monster normal;
    private final Monster major;
    public final List<Monster> ordering = new ArrayList<>();

    public MonsterGroup(Monster monster1, Monster monster2, Monster monster3, String groupName) {
        ordering.add(monster1);
        ordering.add(monster2);
        ordering.add(monster3);

        this.groupName = groupName.trim();
        minor = ordering.stream().filter(mon -> mon.getRanking() == Monster.Ranking.MINOR).findFirst().orElse(monster1);
        normal = ordering.stream().filter(mon -> mon.getRanking() == Monster.Ranking.NORMAL).findFirst().orElse(monster2);
        major = ordering.stream().filter(mon -> mon.getRanking() == Monster.Ranking.MAJOR).findFirst().orElse(monster3);

        getMonsters().forEach(mon -> {
            if (!mon.groupName().equals(this.groupName)) {
                String mismatch = mon.groupName() + " and " + this.groupName;
                throw new IllegalArgumentException("Group name does not match with monster: " + mismatch);
            }
        });
    }

    @NotNull
    @Override
    public Iterator<Monster> iterator() {
        return Arrays.asList(getMinor(), getNormal(), getMajor()).iterator();
    }

    @NotNull
    public List<Monster> getMonsters() {
        return Arrays.asList(getMinor(), getNormal(), getMajor());
    }

    /**
     * @param monsters
     * @return null if there are no monsters left to parse.
     * @throws IllegalArgumentException if the monsters cannot be summarized in a group.
     * @throws java.util.NoSuchElementException if there are insufficient monsters in the list.
     */
    public static MonsterGroup reduce(List<Monster> monsters) {
        if (monsters.isEmpty()) return null;

        Monster minor = monsters.remove(0);
        Monster normal = monsters.remove(0);
        Monster major = monsters.remove(0);

        // Assert that the monsters are grouped correctly.
        List<String> uniqueNames = Stream.of(minor, normal, major)
                .map(mon -> minor.Picture.substring(0, minor.Picture.length() - 2))
                .distinct()
                .collect(Collectors.toList());
        if (uniqueNames.size() != 1) {
            String names = String.join(", ", uniqueNames);

            throw new IllegalArgumentException("The following names do not match: " + names);
        }

        return new MonsterGroup(minor, normal, major, uniqueNames.get(0));
    }

    @Override
    public String toString() {
        return ordering.stream().map(Monster::toString).collect(Collectors.joining(MonsterTable.EOL));
    }

    public Monster getMinor() {
        return minor;
    }

    public Monster getNormal() {
        return normal;
    }

    public Monster getMajor() {
        return major;
    }
}
