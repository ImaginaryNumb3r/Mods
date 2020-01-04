package spell;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.range;

/**
 * spell.Spell Criteria:
 *  - Fast / Normal / Slow recovery
 *  - High / Medium / Low Damage
 *  - Resourceful / Reasonable / Wasteful
 *
 * Fast: 90 80 70 60
 * Normal: 120 110 100 90
 * Slow: 150 140 130 120
 *
 *  Fire: Fast Recovery, High Damage, Wasteful
 *  Air: Fast Recovery, Medium Damage, Reasonable
 *  Water: Normal Recovery, Medium Damage, Resourceful
 *  Earth: Slow Recovery, High Damage, Reasonable
 *
 *  Mind: Slow Recovery, High Damage, Reasonable
 *  Body: Fast Recovery, Normal Damage, Wasteful
 *  Spirit: Normal Recovery, Normal Damage , Reasonable
 *
 *  Light: Fast Recovery, Medium Damage, Reasonable
 *  Dark: Normal Recovery, High Damage, Wasteful
 *
 *  TODO: Try to give mass fear a damage effect
 */

/**
 * Creator: Patrick
 * Created: 01.05.2019
 * Purpose:
 */
public class SpellExecutor {
    private static final int MIN_RANK = 4;
    private static final int MAX_RANK = 16;
    // private static final int RANKS = MAX_RANK - MIN_RANK + 1;

    private static void plot(List<Spell> damageSpells, String label, BiFunction<Spell, Integer, Double> reducer) {
        double[] xData = range(MIN_RANK, MAX_RANK + 1).asDoubleStream().toArray();
        double[][] yData = new double[damageSpells.size()][xData.length];
        String[] names = damageSpells.stream().map(spell -> spell.SPELL_NAME).toArray(String[]::new);

        int i = 0;
        for (Spell spell : damageSpells) {
            double[] data = yData[i];

            for (int j = MIN_RANK; j <= MAX_RANK; ++j) {
                Double damage = reducer.apply(spell, j);
                data[j - MIN_RANK] = damage;
                /*
                if (efficiency) {
                    double damage = spell.damage(j) / (double) spell.spellCost(j);
                    data[j - MIN_RANK] = damage;
                } else {
                    double damage = spell.damage(j);
                    data[j - MIN_RANK] = damage;
                } */
            }

            ++i;
        }
        XYChart chart = QuickChart.getChart("spell.Spell Chart", "Levels", label, names, xData, yData);

        // Show it
        new SwingWrapper(chart).displayChart();
    }

    public static void main(String[] args) throws IOException {
        List<Spell> spells = getSpells();

        var damageSpells = spells.stream()
                .filter(SpellExecutor::isDamageSpell)
                .collect(Collectors.toList());
        /* damageSpells = damageSpells.stream()
                .filter(spell -> spell.MASTERY.ordinal() >= spell.Mastery.MASTER.ordinal())
                .collect(Collectors.toList()); */

        damageSpells = damageSpells.stream().filter(spell ->
                !Arrays.asList("Meteor Shower", "Starburst", "Shrapmetal").contains(spell.SPELL_NAME)
        ).collect(Collectors.toList());

        // Efficiency:
        plot(damageSpells, "Efficiency" , (spell, level) -> spell.damage(level) / (double) spell.spellCost(level) );

        // Damage
        plot(damageSpells, "Damage", (spell, level) -> (double) spell.damage(level) );

        // DPS
        plot(damageSpells, "DPS", (spell, level) -> spell.damage(level) / (double) spell.recoveryTime(level) );
        // plot(damageSpells, false);
    }

    private static List<Spell> getSpells() throws IOException {
        List<String> lines = Files.readAllLines(SpellData.SPELL_FILE);
        lines.remove(0);
        lines.remove(0);

        List<Spell> spells = lines.stream().map(Spell::parse).collect(Collectors.toList());

        int i = 0;
        int j = 0;
        for (Spell spell : spells) {

            if (i == 11) {
                i = 0;
                ++j;
            }
            spell.MASTERY = Mastery.parseLevel(i);
            spell.SCHOOL = School.values()[j];
            ++i;
        }

        return spells;
    }

    private static boolean isDamageSpell(Spell spell) {
        return (spell.DAMAGE_ADD != 0 || spell.DAMAGE_DICE != 0);
    }
}
