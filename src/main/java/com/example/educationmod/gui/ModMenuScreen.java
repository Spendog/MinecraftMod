package com.example.educationmod.gui;

import com.example.educationmod.ModConfigManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModMenuScreen extends Screen {
    private final List<String> fileList = new ArrayList<>();

    public ModMenuScreen() {
        super(Text.literal("Education Mod Menu"));
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        int y = 50;

        // --- Top Row: Main Features ---
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Courses"), button -> {
            this.client.setScreen(new BookScreen(this));
        }).dimensions(center - 105, y, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Dashboard"), button -> {
            this.client.setScreen(new DashboardScreen(this));
        }).dimensions(center + 5, y, 100, 20).build());

        y += 25;

        // --- Second Row: Tools ---
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Triggers"), button -> {
            this.client.setScreen(new TriggerEditorScreen(this));
        }).dimensions(center - 105, y, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Settings"), button -> {
            this.client.setScreen(new SettingsScreen(this));
        }).dimensions(center + 5, y, 100, 20).build());

        y += 35;

        // --- Editor Access ---
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Open File Editor"), button -> {
            this.client.setScreen(new FileSelectionScreen(this));
        }).dimensions(center - 100, y, 200, 20).build());

        // Add Close Button (Top Right)
        this.addDrawableChild(ButtonWidget.builder(Text.literal("X"), button -> {
            this.close();
        }).dimensions(this.width - 30, 10, 20, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Gradient Background (Dark Blue to Purple)
        context.fillGradient(0, 0, this.width, this.height, 0xFF100010, 0xFF300030);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);

        // Next Goal Display (Anchored to bottom with padding)
        int bottomMargin = 30;
        context.drawCenteredTextWithShadow(this.textRenderer, "Next Goal: Master Hex Codes", this.width / 2,
                this.height - bottomMargin, 0x55FFFF);

        super.render(context, mouseX, mouseY, delta);
    }
}
