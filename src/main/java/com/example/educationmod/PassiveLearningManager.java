package com.example.educationmod;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.*;

/**
 * PassiveLearningManager - The Backbone of Immersive Learning
 * 
 * Monitors player activity and streams relevant educational facts to chat.
 * This creates a "breathing" learning system where information flows naturally
 * during gameplay without interrupting the player's flow state.
 */
public class PassiveLearningManager {

    private static PassiveLearningManager INSTANCE;

    // Activity tracking
    private String currentActivity = "IDLE";
    private long lastActivityChange = 0;
    private long lastFactSent = 0;

    // Fact management
    private Map<String, List<String>> factsByActivity = new HashMap<>();
    private Map<String, Integer> factIndexByActivity = new HashMap<>();
    private long factIntervalMs = 30000; // 30 seconds default

    // Spaced repetition tracking
    private Map<String, Long> factLastShown = new HashMap<>();

    private PassiveLearningManager() {
        loadFacts();
    }

    public static PassiveLearningManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PassiveLearningManager();
        }
        return INSTANCE;
    }

    public static void init() {
        getInstance();

        // Register tick event to monitor activity
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            getInstance().tick(client);
        });
    }

    private void loadFacts() {
        // Load facts from JSON files
        // For now, use hardcoded facts as a prototype

        // Geology facts (mining)
        factsByActivity.put("MINING_COAL", Arrays.asList(
                "Coal is primarily carbon, formed from ancient plant matter over millions of years.",
                "Coal forms under intense heat and pressure deep underground.",
                "There are four main types of coal: lignite, sub-bituminous, bituminous, and anthracite.",
                "The deeper coal is buried, the higher its carbon content and energy value."));

        factsByActivity.put("MINING_IRON", Arrays.asList(
                "Iron ore is one of the most abundant elements in Earth's crust.",
                "Iron oxidizes (rusts) when exposed to oxygen and moisture.",
                "Pure iron is relatively soft; it's alloyed with carbon to make steel.",
                "The red color of iron ore comes from iron oxide (rust)."));

        // Color theory facts (crafting dyes)
        factsByActivity.put("CRAFTING_DYE", Arrays.asList(
                "Primary colors (red, blue, yellow) cannot be created by mixing other colors.",
                "Secondary colors are created by mixing two primary colors.",
                "Complementary colors are opposite each other on the color wheel.",
                "Mixing complementary colors creates neutral tones (browns and grays)."));

        // General learning facts
        factsByActivity.put("IDLE", Arrays.asList(
                "Spaced repetition is one of the most effective learning techniques.",
                "Active recall (testing yourself) is more effective than passive review.",
                "The best time to review information is just before you're about to forget it.",
                "Learning in context (while doing) improves retention by up to 75%."));
    }

    private void tick(MinecraftClient client) {
        if (client.player == null || client.world == null) {
            return;
        }

        // Detect current activity
        String newActivity = detectActivity(client.player);

        // Update activity if changed
        if (!newActivity.equals(currentActivity)) {
            currentActivity = newActivity;
            lastActivityChange = System.currentTimeMillis();
            factIndexByActivity.putIfAbsent(currentActivity, 0);
        }

        // Send facts at intervals
        long now = System.currentTimeMillis();
        if (now - lastFactSent >= factIntervalMs) {
            sendFact(client, currentActivity);
            lastFactSent = now;
        }
    }

    private String detectActivity(PlayerEntity player) {
        // Check what the player is holding/doing
        ItemStack mainHand = player.getMainHandStack();

        // Mining detection (simplified - in full version, track block break events)
        if (mainHand.getItem().toString().contains("pickaxe")) {
            // Could be mining - default to coal for prototype
            return "MINING_COAL";
        }

        // Crafting detection (simplified)
        if (mainHand.getItem().toString().contains("dye")) {
            return "CRAFTING_DYE";
        }

        return "IDLE";
    }

    private void sendFact(MinecraftClient client, String activity) {
        List<String> facts = factsByActivity.get(activity);
        if (facts == null || facts.isEmpty()) {
            return;
        }

        // Get next fact in rotation
        int index = factIndexByActivity.getOrDefault(activity, 0);
        String fact = facts.get(index);

        // Update index for next time
        factIndexByActivity.put(activity, (index + 1) % facts.size());

        // Send to chat with prefix
        com.example.educationmod.util.ChatUtils.sendMessage(client, fact);

        // Track for spaced repetition
        factLastShown.put(fact, System.currentTimeMillis());
    }

    public void setFactInterval(long intervalMs) {
        this.factIntervalMs = intervalMs;
    }

    public String getCurrentActivity() {
        return currentActivity;
    }
}
