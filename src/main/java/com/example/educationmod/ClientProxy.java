package com.example.educationmod;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.gui.GuiMainMenu;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy {

    private static final KeyBinding OPEN_GUI_KEY =
            new KeyBinding("Open GUI", Keyboard.KEY_G, "key.categories.misc");

    @Override
    public void setupDataDirectory(FMLPreInitializationEvent event) {
        super.setupDataDirectory(event);
    }

    @Override
    public void registerKeyBindings() {
        ClientRegistry.registerKeyBinding(OPEN_GUI_KEY);
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (OPEN_GUI_KEY.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
        }
    }
}
