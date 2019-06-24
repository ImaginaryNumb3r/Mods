package io;

import model.Creature;
import model.Faction;
import parsing.xml.XMLDocument;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.nio.file.Files.isDirectory;

/**
 * Creator: Patrick
 * Created: 28.05.2019
 * Purpose:
 */
public class CreatureData {
    public static final Path CREATURE_DIR = Path.of("GameMechanics", "Creature", "Creatures");

    public static List<Creature> loadCreatures() throws IOException, ExecutionException, InterruptedException {
        var allCreatures = new ArrayList<Creature>();
        var factionDirs = Files.newDirectoryStream(CREATURE_DIR);

        ExecutorService executor = Executors.newFixedThreadPool(Faction.count());

        var futures = new ArrayList<Future<ArrayList<Creature>>>();
        for (Path factionDir : factionDirs) {

            // Submit to executor.
            var future = executor.submit(() -> {
                var creatures = new ArrayList<Creature>();
                DirectoryStream<Path> creaturesPath;

                try {
                    if (isDirectory(factionDir)) {
                        creaturesPath = Files.newDirectoryStream(factionDir);
                        Faction faction = Faction.valueOf(factionDir.getFileName().toString());

                        for (Path factionEntry : creaturesPath) {
                            creatures.addAll(readCreatureDirectory(factionEntry, faction));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return creatures;
            });

            futures.add(future);
        }

        for (var future : futures) {
            allCreatures.addAll(future.get());
        }

        executor.shutdown();

        return allCreatures;
    }

    private static List<Creature> readCreatureDirectory(Path entry, Faction faction) throws IOException {
        var creatures = new ArrayList<Creature>();

        if (Files.isDirectory(entry)) {
            for (Path creaturesDir : Files.newDirectoryStream(entry)) {
                creatures.addAll(readCreatureDirectory(creaturesDir, faction));
            }
        }
        else if (Files.isRegularFile(entry)){
            String fileName = entry.getFileName().toString();
            String creatureName = fileName.split("[.]")[0];

            String creatureStr = Files.readString(entry);
            var creatureXML = XMLDocument.of(creatureStr);

            // TODO: Check whether this really is a real creature
            Creature creature = Creature.parse(creatureName, creatureXML, faction);
            if (creature != null) {
                creatures.add(creature);
            }

        } else {
            throw new IllegalStateException("Entry is neither file nor directory! At: " + entry.toString());
        }

        return creatures;
    }


    private static class LoadFactionTask {

    }

}
