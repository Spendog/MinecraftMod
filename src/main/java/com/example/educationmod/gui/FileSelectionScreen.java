package com.example.educationmod.gui;

import com.example.educationmod.ModConfigManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FileSelectionScreen extends Screen {
    private final Screen parent;
    private final List<String> fileList = new ArrayList<>();
    private final Consumer<String> onSelect;

    public FileSelectionScreen(Screen parent) {
        this(parent, null);
    }

    public FileSelectionScreen(Screen parent, Consumer<String> onSelect) {
        super(Text.literal("File Editor"));
        this.parent = parent;
        this.onSelect = onSelect;
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
            this.addDrawableChild(
                    ButtonWidget.builder(Text.literal((onSelect != null ? "Select: " : "Edit: ") + file), button -> {
                        String fName = fileName.substring(fileName.indexOf(": ") + 2);
                        File targetFile = fileName.startsWith("Topics")
                                ? ModConfigManager.TOPICS_DIR.resolve(fName).toFile()
                                : ModConfigManager.EVENTS_DIR.resolve(fName).toFile();

                        if (onSelect != null) {
                            onSelect.accept(fName);
                            this.client.setScreen(parent);
                        } else {
                            if (Screen.hasShiftDown()) {
                                net.minecraft.util.Util.getOperatingSystem().open(targetFile);
                            } else {
                                if (fileName.startsWith("Topics")) {
                                    this.client.setScreen(new QuizEditorScreen(targetFile));
                                } else {
                                    net.minecraft.util.Util.getOperatingSystem().open(targetFile);
                                }
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
        // Solid Background to prevent "blur" issues
        // this.renderBackground(context, mouseX, mouseY, delta); // Removed to prevent
        // blur
        context.fill(0, 0, this.width, this.height, 0xFF100010); // Solid dark purple/black
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
