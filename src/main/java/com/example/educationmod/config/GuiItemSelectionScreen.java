package com.example.educationmod.config;

import com.example.educationmod.content.QuizContent;
import com.example.educationmod.content.ContentLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class GuiItemSelectionScreen extends GuiScreen {

    private int currentTopicIndex = 0;
    private List<QuizContent> quizContents = ContentLoader.contents;

    @Override
    public void initGui() {
        super.initGui();

        // Add buttons for each quiz topic
        int buttonYPos = this.height / 4;
        for (int i = 0; i < quizContents.size(); i++) {
            this.buttonList.add(new GuiButton(i, this.width / 2 - 100, buttonYPos + i * 30, 200, 20, quizContents.get(i).getQuestion()));
        }

        // Add the test button
        this.buttonList.add(new GuiButton(quizContents.size(), this.width / 2 - 100, buttonYPos + quizContents.size() * 30, 200, 20, "Test Quiz Topics"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == quizContents.size()) {
            // Test button clicked, cycle through quiz topics
            cycleThroughQuizTopics();
        } else {
            // Regular quiz topic button clicked
            QuizContent selectedContent = quizContents.get(button.id);
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(String.format("[%s] %s", selectedContent.getQuestion(), selectedContent.getAnswer())));
            mc.displayGuiScreen(null);
        }
    }

    // Method to cycle through quiz topics when the test button is clicked
    private void cycleThroughQuizTopics() {
        if (quizContents.isEmpty()) return;

        // Get the current quiz topic and show it in the chat
        QuizContent currentTopic = quizContents.get(currentTopicIndex);
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(String.format("[%s] %s", currentTopic.getQuestion(), currentTopic.getAnswer())));

        // Move to the next topic
        currentTopicIndex = (currentTopicIndex + 1) % quizContents.size();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        // Draw the title at the top of the screen
        fontRendererObj.drawString("Select a Quiz Topic", this.width / 2 - fontRendererObj.getStringWidth("Select a Quiz Topic") / 2, 20, 0xFFFFFF);
    }
}
