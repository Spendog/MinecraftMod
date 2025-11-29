package com.example.educationmod;

import com.example.educationmod.gui.LearningHUD;
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
                "key.educationmod.menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "category.educationmod.general"));

        KeyBinding consoleKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.educationmod.console",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_GRAVE_ACCENT,
                "category.educationmod.general"));

        // Register Tick Event to check for key press
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKeyBinding.wasPressed()) {
                client.setScreen(new ModMenuScreen());
            }

            while (consoleKeyBinding.wasPressed()) {
                com.example.educationmod.debug.DeveloperConsole.getInstance().toggle();
                // State is now handled by DeveloperConsole toggling ModSettings
                if (com.example.educationmod.debug.DeveloperConsole.getInstance().isVisible()) {
                    client.setScreen(new com.example.educationmod.debug.DeveloperConsoleScreen(client.currentScreen));
                } else {
                    // If we toggle it off via key, close screen
                    if (client.currentScreen instanceof com.example.educationmod.debug.DeveloperConsoleScreen) {
                        client.setScreen(null);
                    }
                }
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

        // Initialize v012 Immersive Learning Systems
        PassiveLearningManager.init();
        IdleDetector.init();
        ChatQuizHandler.init();
        LearningHUD.init();

        EducationMod.LOGGER.info("EducationModClient Initialized with v012 Immersive Learning.");
    }
}
