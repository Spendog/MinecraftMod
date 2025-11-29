package com.example.educationmod;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ActivityLogger {
    private static final Path LOG_FILE = FabricLoader.getInstance().getConfigDir()
            .resolve("educationmod/activity_log.txt");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String message) {
        writeEntry("[INFO] " + message);
    }

    public static void logError(String message, Exception e) {
        writeEntry("[ERROR] " + message + ": " + (e != null ? e.getMessage() : "Unknown error"));
        if (e != null) {
            e.printStackTrace(); // Still print to console for debugging
        }
    }

    public static void logInteraction(String interactionType) {
        writeEntry("[INTERACTION] " + interactionType);
    }

    private static void writeEntry(String entry) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String line = String.format("[%s] %s%n", timestamp, entry);

        try {
            if (!Files.exists(LOG_FILE.getParent())) {
                Files.createDirectories(LOG_FILE.getParent());
            }
            Files.write(LOG_FILE, line.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Failed to write to activity log: " + e.getMessage());
        }
    }

    public static Path getLogFilePath() {
        return LOG_FILE;
    }
}
