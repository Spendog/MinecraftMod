package com.example.educationmod.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class QuizScreen extends Screen {
    private final String topicFile;

    public QuizScreen(String topicFile) {
        super(Text.literal("Quiz Time!"));
        this.topicFile = topicFile;
    }

    @Override
    protected void init() {
        // TODO: Load questions from topicFile
        
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Option A"), button -> {
            // Handle answer
            this.close();
        }).dimensions(this.width / 2 - 100, this.height / 2, 200, 20).build());
        
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Option B"), button -> {
            // Handle answer
            this.close();
        }).dimensions(this.width / 2 - 100, this.height / 2 + 25, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Topic: " + topicFile), this.width / 2, 40, 0xAAAAAA);
        super.render(context, mouseX, mouseY, delta);
    }
}
