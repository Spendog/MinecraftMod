package com.example.educationmod.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.educationmod.EducationMod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KnowledgeGapTracker {
    private static KnowledgeGapTracker INSTANCE;
    private final List<MissedConcept> missedConcepts = new ArrayList<>();

    public static class MissedConcept {
        public String topic;
        public String question;
        public String userAnswer;
        public String correctAnswer;
        public long timestamp;

        public MissedConcept(String topic, String question, String userAnswer, String correctAnswer) {
            this.topic = topic;
            this.question = question;
            this.userAnswer = userAnswer;
            this.correctAnswer = correctAnswer;
            this.timestamp = System.currentTimeMillis();
        }
    }

    public static KnowledgeGapTracker getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new KnowledgeGapTracker();
        }
        return INSTANCE;
    }

    public void logGap(String topic, String question, String userAnswer, String correctAnswer) {
        missedConcepts.add(new MissedConcept(topic, question, userAnswer, correctAnswer));
        EducationMod.LOGGER.info("Knowledge Gap Logged: " + topic + " - " + question);
    }

    public List<MissedConcept> getGaps() {
        return new ArrayList<>(missedConcepts);
    }

    public void generateStudySet() {
        if (missedConcepts.isEmpty()) {
            EducationMod.LOGGER.info("No gaps to study.");
            return;
        }

        File studyFile = new File("config/mod_data/topics/study_set_" + System.currentTimeMillis() + ".json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(studyFile)) {
            // Create a simple structure for now. In a real scenario, this would match the
            // Topic JSON schema.
            writer.write(gson.toJson(missedConcepts));
            EducationMod.LOGGER.info("Study set generated: " + studyFile.getAbsolutePath());
        } catch (IOException e) {
            EducationMod.LOGGER.error("Failed to generate study set", e);
        }
    }
}
