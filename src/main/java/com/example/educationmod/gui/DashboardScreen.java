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

        super.render(context, mouseX, mouseY, delta);
    }
}
