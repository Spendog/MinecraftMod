package com.example.educationmod.gui;

import com.example.educationmod.PlayerStats;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ModMenuScreen extends Screen {

    public ModMenuScreen() {
        super(Text.literal("Education Mod Menu"));
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        int y = 50;

        // Main Features
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Courses"), button -> {
            this.client.setScreen(new BookScreen(this));
        }).dimensions(center - 105, y, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Dashboard"), button -> {
            this.client.setScreen(new DashboardScreen(this));
        }).dimensions(center + 5, y, 100, 20).build());

        y += 25;

        // Tools
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Triggers"), button -> {
            this.client.setScreen(new TriggerEditorScreen(this));
        }).dimensions(center - 105, y, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Settings"), button -> {
            this.client.setScreen(new SettingsScreen(this));
        }).dimensions(center + 5, y, 100, 20).build());

        y += 35;

        // File Editor
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Open File Editor"), button -> {
            this.client.setScreen(new FileSelectionScreen(this));
        }).dimensions(center - 100, y, 200, 20).build());

        // Close Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("X"), button -> {
            this.close();
        }).dimensions(this.width - 30, 10, 20, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Gradient Background
        context.fillGradient(0, 0, this.width, this.height, 0xFF001020, 0xFF002040);

        // Title
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);

        // Layer Progress (bottom-left)
        PlayerStats stats = PlayerStats.getInstance();
        int totalLayers = stats.topicConfidence.size();
        String layersText = "§7Layers Learned: §e" + totalLayers;
        context.drawText(this.textRenderer, layersText, 10, this.height - 20, 0xFFFFFF, true);

        super.render(context, mouseX, mouseY, delta);
    }
}
