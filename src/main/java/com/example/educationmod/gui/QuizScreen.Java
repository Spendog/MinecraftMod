package com.example.educationmod.gui;

import com.example.educationmod.content.QuizContent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.ResourceLocation;

public class QuizScreen extends Screen {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("educationmod", "textures/gui/quiz_background.png");
    private final QuizContent quiz;

    public QuizScreen(QuizContent quiz) {
        super(new TextComponent(quiz.getTitle()));
        this.quiz = quiz;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Add buttons for options
        for (int i = 0; i < quiz.getOptions().length; i++) {
            String option = quiz.getOptions()[i];
            int buttonWidth = 150;
            int buttonHeight = 20;
            int buttonX = centerX - buttonWidth / 2;
            int buttonY = centerY - (quiz.getOptions().length * buttonHeight / 2) + (i * 25);

            this.addRenderableWidget(new Button(buttonX, buttonY, buttonWidth, buttonHeight, new TextComponent(option), button -> {
                handleAnswer(i);
            }));
        }
    }

    private void handleAnswer(int index) {
        boolean isCorrect = quiz.isCorrect(index);
        Minecraft.getInstance().player.displayClientMessage(
                new TextComponent(isCorrect ? "Correct!" : "Wrong!"), true
        );
        this.onClose();
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);

        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        int x = (this.width - 256) / 2;
        int y = (this.height - 256) / 2;
        blit(poseStack, x, y, 0, 0, 256, 256, 256, 256);

        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}