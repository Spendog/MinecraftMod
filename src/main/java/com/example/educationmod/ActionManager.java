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
            default:
                EducationMod.LOGGER.warn("Unknown action type: " + actionType);
        }
    }

    private static void openQuiz(String topicFile) {
        MinecraftClient.getInstance().execute(() -> {
            MinecraftClient.getInstance().setScreen(new QuizScreen(topicFile));
        });
    }

    private static void executeCommand(String command) {
        if (MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.networkHandler.sendCommand(command);
        }
    }
}
