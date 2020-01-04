package misc;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Creator: Patrick
 * Created: 09.05.2019
 * Purpose:
 */
public class DamageCalc {

    public static double attackBonus(String str) {
        String[] split = str.split("[+]");
        return split.length == 2
                ? Double.parseDouble(split[1])
                : 0;
    }
    public static double averageDamage(String str) {
        str = str.split("[+]")[0];

        String[] parts = str.toLowerCase().split("d");
        int dice = Integer.valueOf(parts[0]);
        int faces = Integer.valueOf(parts[1]);

        return averageDamage(dice, faces);
    }

    public static double averageDamage(DiceThrow diceThrow) {
        return averageDamage(diceThrow.dice, diceThrow.faces);
    }

    public static double averageDamage(int dice, double faces) {
        double averageDamage = (faces + 1) / 2;

        return dice * averageDamage;
    }

    public static DiceDamage scaleToClosest(double scalar, DiceDamage original) {
        double damage = original.average() * scalar;

        List<DiceDamage> possibilities = toDiceDamage(damage - 0.5, DiceDamage::new);
        DiceDamage closest = getClosest(possibilities, original);
        closest.scaleExtra(scalar, 0);

        return closest;
    }

    public static DiceTreasure scaleToClosestRange(double scalar, DiceTreasure original) {
        double damage = original.average() * scalar;

        Set<DiceTreasure> possibilities = new HashSet<>(toDiceDamage(damage - 0.5, DiceTreasure::new));
        possibilities.addAll(toDiceDamage(damage, DiceTreasure::new));
        possibilities.addAll(toDiceDamage(damage + 0.5, DiceTreasure::new));

        DiceTreasure closest = getClosest(new ArrayList<>(possibilities), original);
        closest.scaleExtra(scalar, 0);

        return closest;
    }

    public static <T extends DiceThrow> List<T> toDiceDamage(final double averageValue, BiFunction<Integer, Integer, T> constructor) {
        double rounded = roundToNearest(averageValue);
        int dividerCount = (int) (rounded / 2);
        List<Integer> dice = new ArrayList<>();
        var dicePossibilities = new ArrayList<T>();

        for (int i = 1; i < rounded + 1; ++i) {
            double val = rounded / i;
            double resudial = val % 0.5;

            if (resudial == 0 || resudial == 0.5) {
                dice.add(i);
            }
        }

        var faces = dice.stream()
                .map(dieCount -> {
                    return averageValue / dieCount;
                }).collect(Collectors.toList());

        // int faces = dice[0];
        var diceIterator = dice.iterator();
        var doubleIterator = faces.iterator();
        while (diceIterator.hasNext()) {
            int die = diceIterator.next();
            int face = (int) (doubleIterator.next() * 2) - 1;

            dicePossibilities.add(constructor.apply(die, face));
        }

        if (doubleIterator.hasNext()) throw new IllegalStateException();

        List<T> filtered = dicePossibilities.stream()
                .filter(dd -> dd.faces > 1)
                .collect(Collectors.toList());
        /*
        List<DiceDamage> secondFilter = firstFilter.stream().filter(dd -> dd.dice < monsterLevel).collect(Collectors.toList());

        List<DiceDamage> filter = secondFilter;
        if (secondFilter.isEmpty()) {
            filter = firstFilter;
        } else {
            var tempFilter = secondFilter.stream().filter(dd -> dd.faces < monsterLevel).collect(Collectors.toList());
            if (!tempFilter.isEmpty()) {
                filter = tempFilter;
            }
        }*/

        return filtered;
    }

    public static <T extends DiceThrow<?>> T getClosest(List<T> diceDamages, T original) {
        ArrayList<DiceThrow.Dist> dists = new ArrayList<>();

        for (DiceThrow<?> diceDamage : diceDamages) {
            dists.add(original.distance(diceDamage));
        }

        int minIndex = 0;
        int min = dists.get(0).dist();
        int i = 0;
        for (DiceThrow.Dist dist : dists) {
            int distance = dist.dist();
            if (distance < min) {
                min = distance;
                minIndex = i;
            }

            ++i;
        }

        return diceDamages.get(minIndex);
    }

    public static double roundToNearest(double value) {
        double[] rounded = new double[3];
        double[] distances = new double[3];

        double round = Math.round(value);
        rounded[0] = round;
        rounded[1]  = round - 0.5;
        rounded[2]  = round + 0.5;

        distances[0] = value - rounded[0];
        distances[1] = value - rounded[1];
        distances[2] = value - rounded[2];

        int i = 0;
        int minIndex = 0;
        double min = distances[0];
        for (double distance : distances) {
            if (Math.abs(distance) < min) {
                minIndex = i;
            }

            ++i;
        }

        return rounded[minIndex];
    }

}
