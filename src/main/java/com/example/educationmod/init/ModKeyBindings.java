package com.example.educationmod.init;

import com.example.educationmod.EducationMod;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public class ModKeyBindings {
    public static KeyBinding openQuizKey;

    @SideOnly(Side.CLIENT)
    public static void init() {
        openQuizKey = new KeyBinding("key.educationmod.quiz", Keyboard.KEY_F6, "Education Mod");
        ClientRegistry.registerKeyBinding(openQuizKey);
    }
}