package com.example.educationmod.registries;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TriggerRegistry {
        private static final Map<String, TriggerType> REGISTRY = new HashMap<>();
        private static final List<TriggerType> ORDERED_LIST = new ArrayList<>();

        public static void register(TriggerType type) {
                REGISTRY.put(type.id, type);
                ORDERED_LIST.add(type);
        }

        public static TriggerType get(String id) {
                return REGISTRY.get(id);
        }

        public static List<TriggerType> getAll() {
                return ORDERED_LIST;
        }

        public static void init() {
                // Base Triggers
                register(new TriggerType("BLOCK_BREAK", "Block Break", new ItemStack(Items.DIAMOND_PICKAXE),
                                "Triggers when a player breaks a block.", "BLOCK"));
                register(new TriggerType("ITEM_USE", "Item Use", new ItemStack(Items.FISHING_ROD),
                                "Triggers when a player uses an item.", "ITEM"));
                register(new TriggerType("ENTITY_KILL", "Kill Entity", new ItemStack(Items.DIAMOND_SWORD),
                                "Triggers when a player kills an entity.", "ENTITY"));
                register(new TriggerType("PLAYER_JOIN", "Player Join", new ItemStack(Items.PLAYER_HEAD),
                                "Triggers when a player joins the server.", "NONE"));

                // Player Triggers
                register(new TriggerType("LEVEL_UP", "Level Up", new ItemStack(Items.EXPERIENCE_BOTTLE),
                                "Triggers when player levels up", "NONE"));
                register(new TriggerType("WEATHER_CHANGE", "Weather Change", new ItemStack(Items.WATER_BUCKET),
                                "Triggers when weather changes", "NONE"));
                register(new TriggerType("TIME_UPDATE", "Time Update", new ItemStack(Items.CLOCK),
                                "Triggers at specific time", "NONE"));

                // Combat Triggers
                register(new TriggerType("TAKE_DAMAGE", "Take Damage", new ItemStack(Items.SHIELD),
                                "Triggers when player takes damage", "NONE"));
                register(new TriggerType("DEAL_DAMAGE", "Deal Damage", new ItemStack(Items.IRON_SWORD),
                                "Triggers when player deals damage", "ENTITY"));
                register(new TriggerType("TAME_ENTITY", "Tame Entity", new ItemStack(Items.BONE),
                                "Triggers when taming an animal", "ENTITY"));
                register(new TriggerType("BREED_ENTITY", "Breed Entity", new ItemStack(Items.WHEAT),
                                "Triggers when breeding animals", "ENTITY"));

                // Misc Triggers
                register(new TriggerType("CRAFT_ITEM", "Craft Item", new ItemStack(Items.CRAFTING_TABLE),
                                "Triggers when crafting", "ITEM"));
                register(new TriggerType("SMELT_ITEM", "Smelt Item", new ItemStack(Items.FURNACE),
                                "Triggers when smelting", "ITEM"));
                register(new TriggerType("ENCHANT_ITEM", "Enchant Item", new ItemStack(Items.ENCHANTING_TABLE),
                                "Triggers when enchanting", "ITEM"));
                register(new TriggerType("FISH_ITEM", "Fish Item", new ItemStack(Items.FISHING_ROD),
                                "Triggers when catching fish", "ITEM"));

                // Skyblock Triggers
                register(new TriggerType("GAIN_SKILL_XP", "Gain Skill XP", new ItemStack(Items.EXPERIENCE_BOTTLE),
                                "Triggers when gaining skill XP", "NONE"));
        }

        public static class TriggerType {
                public final String id;
                public final String displayName;
                public final ItemStack icon;
                public final String description;
                public final String requiredConditionType; // BLOCK, ITEM, ENTITY, BIOME, NONE

                public TriggerType(String id, String displayName, ItemStack icon, String description,
                                String requiredConditionType) {
                        this.id = id;
                        this.displayName = displayName;
                        this.icon = icon;
                        this.description = description;
                        this.requiredConditionType = requiredConditionType;
                }
        }
}
