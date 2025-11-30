package com.example.educationmod;

import com.example.educationmod.gui.QuizScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ActionManager {

    public static void executeAction(String actionType, String data) {
        switch (actionType) {
            case "OPEN_COURSE":
                // Open Course Screen
                net.minecraft.client.MinecraftClient.getInstance().execute(() -> {
                    String courseId = data.replace(".json", "");
                    net.minecraft.client.MinecraftClient.getInstance()
                            .setScreen(new com.example.educationmod.gui.CourseScreen(null, courseId));
                });
                break;
            case "ADD_CHAPTER":
                // Unlock chapter logic (Placeholder)
                com.example.educationmod.ActivityLogger.log("Unlocked chapter: " + data);
                break;
            case "QUIZ":
                openQuiz(data);
                break;
            case "COMMAND":
                if (ModSettings.isSafeMode()) {
                    EducationMod.LOGGER.info("Command blocked by Safe Mode: " + data);
                    if (MinecraftClient.getInstance().player != null) {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("§cAction blocked by Safe Mode"),
                                true);
                    }
                } else {
                    executeCommand(data);
                }
                break;
            case "LOCK":
                // TODO: Implement input locking
                break;
            case "SEND_MESSAGE":
                com.example.educationmod.util.ChatUtils.sendMessage(MinecraftClient.getInstance(), data);
                break;
            case "SEND_TITLE":
                com.example.educationmod.util.ChatUtils.sendTitle(MinecraftClient.getInstance(), data, "");
                break;
            case "DISPLAY_ACTION_BAR":
                com.example.educationmod.util.ChatUtils.sendActionBar(MinecraftClient.getInstance(), data);
                break;
            case "SB_GIVE_ITEM":
                if (MinecraftClient.getInstance().player != null) {
                    MinecraftClient.getInstance().execute(() -> {
                        net.minecraft.registry.RegistryKey<net.minecraft.item.Item> key = net.minecraft.registry.RegistryKey
                                .of(net.minecraft.registry.RegistryKeys.ITEM,
                                        net.minecraft.util.Identifier.of(data.toLowerCase()));
                        net.minecraft.item.Item item = net.minecraft.registry.Registries.ITEM.get(key);
                        if (item != net.minecraft.item.Items.AIR) {
                            MinecraftClient.getInstance().player.getInventory()
                                    .insertStack(new net.minecraft.item.ItemStack(item));
                            com.example.educationmod.util.ChatUtils.sendActionBar(MinecraftClient.getInstance(),
                                    "Received: " + data);
                        }
                    });
                }
                break;
            case "SB_PLAY_SOUND":
                if (MinecraftClient.getInstance().player != null) {
                    MinecraftClient.getInstance().execute(() -> {
                        net.minecraft.util.Identifier soundId = net.minecraft.util.Identifier.of(data.toLowerCase());
                        MinecraftClient.getInstance().player.playSound(
                                net.minecraft.sound.SoundEvent.of(soundId), 1.0f, 1.0f);
                    });
                }
                break;
            default:
                EducationMod.LOGGER.warn("Unknown action type: " + actionType);
        }
    }

    private static void openQuiz(String topicFile) {
        MinecraftClient.getInstance().execute(() -> {
            MinecraftClient.getInstance().setScreen(new QuizScreen(topicFile));
        });
    }

    public static void execute(com.example.educationmod.registries.ActionRegistry.ActionType actionType, String data) {
        executeAction(actionType.id, data);
    }

    private static void executeCommand(String command) {
        if (MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.networkHandler.sendCommand(command);
        }
    }
}
