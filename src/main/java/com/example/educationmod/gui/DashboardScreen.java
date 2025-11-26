package com.example.educationmod.gui;

import com.example.educationmod.ModConfigManager;
import com.example.educationmod.PlayerStats;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.io.File;
import java.util.Map;

public class DashboardScreen extends Screen {
    private final Screen parent;

    public DashboardScreen(Screen parent) {
        super(Text.literal("Learning Dashboard"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int center = this.width / 2;

        // Close Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("X"), button -> {
            this.close();
        }).dimensions(this.width - 30, 10, 20, 20).build());

        // Back Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), button -> this.client.setScreen(parent))
                .dimensions(center - 100, this.height - 40, 200, 20).build());

        // Export Data Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Copy Data to Clipboard"), button -> {
            String weakTopic = PlayerStats.getInstance().getWeakestTopic();
            StringBuilder sb = new StringBuilder();
            sb.append("=== Education Mod Data ===\n");
            sb.append("Weakest Topic: ").append(weakTopic).append("\n");
            if (weakTopic != null) {
                sb.append("Confidence: ").append(PlayerStats.getInstance().topicConfidence.getOrDefault(weakTopic, 0.0))
                        .append("\n");
                sb.append("Streak: ").append(PlayerStats.getInstance().topicStreaks.getOrDefault(weakTopic, 0))
                        .append("\n");
            }
            sb.append("Total Quizzes: ")
                    .append(PlayerStats.getInstance().quizzesTaken.values().stream().mapToInt(Integer::intValue).sum())
                    .append("\n");

            this.client.keyboard.setClipboard(sb.toString());
            button.setMessage(Text.literal("Copied!"));
        }).dimensions(center - 100, this.height - 70, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Gradient Background
        context.fillGradient(0, 0, this.width, this.height, 0xFF001020, 0xFF002040);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);

        int y = 60;
        int center = this.width / 2;

        // Weakest Topic
        String weakTopic = PlayerStats.getInstance().getWeakestTopic();
        context.drawTextWithShadow(this.textRenderer, "Weakest Topic:", center - 150, y, 0xFFAAAA);
        if (weakTopic != null) {
            context.drawTextWithShadow(this.textRenderer, weakTopic, center + 50, y, 0xFF5555);
        } else {
            context.drawTextWithShadow(this.textRenderer, "None (Great job!)", center + 50, y, 0x55FF55);
        }
        y += 30;

        // Total Quizzes Taken
        int totalQuizzes = PlayerStats.getInstance().quizzesTaken.values().stream().mapToInt(Integer::intValue).sum();
        context.drawTextWithShadow(this.textRenderer, "Total Quizzes:", center - 150, y, 0xAAAAAA);
        context.drawTextWithShadow(this.textRenderer, String.valueOf(totalQuizzes), center + 50, y, 0xFFFFFF);
        y += 30;

        // Recommendation
        context.drawTextWithShadow(this.textRenderer, "Recommended:", center - 150, y, 0xAAFFAA);
        if (weakTopic != null) {
            context.drawTextWithShadow(this.textRenderer, "Review " + weakTopic, center + 50, y, 0xFFFF55);
        } else {
            context.drawTextWithShadow(this.textRenderer, "Explore new topics!", center + 50, y, 0x55FFFF);
        }
        y += 40;

        // --- Analysis View (Variables) ---
        context.drawCenteredTextWithShadow(this.textRenderer, "--- Analysis View ---", center, y, 0x888888);
        y += 20;

        if (weakTopic != null) {
            double confidence = PlayerStats.getInstance().topicConfidence.getOrDefault(weakTopic, 0.0) * 100;
            int streak = PlayerStats.getInstance().topicStreaks.getOrDefault(weakTopic, 0);

            context.drawTextWithShadow(this.textRenderer, "Topic:", center - 150, y, 0xAAAAAA);
            context.drawTextWithShadow(this.textRenderer, weakTopic, center + 50, y, 0xFFFFFF);
            y += 15;

            context.drawTextWithShadow(this.textRenderer, "Confidence:", center - 150, y, 0xAAAAAA);
            context.drawTextWithShadow(this.textRenderer, String.format("%.1f%%", confidence), center + 50, y,
                    confidence > 80 ? 0x55FF55 : 0xFF5555);
            y += 15;

            context.drawTextWithShadow(this.textRenderer, "Streak:", center - 150, y, 0xAAAAAA);
            context.drawTextWithShadow(this.textRenderer, String.valueOf(streak), center + 50, y,
                    streak > 3 ? 0x55FFFF : 0xFFFFFF);
            y += 25;

            // --- Simple Bar Chart (Confidence) ---
            int barWidth = 200;
            int barHeight = 10;
            int filledWidth = (int) (barWidth * (confidence / 100.0));

            context.fill(center - 100, y, center - 100 + barWidth, y + barHeight, 0xFF444444); // Background
            context.fill(center - 100, y, center - 100 + filledWidth, y + barHeight,
                    confidence > 80 ? 0xFF55FF55 : 0xFFFF5555); // Bar
            y += 20;

        } else {
            context.drawCenteredTextWithShadow(this.textRenderer, "No data available yet.", center, y, 0xAAAAAA);
            y += 40;
        }

        super.render(context, mouseX, mouseY, delta);
    }
}
