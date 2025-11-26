package com.example.educationmod;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import java.io.File; // Added import

public class TriggerRegistry {

    private static final java.util.Set<net.minecraft.item.Item> previousInventory = new java.util.HashSet<>();
    private static boolean firstRun = true;

    public static void init() {
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
                    // New item found!
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
        for (ModConfigManager.EventDefinition event : ModConfigManager.getEvents()) {
            if (event.trigger.equalsIgnoreCase(triggerType)) {
                // Simple contains check for flexibility, or exact match
                if (condition.equals(event.condition) || condition.contains(event.condition)) {
                    if (event.action != null) {
                        // Adaptive Logic: If action is QUIZ, try to find a weak topic first
                        if ("QUIZ".equals(event.action.type)) {
                            String weakTopic = PlayerStats.getInstance().getWeakestTopic();
                            if (weakTopic != null && !weakTopic.isEmpty()) {
                                // Override with weak topic
                                File topicFile = ModConfigManager.TOPICS_DIR.resolve(weakTopic + ".json").toFile();
                                if (topicFile.exists()) {
                                    ActionManager.executeAction("QUIZ", topicFile.getAbsolutePath());
                                    return; // Executed adaptive quiz
                                }
                            }
                        }

                        // Fallback to default action
                        ActionManager.executeAction(event.action.type, event.action.data);
                    }
                }
            }
        }
    }
}
