package com.example.educationmod.registries;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConditionRegistry {
        private static final Map<String, ConditionType> REGISTRY = new HashMap<>();
        private static final List<ConditionType> ORDERED_LIST = new ArrayList<>();

        public static void register(ConditionType type) {
                REGISTRY.put(type.id, type);
                ORDERED_LIST.add(type);
        }

        public static ConditionType get(String id) {
                return REGISTRY.get(id);
        }

        public static List<ConditionType> getAll() {
                return ORDERED_LIST;
        }

        public static void init() {
                // EDUCATIONAL CONDITIONS WHITELIST
                // Base Conditions
                register(new ConditionType("NONE", "No Condition", new ItemStack(Items.BARRIER), "Always matches."));

                // Educational Progress Conditions
                register(new ConditionType("QUIZ_COMPLETED", "Quiz Completed", new ItemStack(Items.WRITABLE_BOOK),
                                "Has completed a specific quiz."));
                register(new ConditionType("ACHIEVEMENT_UNLOCKED", "Achievement Unlocked",
                                new ItemStack(Items.NETHER_STAR),
                                "Has unlocked a specific educational achievement."));
                register(new ConditionType("SKILL_KNOWLEDGE_LEVEL", "Knowledge Level",
                                new ItemStack(Items.EXPERIENCE_BOTTLE),
                                "Has demonstrated knowledge about a skill (via quiz score)."));

                // Skyblock Context Conditions (non-gameplay)
                register(new ConditionType("HAS_SKILL_LEVEL", "Has Skill Lvl", new ItemStack(Items.DIAMOND_PICKAXE),
                                "True if player has reached a Skyblock skill level (for context)."));
                register(new ConditionType("IS_IN_DUNGEON", "In Dungeon", new ItemStack(Items.IRON_BARS),
                                "True if inside a dungeon (for contextual quizzes)."));
                register(new ConditionType("SB_LEVEL_REQ", "Level Req", new ItemStack(Items.BOOK),
                                "Skyblock Level Requirement"));
                register(new ConditionType("SB_ITEM_REQ", "Item Req", new ItemStack(Items.CHEST),
                                "Skyblock Item Requirement"));
                register(new ConditionType("SB_PET_REQ", "Pet Req", new ItemStack(Items.BONE),
                                "Skyblock Pet Requirement"));
        }

        public static class ConditionType {
                public final String id;
                public final String displayName;
                public final ItemStack icon;
                public final String description;

                public ConditionType(String id, String displayName, ItemStack icon, String description) {
                        this.id = id;
                        this.displayName = displayName;
                        this.icon = icon;
                        this.description = description;
                }
        }
}
