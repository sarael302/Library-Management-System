package utils;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedWriter;

public class FileHandler {
    public static void createFileIfNotExists(String fileName, String header) throws IOException {
        Path filePath = Paths.get(fileName);
        if (Files.notExists(filePath)) {
            Files.createFile(filePath);
            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                writer.write(header);
                writer.newLine();
            }
        }
    }
}
