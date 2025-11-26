package com.example.educationmod;

import com.example.educationmod.gui.ModMenuScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.client.MinecraftClient;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class EducationModClient implements ClientModInitializer {

    private static KeyBinding menuKeyBinding;

    @Override
    public void onInitializeClient() {
        EducationMod.LOGGER.info("EducationModClient Initializing...");

        // Register KeyBinding
        menuKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.educationmod.menu", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_M, // The keycode of the key
                "category.educationmod.general" // The translation key of the keybinding's category.
        ));

        // Register Tick Event to check for key press
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKeyBinding.wasPressed()) {
                client.setScreen(new ModMenuScreen());
            }
        });

        // Register Command
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("edumenu")
                    .executes(context -> {
                        EducationMod.LOGGER.info("Command /edumenu executed!");
                        MinecraftClient.getInstance().execute(() -> {
                            MinecraftClient.getInstance().setScreen(new ModMenuScreen());
                        });
                        return 1;
                    }));
        });

        EducationMod.LOGGER.info("EducationModClient Initialized. Keybinding 'M' registered.");
    }
}
