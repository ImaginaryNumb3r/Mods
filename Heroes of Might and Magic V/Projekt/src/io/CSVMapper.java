package io;

import model.Creature;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Creator: Patrick
 * Created: 29.05.2019
 * Purpose:
 */
public class CSVMapper {
    private final Path _target;

    public CSVMapper(Path target) {
        _target = target;
    }

    public void append(List<Creature> creatures) {
        try { Files.delete(_target); }
        catch (IOException e) {
            e.printStackTrace();
        }

        try (var writer = new BufferedWriter(new FileWriter(_target.toFile()))) {
            var header = Creature.getFieldNames();

            var printer = new CSVPrinter(writer, CSVFormat.EXCEL.withHeader(header));
            for (Creature creature : creatures) {
                Object[] values = creature.getValues();
                printer.printRecord(values);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
