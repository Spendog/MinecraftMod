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

        // Render Layer Graph
        renderLayerGraph(context, this.width / 2 + 20, 60);

        super.render(context, mouseX, mouseY, delta);
    }

    private void renderLayerGraph(DrawContext context, int x, int y) {
        context.drawText(this.textRenderer, "§6Knowledge Graph:", x, y, 0xFFFFFF, true);
        y += 20;

        com.example.educationmod.layers.LayerManager layerManager = com.example.educationmod.layers.LayerManager
                .getInstance();
        Map<String, java.util.List<com.example.educationmod.layers.ConceptLayer>> topicLayers = layerManager
                .getTopicLayers();

        int topicIndex = 0;
        int startX = x + 20;
        int startY = y + 100; // Start from bottom

        for (Map.Entry<String, java.util.List<com.example.educationmod.layers.ConceptLayer>> entry : topicLayers
                .entrySet()) {
            String topic = entry.getKey();
            java.util.List<com.example.educationmod.layers.ConceptLayer> layers = entry.getValue();

            int currentX = startX + (topicIndex * 80);
            int currentY = startY;

            // Draw topic label
            context.drawText(this.textRenderer, topic, currentX, currentY + 25, 0xAAAAAA, true);

            for (com.example.educationmod.layers.ConceptLayer layer : layers) {
                boolean isLearned = layerManager.getStackHeight(layer.getId()) > 0;
                int color = isLearned ? 0xFF55FF55 : 0xFF555555; // Green if learned, Gray if not
                String label = layer.getId().replace(topic + "_", ""); // Shorten label

                // Draw connection to previous (simplified vertical stack)
                if (layers.indexOf(layer) > 0) {
                    context.fill(currentX + 30, currentY + 20, currentX + 30, currentY + 30, 0xFF888888);
                }

                drawNode(context, currentX, currentY, label, color);
                currentY -= 30; // Move up
            }
            topicIndex++;
        }
    }

    private void drawNode(DrawContext context, int x, int y, String label, int color) {
        context.fill(x, y, x + 60, y + 20, 0xFF202020); // Background
        context.drawBorder(x, y, 60, 20, color); // Border
        // Truncate label if too long
        if (label.length() > 8)
            label = label.substring(0, 8) + "..";
        context.drawText(this.textRenderer, label, x + 5, y + 6, 0xFFFFFF, false);
    }
}
