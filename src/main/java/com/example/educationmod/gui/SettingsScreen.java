package com.example.educationmod.gui;

import com.example.educationmod.ModSettings;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class SettingsScreen extends Screen {
    private final Screen parent;

    public SettingsScreen(Screen parent) {
        super(Text.literal("Mod Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        int y = 40;

        // Console Toggle
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Developer Console: " + (ModSettings.isConsoleEnabled() ? "ON" : "OFF")),
                button -> {
                    boolean newState = !ModSettings.isConsoleEnabled();
                    ModSettings.setConsoleEnabled(newState);
                    button.setMessage(Text.literal("Developer Console: " + (newState ? "ON" : "OFF")));
                }).dimensions(center - 100, y, 200, 20).build());

        y += 25;

        // Debug Docs Toggle
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Show Debug Docs: " + (ModSettings.isShowDebugDocs() ? "ON" : "OFF")),
                button -> {
                    boolean newState = !ModSettings.isShowDebugDocs();
                    ModSettings.setShowDebugDocs(newState);
                    button.setMessage(Text.literal("Show Debug Docs: " + (newState ? "ON" : "OFF")));
                }).dimensions(center - 100, y, 200, 20).build());

        y += 25;

        // Safe Mode Toggle
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Safe Mode: " + (ModSettings.isSafeMode() ? "ON" : "OFF")),
                button -> {
                    boolean newState = !ModSettings.isSafeMode();
                    ModSettings.setSafeMode(newState);
                    button.setMessage(Text.literal("Safe Mode: " + (newState ? "ON" : "OFF")));
                }).dimensions(center - 100, y, 200, 20).build());

        y += 25;

        // Immersive Mode Toggle
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Immersive Mode: " + (ModSettings.isImmersiveMode() ? "ON" : "OFF")),
                button -> {
                    boolean newState = !ModSettings.isImmersiveMode();
                    ModSettings.setImmersiveMode(newState);
                    button.setMessage(Text.literal("Immersive Mode: " + (newState ? "ON" : "OFF")));
                }).dimensions(center - 100, y, 200, 20).build());

        y += 25;

        // HUD Opacity Cycle
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("HUD Opacity: " + (int) (ModSettings.getHudOpacity() * 100) + "%"),
                button -> {
                    float current = ModSettings.getHudOpacity();
                    float next = current >= 1.0f ? 0.5f : current + 0.25f;
                    ModSettings.setHudOpacity(next);
                    button.setMessage(Text.literal("HUD Opacity: " + (int) (next * 100) + "%"));
                }).dimensions(center - 100, y, 200, 20).build());

        y += 25;

        // Idle Quiz Toggle
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Idle Quiz: " + (ModSettings.isIdleQuizEnabled() ? "ON" : "OFF")),
                button -> {
                    boolean newState = !ModSettings.isIdleQuizEnabled();
                    ModSettings.setIdleQuizEnabled(newState);
                    button.setMessage(Text.literal("Idle Quiz: " + (newState ? "ON" : "OFF")));
                }).dimensions(center - 100, y, 200, 20).build());

        y += 25;

        // Reset HUD Position
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Reset HUD Position"),
                button -> {
                    ModSettings.setHudX(10);
                    ModSettings.setHudY(10);
                    // Also update the live HUD instance if possible, but ModSettings is the source
                    // of truth now
                }).dimensions(center - 100, y, 200, 20).build());

        // Back Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), button -> {
            this.client.setScreen(parent);
        }).dimensions(center - 100, this.height - 40, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Gradient Background
        context.fillGradient(0, 0, this.width, this.height, 0xFF000000, 0xFF202020);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);

        super.render(context, mouseX, mouseY, delta);
    }
}
