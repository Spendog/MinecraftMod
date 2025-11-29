package com.example.educationmod.gui;

import com.example.educationmod.ModConfigManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import com.example.educationmod.PlayerStats;
import com.example.educationmod.LearningLogger;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizScreen extends Screen {
    private final String topicFile;
    private ModConfigManager.TopicDefinition topic;
    private int currentQuestionIndex = 0;
    private int score = 0;

    public QuizScreen(String topicFile) {
        super(Text.literal("Quiz Time!"));
        this.topicFile = topicFile;
    }

    public QuizScreen(ModConfigManager.TopicDefinition topic) {
        super(Text.literal("Quiz Time!"));
        this.topic = topic;
        this.topicFile = null;
    }

    @Override
    protected void init() {
        if (topic == null && topicFile != null) {
            topic = ModConfigManager.loadTopic(topicFile);
        }

        if (topic == null || topic.questions == null || topic.questions.isEmpty()) {
            this.close(); // Nothing to show
            return;
        }

        if (currentQuestionIndex == 0) {
            com.example.educationmod.ActivityLogger
                    .logInteraction("Started quiz: " + (topic.id != null ? topic.id : "Unknown"));
        }

        setupQuestion();
    }

    private void setupQuestion() {
        this.clearChildren();

        // Add Close Button (Top Right)
        this.addDrawableChild(ButtonWidget.builder(Text.literal("X"), button -> {
            this.close();
        }).dimensions(this.width - 30, 10, 20, 20).build());

        if (currentQuestionIndex >= topic.questions.size()) {
            // Quiz finished
            // Record stats
            if (topicFile != null) {
                String topicId = new java.io.File(topicFile).getName().replace(".json", "");
                PlayerStats.getInstance().recordResult(topicId, score, topic.questions.size());
            } else if (topic != null && topic.id != null) {
                PlayerStats.getInstance().recordResult(topic.id, score, topic.questions.size());
            }

            com.example.educationmod.ActivityLogger.logInteraction("Finished quiz: "
                    + (topic.id != null ? topic.id : "Unknown") + " Score: " + score + "/" + topic.questions.size());

            this.addDrawableChild(ButtonWidget
                    .builder(Text.literal("Finish (Score: " + score + "/" + topic.questions.size() + ")"),
                            button -> this.close())
                    .dimensions(this.width / 2 - 100, this.height / 2, 200, 20).build());
            return;
        }

        ModConfigManager.QuestionDefinition q = topic.questions.get(currentQuestionIndex);

        // Log Question View
        LearningLogger.log("QUESTION_VIEW", q.question);

        // Flag Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Flag"), button -> {
            LearningLogger.log("QUESTION_FLAGGED", q.question);

            if (q.flags == null)
                q.flags = new ArrayList<>();
            if (!q.flags.contains("NEEDS_REVIEW")) {
                q.flags.add("NEEDS_REVIEW");
                // Save the topic file to persist the flag
                if (topicFile != null) {
                    saveTopic(topicFile, topic);
                }
            }

            button.setMessage(Text.literal("Flagged!"));
            button.active = false;
        }).dimensions(this.width - 80, 10, 40, 20).build());

        List<String> answers = new ArrayList<>(q.incorrect_answers);
        answers.add(q.correct_answer);
        Collections.shuffle(answers);

        int y = this.height / 2;
        for (String ans : answers) {
            this.addDrawableChild(ButtonWidget.builder(Text.literal(ans), button -> {
                boolean correct = ans.equals(q.correct_answer);
                LearningLogger.log("ANSWER_SUBMITTED", new AnswerLog(q.question, ans, correct));

                if (correct) {
                    score++;
                }
                currentQuestionIndex++;
                setupQuestion();
            }).dimensions(this.width / 2 - 100, y, 200, 20).build());
            y += 25;
        }
    }

    private static class AnswerLog {
        String question;
        String answer;
        boolean correct;

        public AnswerLog(String q, String a, boolean c) {
            this.question = q;
            this.answer = a;
            this.correct = c;
        }
    }

    private void saveTopic(String filename, ModConfigManager.TopicDefinition topic) {
        try (java.io.FileWriter writer = new java.io.FileWriter(new java.io.File(filename))) {
            new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(topic, writer);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Gradient Background (Dark Blue to Purple)
        context.fillGradient(0, 0, this.width, this.height, 0xFF100010, 0xFF300030);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);

        if (topic != null && currentQuestionIndex < topic.questions.size()) {
            ModConfigManager.QuestionDefinition q = topic.questions.get(currentQuestionIndex);
            context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(q.question), this.width / 2, 50,
                    0xFFFFFF);
            context.drawCenteredTextWithShadow(this.textRenderer,
                    Text.literal("Question " + (currentQuestionIndex + 1) + "/" + topic.questions.size()),
                    this.width / 2, 35, 0xAAAAAA);
        } else if (topic != null) {
            context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Quiz Complete!"), this.width / 2, 50,
                    0x00FF00);
        } else {
            context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Failed to load topic: " + topicFile),
                    this.width / 2, 50, 0xFF0000);
        }

        super.render(context, mouseX, mouseY, delta);
    }
}
