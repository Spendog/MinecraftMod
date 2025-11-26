package com.example.educationmod.gui;

import com.example.educationmod.IdleDetector;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class SettingsScreen extends Screen {
    private final Screen parent;
    public static boolean safeMode = true; // Default to Safe Mode ON
    public static boolean immersiveMode = true; // Immersive Learning ON by default
    public static float hudOpacity = 0.7f; // 70% opacity default
    public static String idleQuizTrigger = "OFF"; // OFF by default

    public SettingsScreen(Screen parent) {
        super(Text.literal("Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        int y = 60;

        // Safe Mode Toggle
        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Hypixel Safe Mode: " + (safeMode ? "ON" : "OFF")), button -> {
                    safeMode = !safeMode;
                    button.setMessage(Text.literal("Hypixel Safe Mode: " + (safeMode ? "ON" : "OFF")));
                }).dimensions(center - 100, y, 200, 20).build());
        y += 30;

        // Immersive Mode Toggle
        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Immersive Learning: " + (immersiveMode ? "ON" : "OFF")), button -> {
                    immersiveMode = !immersiveMode;
                    LearningHUD.getInstance().setEnabled(immersiveMode);
                    button.setMessage(Text.literal("Immersive Learning: " + (immersiveMode ? "ON" : "OFF")));
                }).dimensions(center - 100, y, 200, 20).build());
        y += 30;

        // HUD Opacity
        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("HUD Opacity: " + (int) (hudOpacity * 100) + "%"), button -> {
                    hudOpacity += 0.1f;
                    if (hudOpacity > 1.0f)
                        hudOpacity = 0.5f;
                    LearningHUD.getInstance().setOpacity(hudOpacity);
                    button.setMessage(Text.literal("HUD Opacity: " + (int) (hudOpacity * 100) + "%"));
                }).dimensions(center - 100, y, 200, 20).build());
        y += 30;

        // Idle Quiz Trigger
        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Idle Quiz: " + idleQuizTrigger), button -> {
                    switch (idleQuizTrigger) {
                        case "OFF" -> {
                            idleQuizTrigger = "5s";
                            IdleDetector.getInstance().setEnabled(true);
                            IdleDetector.getInstance().setIdleThreshold(5000);
                        }
                        case "5s" -> {
                            idleQuizTrigger = "10s";
                            IdleDetector.getInstance().setIdleThreshold(10000);
                        }
                        case "10s" -> {
                            idleQuizTrigger = "30s";
                            IdleDetector.getInstance().setIdleThreshold(30000);
                        }
                        case "30s" -> {
                            idleQuizTrigger = "1min";
                            IdleDetector.getInstance().setIdleThreshold(60000);
                        }
                        case "1min" -> {
                            idleQuizTrigger = "OFF";
                            IdleDetector.getInstance().setEnabled(false);
                        }
                    }
                    button.setMessage(Text.literal("Idle Quiz: " + idleQuizTrigger));
                }).dimensions(center - 100, y, 200, 20).build());
        y += 30;

        // Reset HUD Position
        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Reset HUD Position"), button -> {
                    LearningHUD.getInstance().resetPosition();
                }).dimensions(center - 100, y, 200, 20).build());

        // Back Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), button -> this.client.setScreen(parent))
                .dimensions(center - 100, this.height - 40, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fillGradient(0, 0, this.width, this.height, 0xFF001020, 0xFF002040);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);

        // Help text
        context.drawCenteredTextWithShadow(this.textRenderer, "§7Configure your learning experience", this.width / 2,
                40, 0xAAAAAA);

        super.render(context, mouseX, mouseY, delta);
    }
}
