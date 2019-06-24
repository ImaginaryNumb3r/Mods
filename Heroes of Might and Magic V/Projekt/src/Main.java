import io.CSVMapper;
import io.CreatureData;
import io.Zipper;
import model.Creature;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Creator: Patrick
 * Created: 27.05.2019
 * Purpose:
 */
public class Main {

    public static void main(String[] args) {
        Path creaturePath = Path.of("Creatures.csv");

        try {
            List<Creature> creatures = CreatureData.loadCreatures();
            CSVMapper csv = new CSVMapper(creaturePath);
            csv.append(creatures);

        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        new Zipper();
    }
}
