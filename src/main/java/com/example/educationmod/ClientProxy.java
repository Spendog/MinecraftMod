// ClientProxy.java
package com.example.educationmod;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy {

    // Define a KeyBinding for opening the GUI
    private static final KeyBinding OPEN_GUI_KEY =
            new KeyBinding("Open GUI", Keyboard.KEY_G, "key.categories.misc");

    @Override
    public void setupDataDirectory(FMLPreInitializationEvent event) {
        super.setupDataDirectory(event);
    }

    @Override
    public void registerKeyBindings() {
        // Register the key binding and event handler on client side
        ClientRegistry.registerKeyBinding(OPEN_GUI_KEY);
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        // Check if our key was pressed
        if (OPEN_GUI_KEY.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
        }
    }
}
