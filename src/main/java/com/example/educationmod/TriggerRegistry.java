package com.example.educationmod;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TriggerRegistry {

    private static final java.util.Set<net.minecraft.item.Item> previousInventory = new java.util.HashSet<>();
    private static boolean firstRun = true;

    // Trigger statistics
    private static final Map<String, Integer> triggerCounts = new HashMap<>();
    private static final Map<String, Long> lastTriggered = new HashMap<>();

    public static void init() {
        com.example.educationmod.debug.DeveloperConsole.getInstance().log("TriggerRegistry Initializing...", 0xAAAAAA);
        registerBlockBreakListener();
        registerUseItemListener();
        registerChatListener();
        registerInventoryListener();
    }

    private static void registerInventoryListener() {
        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null)
                return;

            java.util.Set<net.minecraft.item.Item> currentInventory = new java.util.HashSet<>();
            for (int i = 0; i < client.player.getInventory().size(); i++) {
                ItemStack stack = client.player.getInventory().getStack(i);
                if (!stack.isEmpty()) {
                    currentInventory.add(stack.getItem());
                }
            }

            if (firstRun) {
                previousInventory.addAll(currentInventory);
                firstRun = false;
                return;
            }

            for (net.minecraft.item.Item item : currentInventory) {
                if (!previousInventory.contains(item)) {
                    String itemId = item.getTranslationKey();
                    checkAndExecute("ITEM_PICKUP", itemId);
                }
            }

            previousInventory.clear();
            previousInventory.addAll(currentInventory);
        });
    }

    private static void registerBlockBreakListener() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (!world.isClient)
                return true;

            String blockId = state.getBlock().getTranslationKey();
            checkAndExecute("BLOCK_BREAK", blockId);
            return true;
        });
    }

    private static void registerUseItemListener() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (!world.isClient)
                return ActionResult.PASS;

            ItemStack stack = player.getStackInHand(hand);
            String itemId = stack.getItem().getTranslationKey();
            checkAndExecute("USE_ITEM", itemId);
            return ActionResult.PASS;
        });
    }

    private static void registerChatListener() {
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            String text = message.getString();
            checkAndExecute("CHAT", text);
        });
    }

    private static void checkAndExecute(String triggerType, String condition) {
        java.util.List<ModConfigManager.EventDefinition> events = com.example.educationmod.core.RelationalStore
                .getInstance().findEvents(triggerType, condition);

        if (events != null) {
            for (ModConfigManager.EventDefinition event : events) {
                if (event.action != null) {
                    // Track trigger statistics
                    String triggerKey = triggerType + ":" + event.condition;
                    triggerCounts.put(triggerKey, triggerCounts.getOrDefault(triggerKey, 0) + 1);
                    lastTriggered.put(triggerKey, System.currentTimeMillis());

                    // Log to Console
                    com.example.educationmod.debug.DeveloperConsole.getInstance().log("Trigger Fired: " + triggerKey,
                            0x55FF55);

                    // Adaptive Logic: If action is QUIZ, try weak topic first
                    if ("QUIZ".equals(event.action.type)) {
                        String weakTopic = PlayerStats.getInstance().getWeakestTopic();
                        if (weakTopic != null && !weakTopic.isEmpty()) {
                            // We need to find the file path for the weak topic.
                            // Since RelationalStore stores topics by ID, we can check if it exists there.
                            // But ActionManager expects a filepath or ID. Let's assume ID works if we
                            // updated ActionManager.
                            // Actually, ActionManager likely reads the file.
                            // For now, let's keep the old logic but use the store to verify existence.
                            ModConfigManager.TopicDefinition topic = com.example.educationmod.core.RelationalStore
                                    .getInstance().getTopic(weakTopic);
                            if (topic != null) {
                                ActionManager.executeAction("QUIZ", weakTopic + ".json"); // Reconstruct filename for
                                                                                          // now
                                return;
                            }
                        }
                    }

                    ActionManager.executeAction(event.action.type, event.action.data);
                }
            }
        }
    }

    // Public getters for statistics
    public static Map<String, Integer> getTriggerCounts() {
        return new HashMap<>(triggerCounts);
    }

    public static Map<String, Long> getLastTriggered() {
        return new HashMap<>(lastTriggered);
    }
}
