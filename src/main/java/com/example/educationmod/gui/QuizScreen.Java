package com.example.educationmod.gui;

import com.example.educationmod.content.QuizContent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;

import java.io.IOException;

public class QuizScreen extends GuiScreen {
    private final QuizContent quiz;

    public QuizScreen(QuizContent quiz) {
        this.quiz = quiz;
    }

    @Override
    public void initGui() {
        // Add a "Close" button
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + 50, I18n.format("gui.done")));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) { // Close button
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        if (quiz != null) {
            String title = "Quiz: " + quiz.getTopic();
            String question = quiz.getQuestion();
            this.drawCenteredString(this.fontRendererObj, title, this.width / 2, this.height / 2 - 40, 0xFFFFFF);
            this.drawCenteredString(this.fontRendererObj, question, this.width / 2, this.height / 2 - 20, 0xFFFFFF);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false; // Don't pause the game when the quiz pops up
    }
}
