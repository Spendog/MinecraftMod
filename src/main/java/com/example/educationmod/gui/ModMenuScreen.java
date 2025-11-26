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
        this.fileList.clear();
        loadFiles(ModConfigManager.TOPICS_DIR.toFile(), "Topics");

        int y = 60; // Increased top margin
        int center = this.width / 2;

        // Add "Courses" button at the top
        this.addDrawableChild(ButtonWidget.builder(Text.literal("View Courses"), button -> {
            this.client.setScreen(new BookScreen(this));
        }).dimensions(center - 100, 30, 95, 20).build());

        // Add "Dashboard" button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Dashboard"), button -> {
            this.client.setScreen(new DashboardScreen(this));
        }).dimensions(center + 5, 30, 95, 20).build());

        // Add Close Button (Top Right)
        this.addDrawableChild(ButtonWidget.builder(Text.literal("X"), button -> {
            this.close();
        }).dimensions(this.width - 30, 10, 20, 20).build());

        y += 20; // Shift file list down

        // Add file buttons with better spacing
        for (String file : fileList) {
            final String fileName = file; // Capture for lambda
            this.addDrawableChild(ButtonWidget.builder(Text.literal(file), button -> {
                String fName = fileName.substring(fileName.indexOf(": ") + 2);
                File targetFile = fileName.startsWith("Topics") ? ModConfigManager.TOPICS_DIR.resolve(fName).toFile()
                        : ModConfigManager.EVENTS_DIR.resolve(fName).toFile();

                if (Screen.hasShiftDown()) {
                    net.minecraft.util.Util.getOperatingSystem().open(targetFile);
                } else {
                    if (fileName.startsWith("Topics")) {
                        this.client.setScreen(new QuizEditorScreen(targetFile));
                    } else {
                        net.minecraft.util.Util.getOperatingSystem().open(targetFile);
                    }
                }
            }).dimensions(center - 100, y, 200, 20).build());
            y += 25; // Increased spacing
        }
    }

    private void loadFiles(File dir, String prefix) {
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
            if (files != null) {
                for (File file : files) {
                    fileList.add(prefix + ": " + file.getName());
                }
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Gradient Background (Dark Blue to Purple)
        context.fillGradient(0, 0, this.width, this.height, 0xFF100010, 0xFF300030);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
