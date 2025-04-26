package com.example.educationmod.events;

import com.example.educationmod.content.QuizContent;
import com.example.educationmod.gui.QuizScreen;
import com.example.educationmod.EducationMod;
import com.example.educationmod.init.ModKeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class QuizEvents {
    private final Minecraft mc;
    private final KeyBinding triggerQuizKey;
    private QuizContent currentQuiz;
    
    public QuizEvents() {
        mc = Minecraft.getMinecraft();
        triggerQuizKey = ModKeyBindings.openQuizKey;
    }

    public void triggerQuiz(EntityPlayer player, QuizContent quiz) {
        if (player != null && quiz != null) {
            currentQuiz = quiz;
            openQuizScreen(quiz);
        }
    }

    @SideOnly(Side.CLIENT)
    private void openQuizScreen(QuizContent quiz) {
        if (quiz != null && mc.thePlayer != null) {
            mc.displayGuiScreen(new QuizScreen(quiz));
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (triggerQuizKey != null && triggerQuizKey.isPressed()) {
            openQuizScreen(currentQuiz);
        }
    }
}