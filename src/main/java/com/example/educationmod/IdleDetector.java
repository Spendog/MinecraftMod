package com.example.educationmod;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

/**
 * IdleDetector - Detects when the player is idle and triggers chat-based
 * quizzes
 * 
 * This is an OPTIONAL feature (off by default) that creates natural
 * "checkpoint"
 * moments for learning during gameplay pauses.
 */
public class IdleDetector {

    private static IdleDetector INSTANCE;

    // Settings
    private boolean enabled = false; // OFF by default
    private long idleThresholdMs = 10000; // 10 seconds default
    private long quizCooldownMs = 120000; // 2 minutes between quizzes

    // State tracking
    private Vec3d lastPosition = null;
    private long lastMovementTime = 0;
    private long lastQuizTime = 0;
    private boolean isIdle = false;

    private IdleDetector() {
    }

    public static IdleDetector getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IdleDetector();
        }
        return INSTANCE;
    }

    public static void init() {
        getInstance();

        // Register tick event to monitor player movement
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            getInstance().tick(client);
        });
    }

    private void tick(MinecraftClient client) {
        if (!enabled || client.player == null || client.world == null) {
            return;
        }

        PlayerEntity player = client.player;
        Vec3d currentPos = player.getPos();
        long now = System.currentTimeMillis();

        // Check if player has moved
        if (lastPosition == null || !currentPos.equals(lastPosition)) {
            lastPosition = currentPos;
            lastMovementTime = now;
            isIdle = false;
            return;
        }

        // Player hasn't moved - check if idle threshold reached
        long idleTime = now - lastMovementTime;
        if (idleTime >= idleThresholdMs && !isIdle) {
            isIdle = true;

            // Check cooldown
            if (now - lastQuizTime >= quizCooldownMs) {
                triggerQuiz(client);
                lastQuizTime = now;
            }
        }
    }

    private void triggerQuiz(MinecraftClient client) {
        // Get a random question from the weakest topic
        String weakTopic = PlayerStats.getInstance().getWeakestTopic();
        if (weakTopic == null) {
            // No stats yet, use a random topic
            weakTopic = "color_theory";
        }

        // Load topic and get a random question
        ModConfigManager.TopicDefinition topic = ModConfigManager.loadTopic(
                ModConfigManager.TOPICS_DIR.resolve(weakTopic + ".json").toString());

        if (topic == null || topic.questions == null || topic.questions.isEmpty()) {
            return;
        }

        // Pick a random question
        ModConfigManager.QuestionDefinition question = topic.questions.get(
                (int) (Math.random() * topic.questions.size()));

        // Send quiz to chat
        client.player.sendMessage(Text.literal("§6[EduMod] §eQuick Check!"), false);
        client.player.sendMessage(Text.literal("§f" + question.question), false);

        // Send answer options
        char option = 'A';
        for (String answer : question.incorrect_answers) {
            client.player.sendMessage(Text.literal("§7" + option + ") §f" + answer), false);
            option++;
        }
        // Add correct answer in random position (simplified - should shuffle)
        client.player.sendMessage(Text.literal("§7" + option + ") §f" + question.correct_answer), false);

        client.player.sendMessage(Text.literal("§7Reply with: §e/edu <A/B/C/D>"), false);

        // Store current quiz in ChatQuizHandler
        ChatQuizHandler.getInstance().setActiveQuiz(question, topic.id);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setIdleThreshold(long thresholdMs) {
        this.idleThresholdMs = thresholdMs;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public long getIdleThreshold() {
        return idleThresholdMs;
    }
}
