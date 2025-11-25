package com.example.educationmod;

import com.example.educationmod.gui.QuizScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ActionManager {

    public static void executeAction(String actionType, String data) {
        switch (actionType) {
            case "QUIZ":
                openQuiz(data);
                break;
            case "COMMAND":
                executeCommand(data);
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
