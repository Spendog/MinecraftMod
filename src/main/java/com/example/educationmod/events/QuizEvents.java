package com.example.educationmod.events;

import com.example.educationmod.gui.QuizScreen;
import com.example.educationmod.content.QuizContent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "educationmod", value = Dist.CLIENT)
public class QuizEvents {

    private static QuizContent currentQuiz;

    /**
     * Trigger a quiz based on some game event or input.
     */
    public static void triggerQuiz(PlayerEntity player, QuizContent quiz) {
        currentQuiz = quiz;
        player.sendMessage(new StringTextComponent("Quiz triggered: " + quiz.getTitle()), player.getUUID());
        openQuizScreen();
    }

    /**
     * Open the quiz GUI screen.
     */
    private static void openQuizScreen() {
        Minecraft.getInstance().setScreen(new QuizScreen(currentQuiz));
    }

    /**
     * Keybinding to open a random quiz for testing purposes.
     */
    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event) {
        if (/* Check your custom keybind here */) {
            PlayerEntity player = Minecraft.getInstance().player;
            if (player != null) {
                QuizContent sampleQuiz = new QuizContent("Sample Quiz", "What is the capital of France?", "Paris");
                triggerQuiz(player, sampleQuiz);
            }
        }
    }
}