package com.example.educationmod;

import com.example.educationmod.content.ContentLoader;
import com.example.educationmod.content.QuizContent;
import com.example.educationmod.gui.QuizScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

@Mod(EducationMod.MODID)
public class EducationMod {
    public static final String MODID = "educationmod";
    public static final String KEYBIND_NAME = "Open Quiz Screen"; // Keybind name
    public static final int KEYBIND_KEY = GLFW.GLFW_KEY_F6; // Default key

    public EducationMod() {
        // Register mod lifecycle events
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register event bus for general events
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Load educational content during common setup
        ContentLoader.loadContent();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // Register client-side event listeners
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }

    // Server-side event for testing purposes
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        System.out.println("EducationMod is starting on the server!");
    }

    // Inner class for handling client-specific events
    private static class ClientEventHandler {
        @SubscribeEvent
        public void onKeyPress(InputEvent.KeyInputEvent event) {
            // GLFW_KEY_F6 corresponds to key code 327
            // However, Minecraft Forge uses GLFW key codes directly
            if (event.getKey() == KEYBIND_KEY && event.getAction() == GLFW.GLFW_PRESS) {
                Player player = Minecraft.getInstance().player;
                if (player != null) {
                    // Create a sample quiz and open the screen
                    QuizContent sampleQuiz = new QuizContent(
                        "Sample Quiz",
                        "What is the capital of France?",
                        new String[]{"Paris", "Berlin", "Madrid"},
                        0 // Index of the correct answer
                    );
                    Minecraft.getInstance().setScreen(new QuizScreen(sampleQuiz));
                }
            }
        }
    }
}