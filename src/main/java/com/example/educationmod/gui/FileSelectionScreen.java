package com.example.educationmod.gui;

import com.example.educationmod.ModConfigManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSelectionScreen extends Screen {
    private final Screen parent;
    private final List<String> fileList = new ArrayList<>();

    public FileSelectionScreen(Screen parent) {
        super(Text.literal("File Editor"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.fileList.clear();
        loadFiles(ModConfigManager.TOPICS_DIR.toFile(), "Topics");
        loadFiles(ModConfigManager.EVENTS_DIR.toFile(), "Events");

        int center = this.width / 2;

        // Close/Back Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), button -> {
            this.client.setScreen(parent);
        }).dimensions(center - 100, this.height - 30, 200, 20).build());

        int y = 40;
        for (String file : fileList) {
            final String fileName = file;
            this.addDrawableChild(ButtonWidget.builder(Text.literal("Edit: " + file), button -> {
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
            }).dimensions(center - 150, y, 300, 20).build());
            y += 25;
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
        context.fillGradient(0, 0, this.width, this.height, 0xFF100010, 0xFF300030);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
