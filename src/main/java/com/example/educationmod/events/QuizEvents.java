package com.example.educationmod.events;

import com.example.educationmod.gui.QuizScreen;
import com.example.educationmod.content.QuizContent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
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
    public static void triggerQuiz(EntityPlayer player, QuizContent quiz) {
        currentQuiz = quiz;
        player.addChatMessage(new ChatComponentText("Quiz triggered: " + quiz.getTitle()), player.getUniqueID());
        openQuizScreen();
    }

    /**
     * Open the quiz GUI screen.
     */
    private static void openQuizScreen() {
        Minecraft.getInstance().setScreen(new QuizScreen(currentQuiz));
    }

    }