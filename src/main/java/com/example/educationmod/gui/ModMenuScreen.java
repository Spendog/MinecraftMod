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
        loadFiles(ModConfigManager.EVENTS_DIR.toFile(), "Events");

        int y = 50;
        for (String file : fileList) {
            this.addDrawableChild(ButtonWidget.builder(Text.literal(file), button -> {
                // TODO: Open editor for this file
            }).dimensions(this.width / 2 - 100, y, 200, 20).build());
            y += 25;
        }

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Close"), button -> this.close())
                .dimensions(this.width / 2 - 100, this.height - 30, 200, 20).build());
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
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
