package monster;

import misc.DiceDamage;
import misc.DiceTreasure;
import misc.MonsterFile;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

/**
 * Creator: Patrick
 * Created: 12.05.2019
 * Purpose:
 */
public class MonsterPlotter {

    public static void main(String[] args) throws IOException {
        plot(mon -> (double) mon.EXP); /*
        plotAverageTreasure();

        var HPList = MonsterData.getNewHPList();
        int i = 1;
        for (int hp : HPList) {
            System.out.println(i + "\t" + hp);
            ++i;
        } */
    }

    public static void plotAverageTreasure() throws IOException {
        MonsterTable table = MonsterTable.parse(MonsterData.ORIGINAL_FILE);
        var dataPoints = new ArrayList<DataPoint>();
        table.forEachMonster(mon -> {
            var treasure = new ArrayDeque<>(Arrays.asList(mon.Treasure.split("%"))).getLast();
            double average = DiceTreasure.parse(treasure).average();

            dataPoints.add(new DataPoint(mon.LVL, (int) average));
        });

        scatterPlot(dataPoints);
    }

    public static void plot(Function<Monster, Double> plotFunction) throws IOException {
        MonsterTable table = MonsterTable.parse(MonsterData.ORIGINAL_FILE);
        var dataPoints = new ArrayList<DataPoint>();
        table.forEachMonster(mon -> {
            double data = plotFunction.apply(mon);

            dataPoints.add(new DataPoint(mon.LVL, (int) data));
        });

        scatterPlot(dataPoints);
    }

    public static void plotGeneratedHP() throws IOException {
        int sequence = 0;
        int sequenceLength = 4;
        int hpAdd = 4;
        int lastHP = 9;

        var dataPoints = new ArrayList<>(asList(new DataPoint(1, 3),
                new DataPoint(2, 9), new DataPoint(3, 9))
        );
        for (int level = 4; level <= 100; ++level) {
            lastHP += hpAdd;
            dataPoints.add(new DataPoint(level, lastHP));

            ++sequence;
            if (sequence == sequenceLength) {
                sequenceLength = sequenceLength == 4 ? 6 : 4;
                ++hpAdd;
                sequence = 0;
            }
        }
        dataPoints.forEach(point -> System.out.println(point.LVL + "\t" + point.HP));
    }

    public static void plotDistinctHP() throws IOException {
        var points = new ArrayList<DataPoint>();

        var path = Path.of("monsters.txt");
        MonsterFile.mutate(path, mon -> {
            points.add(new DataPoint(mon.LVL, mon.HP));

            return mon;
        });

        var levelSet = new HashSet<Integer>();
        points.removeIf(point -> {
            return !levelSet.add(point.LVL);
        });
        points.sort(Comparator.comparing(point -> point.LVL));
        points.forEach(point -> System.out.println(point.LVL + "\t" + point.HP));

        scatterPlot(points);
    }

    public static void plotMonsterHP() throws IOException {
        var xLevels = new ArrayList<Integer>();
        var yData = new ArrayList<Integer>();

        var path = Path.of("monsters.txt");
        MonsterFile.mutate(path, mon -> {
            xLevels.add(mon.LVL);
            yData.add(mon.HP);

            return mon;
        });

        scatterPlot(xLevels, yData);
    }

    public static void scatterPlot(List<DataPoint> points) throws IOException {
        var xLvl  = points.stream().map(point -> point.LVL).collect(Collectors.toList());
        var yData = points.stream().map(point -> point.HP).collect(Collectors.toList());

        scatterPlot(xLvl, yData);
    }

    public static void scatterPlot(List<Integer> xLevels, List<Integer> yData) throws IOException {
        XYChart chart = new XYChartBuilder().width(800).height(600).build();

        // Customize Chart
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setChartTitleVisible(false);
        chart.getStyler().setMarkerSize(8);

        // Series

        chart.addSeries("Gaussian Blob", xLevels, yData);

        new SwingWrapper<>(chart).displayChart();
    }

    private static class DataPoint{
        int LVL;
        int HP;

        public DataPoint(int LVL, int HP) {
            this.LVL = LVL;
            this.HP = HP;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DataPoint dataPoint = (DataPoint) o;
            return LVL == dataPoint.LVL;
        }

        @Override
        public int hashCode() {
            return Objects.hash(LVL);
        }

        @Override
        public String toString() {
            return "DataPoint{" +
                    "LVL=" + LVL +
                    ", HP=" + HP +
                    '}';
        }
    }

}
