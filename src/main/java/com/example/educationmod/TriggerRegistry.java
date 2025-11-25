package com.example.educationmod;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;

public class TriggerRegistry {

    public static void init() {
        registerBlockBreakListener();
        registerUseItemListener();
        registerChatListener();
    }

    private static void registerBlockBreakListener() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (!world.isClient) return true;
            
            String blockId = state.getBlock().getTranslationKey();
            checkAndExecute("BLOCK_BREAK", blockId);
            return true;
        });
    }

    private static void registerUseItemListener() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (!world.isClient) return TypedActionResult.pass(ItemStack.EMPTY);

            ItemStack stack = player.getStackInHand(hand);
            String itemId = stack.getItem().getTranslationKey();
            checkAndExecute("USE_ITEM", itemId);
            return TypedActionResult.pass(stack);
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
                if (condition.matches(event.condition) || condition.equals(event.condition)) {
                    if (event.action != null) {
                        ActionManager.executeAction(event.action.type, event.action.data);
                    }
                }
            }
        }
    }
}
