package io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Creator: Patrick
 * Created: 27.05.2019
 * Purpose:
 */
public class Zipper {
    public static Path SOURCE_DIR = Path.of("GameMechanics");
    public static Path TARGET_DIR = Path.of("..", "UserMODs");
    public static Path TARGET_FILE = TARGET_DIR.resolve("NBE.zip");

    public void zipMod() {
        try (var out = new ZipOutputStream(new FileOutputStream(TARGET_FILE.toFile()))) {
            Files.delete(TARGET_FILE);

            Files.walkFileTree(SOURCE_DIR, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    var entry = new ZipEntry(dir.toString());

                    out.putNextEntry(entry);
                    // out.write();

                    return super.preVisitDirectory(dir, attrs);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
