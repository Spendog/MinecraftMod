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
        // Gradient Background
        context.fillGradient(0, 0, this.width, this.height, 0xFF001020, 0xFF002040);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);

        PlayerStats stats = PlayerStats.getInstance();
        int y = 60;

        // Layer Stack Visualization
        context.drawText(this.textRenderer, "§6Learning Progress (Layer Stacks):", 20, y, 0xFFFFFF, true);
        y += 20;

        // Show layer stacks for each topic
        for (Map.Entry<String, Double> entry : stats.topicConfidence.entrySet()) {
            String topic = entry.getKey();
            double confidence = entry.getValue();
            int layerHeight = (int) (confidence * 10);

            // Visual stack representation
            String layers = "█".repeat(Math.max(1, layerHeight));
            String layerText = "§f" + topic + ": §e" + layers + " §7(" + layerHeight + " layers)";
            context.drawText(this.textRenderer, layerText, 30, y, 0xFFFFFF, true);
            y += 15;
        }

        // Process Improvement Section
        y += 10;
        context.drawText(this.textRenderer, "§6Process Improvement:", 20, y, 0xFFFFFF, true);
        y += 20;

        // Placeholder for future tracking
        String improvement = "§7Learning Method: §aBuilding Foundations";
        context.drawText(this.textRenderer, improvement, 30, y, 0xFFFFFF, true);
        y += 15;

        String guessRate = "§7Informed Choices: §aImproving";
        context.drawText(this.textRenderer, guessRate, 30, y, 0xFFFFFF, true);

        super.render(context, mouseX, mouseY, delta);
    }
}
