package com.example.educationmod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlayerStats {
    private static final File STATS_FILE = FabricLoader.getInstance().getConfigDir()
            .resolve("mod_data/player_stats.json").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static PlayerStats INSTANCE;

    // Map of Topic ID -> Confidence Score (0.0 to 1.0)
    public Map<String, Double> topicConfidence = new HashMap<>();
    // Map of Topic ID -> Current Streak
    public Map<String, Integer> topicStreaks = new HashMap<>();
    // Legacy fields (still used for weakness calculation)
    public Map<String, Integer> topicWeakness = new HashMap<>();
    public Map<String, Integer> quizzesTaken = new HashMap<>();

    public static PlayerStats getInstance() {
        if (INSTANCE == null) {
            load();
        }
        return INSTANCE;
    }

    public static void load() {
        if (STATS_FILE.exists()) {
            try (FileReader reader = new FileReader(STATS_FILE)) {
                INSTANCE = GSON.fromJson(reader, PlayerStats.class);
                if (INSTANCE.topicConfidence == null)
                    INSTANCE.topicConfidence = new HashMap<>();
                if (INSTANCE.topicStreaks == null)
                    INSTANCE.topicStreaks = new HashMap<>();
                if (INSTANCE.topicWeakness == null)
                    INSTANCE.topicWeakness = new HashMap<>();
                if (INSTANCE.quizzesTaken == null)
                    INSTANCE.quizzesTaken = new HashMap<>();
            } catch (IOException e) {
                e.printStackTrace();
                INSTANCE = new PlayerStats();
            }
        } else {
            INSTANCE = new PlayerStats();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(STATS_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recordResult(String topicId, int score, int totalQuestions) {
        quizzesTaken.put(topicId, quizzesTaken.getOrDefault(topicId, 0) + 1);

        // Weakness Calculation (Legacy but useful)
        int wrongAnswers = totalQuestions - score;
        int currentWeakness = topicWeakness.getOrDefault(topicId, 0);
        int newWeakness = Math.max(0, currentWeakness + wrongAnswers - score);
        topicWeakness.put(topicId, newWeakness);

        // Confidence Calculation (Moving Average)
        double currentConfidence = topicConfidence.getOrDefault(topicId, 0.5); // Default 50%
        double quizPerformance = (double) score / totalQuestions;
        // New confidence is weighted 70% old, 30% new
        double newConfidence = (currentConfidence * 0.7) + (quizPerformance * 0.3);
        topicConfidence.put(topicId, newConfidence);

        // Streak Calculation
        int currentStreak = topicStreaks.getOrDefault(topicId, 0);
        if (score == totalQuestions) {
            topicStreaks.put(topicId, currentStreak + 1);
        } else {
            topicStreaks.put(topicId, 0); // Reset streak on any mistake
        }

        save();
    }

    public String getWeakestTopic() {
        String weakest = null;
        int maxWeakness = -1;

        for (Map.Entry<String, Integer> entry : topicWeakness.entrySet()) {
            if (entry.getValue() > maxWeakness) {
                maxWeakness = entry.getValue();
                weakest = entry.getKey();
            }
        }
        return weakest;
    }
}
