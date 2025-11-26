package com.example.educationmod;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import com.mojang.brigadier.arguments.StringArgumentType;

import java.util.HashMap;
import java.util.Map;

/**
 * ChatQuizHandler - Handles chat-based quiz interactions with layer integration
 */
public class ChatQuizHandler {

    private static ChatQuizHandler INSTANCE;

    // Current active quiz
    private ModConfigManager.QuestionDefinition activeQuestion = null;
    private String activeTopicId = null;
    private Map<String, String> answerMapping = new HashMap<>();
    private String correctLetter = null;

    private ChatQuizHandler() {
    }

    public static ChatQuizHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChatQuizHandler();
        }
        return INSTANCE;
    }

    public static void init() {
        getInstance();

        // Register /edu command
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("edu")
                    .then(ClientCommandManager.argument("answer", StringArgumentType.word())
                            .executes(context -> {
                                String answer = StringArgumentType.getString(context, "answer").toUpperCase();
                                getInstance().handleAnswer(answer);
                                return 1;
                            })));
        });
    }

    public void setActiveQuiz(ModConfigManager.QuestionDefinition question, String topicId,
            Map<String, String> mapping, String correctLetter) {
        this.activeQuestion = question;
        this.activeTopicId = topicId;
        this.answerMapping = mapping;
        this.correctLetter = correctLetter;
    }

    private void handleAnswer(String answer) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (activeQuestion == null) {
            client.player.sendMessage(Text.literal("§c[EduMod] No active quiz. Wait for a question to appear!"), false);
            return;
        }

        // Check if answer is correct
        boolean correct = answer.equalsIgnoreCase(correctLetter);

        if (correct) {
            client.player.sendMessage(Text.literal("§a[EduMod] ✓ Correct! Well done!"), false);
            PlayerStats.getInstance().recordResult(activeTopicId, 1, 1);

            // Stack the related concept layer
            String layerId = activeTopicId + "_layer_1"; // Simplified mapping
            boolean stacked = com.example.educationmod.layers.LayerManager.getInstance().stackConcept(layerId);

            if (stacked) {
                int stackHeight = com.example.educationmod.layers.LayerManager.getInstance().getStackHeight(layerId);
                client.player.sendMessage(Text.literal("§e[EduMod] 📚 Layer reinforced! Stack: " + stackHeight), false);
            }
        } else {
            client.player.sendMessage(
                    Text.literal("§c[EduMod] ✗ Not quite. The answer was: §e" + correctLetter + ") "
                            + activeQuestion.correct_answer),
                    false);
            PlayerStats.getInstance().recordResult(activeTopicId, 0, 1);

            // Show missing prerequisites if any
            String layerId = activeTopicId + "_layer_1";
            java.util.List<String> missing = com.example.educationmod.layers.LayerManager.getInstance()
                    .getMissingPrerequisites(layerId);

            if (!missing.isEmpty()) {
                client.player.sendMessage(Text.literal("§7[EduMod] 💡 Foundation needed: Learn basics first"), false);
            }
        }

        // Clear active quiz
        activeQuestion = null;
        activeTopicId = null;
        answerMapping.clear();
        correctLetter = null;
    }

    public boolean hasActiveQuiz() {
        return activeQuestion != null;
    }
}
