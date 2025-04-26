package com.example.educationmod.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class GuiMainMenu extends GuiScreen {
    @Override
    public void initGui() {
        this.buttonList.clear();
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        // Add three buttons: Topics (id=0), Actions (1), Events (2)
        this.buttonList.add(new GuiButton(0, centerX - 100, centerY - 30, 200, 20, "Topics"));
        this.buttonList.add(new GuiButton(1, centerX - 100, centerY,      200, 20, "Actions"));
        this.buttonList.add(new GuiButton(2, centerX - 100, centerY + 30, 200, 20, "Events"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        // Log which button was clicked
        if (button.id == 0) MinecraftMod.LOGGER.info("Clicked Topics Button");
        else if (button.id == 1) MinecraftMod.LOGGER.info("Clicked Actions Button");
        else if (button.id == 2) MinecraftMod.LOGGER.info("Clicked Events Button");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
