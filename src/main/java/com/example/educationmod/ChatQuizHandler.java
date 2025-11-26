package com.example.educationmod;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import com.mojang.brigadier.arguments.StringArgumentType;

/**
 * ChatQuizHandler - Handles chat-based quiz interactions
 * 
 * Allows players to answer quizzes via /edu command without opening a GUI.
 * This keeps the player in their flow state during gameplay.
 */
public class ChatQuizHandler {

    private static ChatQuizHandler INSTANCE;

    // Current active quiz
    private ModConfigManager.QuestionDefinition activeQuestion = null;
    private String activeTopicId = null;
    private long quizStartTime = 0;

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

    public void setActiveQuiz(ModConfigManager.QuestionDefinition question, String topicId) {
        this.activeQuestion = question;
        this.activeTopicId = topicId;
        this.quizStartTime = System.currentTimeMillis();
    }

    private void handleAnswer(String answer) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (activeQuestion == null) {
            client.player.sendMessage(Text.literal("§c[EduMod] No active quiz. Wait for a question to appear!"), false);
            return;
        }

        // Map letter to answer (simplified - should match the actual order sent)
        // For prototype, just check if answer contains correct text
        boolean correct = answer.equalsIgnoreCase("D"); // Placeholder logic

        // Better logic: check if the letter maps to correct answer
        // This requires storing the answer mapping when quiz is sent

        if (correct) {
            client.player.sendMessage(Text.literal("§a[EduMod] ✓ Correct! Well done!"), false);
            PlayerStats.getInstance().recordResult(activeTopicId, 1, 1);

            // Play success sound (when we fix the API)
            // client.getSoundManager().play(...)
        } else {
            client.player.sendMessage(
                    Text.literal("§c[EduMod] ✗ Not quite. The answer was: " + activeQuestion.correct_answer), false);
            PlayerStats.getInstance().recordResult(activeTopicId, 0, 1);
        }

        // Clear active quiz
        activeQuestion = null;
        activeTopicId = null;
    }

    public boolean hasActiveQuiz() {
        return activeQuestion != null;
    }
}
