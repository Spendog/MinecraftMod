package com.example.educationmod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LearningLogger {
    private static final File LOG_FILE = FabricLoader.getInstance().getConfigDir()
            .resolve("mod_data/learning_history.json").toFile();
    private static final Gson GSON = new GsonBuilder().create();

    public static void log(String eventType, Object data) {
        LogEntry entry = new LogEntry();
        entry.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        entry.eventType = eventType;
        entry.data = data;

        String json = GSON.toJson(entry);

        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class LogEntry {
        public String timestamp;
        public String eventType;
        public Object data;
    }
}
