package com.example.educationmod.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ChatUtils {
    private static final String PREFIX = "§7[§bEducation§7] §f";

    public static void sendMessage(MinecraftClient client, String message) {
        if (client.player != null) {
            client.player.sendMessage(Text.literal(PREFIX + message), false);
        }
    }

    public static void sendTitle(MinecraftClient client, String title, String subtitle) {
        if (client != null) {
            client.inGameHud.setTitle(Text.literal(title).formatted(Formatting.AQUA));
            client.inGameHud.setSubtitle(Text.literal(subtitle).formatted(Formatting.WHITE));
        }
    }

    public static void sendActionBar(MinecraftClient client, String message) {
        if (client.player != null) {
            client.player.sendMessage(Text.literal(message).formatted(Formatting.GREEN), true);
        }
    }
}
