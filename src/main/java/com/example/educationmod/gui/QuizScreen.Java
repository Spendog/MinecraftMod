package com.example.educationmod.gui.QuizScreen;

import com.example.educationmod.content.QuizContent;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import java.io.IOException;

public class QuizScreen extends GuiScreen {

    private QuizContent quizContent;

    public QuizScreen(QuizContent quizContent) {
        this.quizContent = quizContent;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        // Center the button
        int buttonWidth = 200;
        int buttonHeight = 20;
        int centerX = this.width / 2 - buttonWidth / 2;
        int centerY = this.height / 2 - buttonHeight / 2;

        // Add "Start Quiz" button
        this.buttonList.add(new GuiButton(1, centerX, centerY, buttonWidth, buttonHeight, "Start Quiz"));

        // Add more buttons or fields as needed
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            // Implement quiz start logic
            System.out.println("Quiz Started: " + quizContent.getTitle());
            // For example, transition to the first question screen
            // this.mc.displayGuiScreen(new QuizQuestionScreen(quizContent.getQuestions().get(0)));
            this.mc.displayGuiScreen(null); // Close the GUI for now
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Draw the background
        this.drawDefaultBackground();

        // Draw title
        this.drawCenteredString(this.fontRenderer, "Quiz Time!", this.width / 2, 20, 0xFFFFFF);

        // Draw additional text or images here
        this.drawCenteredString(this.fontRenderer, quizContent.getQuestion(), this.width / 2, 60, 0xFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false; // Set to true if you want to pause the game when the GUI is open
    }
}