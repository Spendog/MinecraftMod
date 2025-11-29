package com.example.educationmod.gui;

import com.example.educationmod.PlayerStats;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

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
            PlayerStats stats = PlayerStats.getInstance();
            StringBuilder sb = new StringBuilder();
            sb.append("=== Education Mod Data ===\n");

            for (Map.Entry<String, Double> entry : stats.topicConfidence.entrySet()) {
                String topic = entry.getKey();
                // Strip path if present
                if (topic.contains("/") || topic.contains("\\")) {
                    topic = topic.substring(Math.max(topic.lastIndexOf('/'), topic.lastIndexOf('\\')) + 1);
                }

                double confidence = entry.getValue();
                int layerHeight = (int) (confidence * 10);
                sb.append(topic).append(": ").append(layerHeight).append(" layers\n");
            }

            int totalQuizzes = stats.quizzesTaken.values().stream().mapToInt(Integer::intValue).sum();
            sb.append("Total Quizzes: ").append(totalQuizzes).append("\n");

            this.client.keyboard.setClipboard(sb.toString());
            button.setMessage(Text.literal("Copied!"));
        }).dimensions(center - 100, this.height - 70, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Solid Background to prevent "blur" issues
        // this.renderBackground(context, mouseX, mouseY, delta); // Removed to prevent
        // blur
        context.fill(0, 0, this.width, this.height, 0xFF101010); // Solid dark gray/black

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);

        PlayerStats stats = PlayerStats.getInstance();
        int leftColumnX = 40;
        int rightColumnX = this.width / 2 + 20;
        int y = 60;

        // --- Left Column: Topic Mastery ---
        context.drawText(this.textRenderer, "§6Topic Mastery:", leftColumnX, y, 0xFFFFFF, true);
        y += 20;

        for (Map.Entry<String, Double> entry : stats.topicConfidence.entrySet()) {
            String topic = entry.getKey();
            // Strip path if present
            if (topic.contains("/") || topic.contains("\\")) {
                topic = topic.substring(Math.max(topic.lastIndexOf('/'), topic.lastIndexOf('\\')) + 1);
            }

            double confidence = entry.getValue();

            // Draw Topic Name
            context.drawText(this.textRenderer, topic, leftColumnX, y, 0xFFFFFF, true);

            // Draw Progress Bar Background
            int barX = leftColumnX + 100;
            int barWidth = 100;
            int barHeight = 8;
            context.fill(barX, y, barX + barWidth, y + barHeight, 0xFF333333);

            // Draw Progress Bar Fill
            int fillWidth = (int) (barWidth * confidence);
            int color = confidence > 0.8 ? 0xFF55FF55 : (confidence > 0.5 ? 0xFFFFFF55 : 0xFFFF5555);
            context.fill(barX, y, barX + fillWidth, y + barHeight, color);

            // Draw Percentage
            String percent = (int) (confidence * 100) + "%";
            context.drawText(this.textRenderer, percent, barX + barWidth + 5, y, 0xAAAAAA, true);

            y += 20;
        }

        // --- Right Column: Insights ---
        y = 60;
        context.drawText(this.textRenderer, "§6Insights:", rightColumnX, y, 0xFFFFFF, true);
        y += 20;

        // Weakest Topic
        String weakest = stats.getWeakestTopic();
        context.drawText(this.textRenderer, "§7Focus Area:", rightColumnX, y, 0xAAAAAA, true);
        context.drawText(this.textRenderer, weakest != null ? "§c" + weakest : "§aNone!", rightColumnX, y + 10,
                0xFFFFFF, true);
        y += 30;

        // Total Quizzes
        int totalQuizzes = stats.quizzesTaken.values().stream().mapToInt(Integer::intValue).sum();
        context.drawText(this.textRenderer, "§7Total Quizzes:", rightColumnX, y, 0xAAAAAA, true);
        context.drawText(this.textRenderer, "§b" + totalQuizzes, rightColumnX, y + 10, 0xFFFFFF, true);
        y += 30;

        // Highest Streak
        int maxStreak = 0;
        for (int streak : stats.topicStreaks.values()) {
            if (streak > maxStreak)
                maxStreak = streak;
        }
        context.drawText(this.textRenderer, "§7Best Streak:", rightColumnX, y, 0xAAAAAA, true);
        context.drawText(this.textRenderer, "§e⚡ " + maxStreak, rightColumnX, y + 10, 0xFFFFFF, true);

        super.render(context, mouseX, mouseY, delta);
    }
}
