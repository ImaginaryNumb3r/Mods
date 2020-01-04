package misc;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Creator: Patrick
 * Created: 09.05.2019
 * Purpose:
 */
public class DamageCalcTest {

    @Test
    public void testAverage() {
        checkAverage("3D6", 10.5);
        checkAverage("1D1", 1);
        checkAverage("1D6", 3.5);
        checkAverage("800D6", 3.5 * 800);
        checkAverage("48D21", 48 * ((21 + 1) / 2));
    }

    public void checkAverage(String str, double expected) {
        double average = DamageCalc.averageDamage(str);

        assertEquals(expected, average, Double.MIN_NORMAL);
    }

    @Test
    public void testToDiceDamage() {
        List<String> diceDamages = Arrays.asList("1D9", "20D8", "14D8", "5D5", "1D20+25");
        List<Double> averageDamages = diceDamages.stream()
                .map(DamageCalc::averageDamage)
                .collect(Collectors.toList());

        List<List<DiceDamage>> testArray = averageDamages.stream()
                .map(damage -> DamageCalc.toDiceDamage(damage, DiceDamage::new))
                .collect(Collectors.toList());

        var iterator = testArray.iterator();
        for (String expected : diceDamages) {
            var possibilities = iterator.next();
            DiceDamage expectedDD = DiceDamage.parse(expected);

            DiceDamage closest = DamageCalc.getClosest(possibilities, expectedDD);
            assertEquals(expectedDD, closest);
        }

        // All elements of iterator must have been iterated.
        assertFalse(iterator.hasNext());
    }
}