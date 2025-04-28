package com.example.educationmod.config;

import com.example.educationmod.config.ModConfig;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class GuiConfigScreen extends GuiScreen {

    private GuiScreen previousScreen;

    public GuiConfigScreen(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        int buttonX = this.width / 2 - 100;
        int buttonY = this.height / 4;

        // Add buttons to interact with the config
        this.buttonList.add(new GuiButton(1, buttonX, buttonY, 200, 20, I18n.format("button.config.toggleQuizzes")));
        this.buttonList.add(new GuiButton(2, buttonX, buttonY + 30, 200, 20, I18n.format("button.config.setQuizFrequency")));
        this.buttonList.add(new GuiButton(0, buttonX, buttonY + 60, 200, 20, I18n.format("gui.done")));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:
                // Toggle quizzes
                ModConfig.enableQuizzes = !ModConfig.enableQuizzes;
                break;
            case 2:
                // For now, we can just log the quiz frequency and handle this in the future
                System.out.println("Current quiz frequency: " + ModConfig.quizFrequency);
                break;
            case 0:
                // Return to the previous screen
                Minecraft.getMinecraft().displayGuiScreen(previousScreen);
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Education Mod Config", this.width / 2, this.height / 4 - 20, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
