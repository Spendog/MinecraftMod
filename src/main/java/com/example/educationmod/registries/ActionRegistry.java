package com.example.educationmod.registries;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionRegistry {
        private static final Map<String, ActionType> REGISTRY = new HashMap<>();
        private static final List<ActionType> ORDERED_LIST = new ArrayList<>();

        public static void register(ActionType type) {
                REGISTRY.put(type.id, type);
                ORDERED_LIST.add(type);
        }

        public static ActionType get(String id) {
                return REGISTRY.get(id);
        }

        public static List<ActionType> getAll() {
                return ORDERED_LIST;
        }

        public static void init() {
                // EDUCATIONAL ACTIONS WHITELIST
                // Core: Quiz-based learning
                register(new ActionType("QUIZ", "Start Quiz", new ItemStack(Items.WRITABLE_BOOK),
                                "Starts an educational quiz for the player."));

                // Communication: Educational feedback
                register(new ActionType("SEND_MESSAGE", "Send Message", new ItemStack(Items.PAPER),
                                "Sends an educational message to the player."));
                register(new ActionType("SEND_TITLE", "Send Title", new ItemStack(Items.WHITE_BANNER),
                                "Shows a large educational title on screen."));
                register(new ActionType("DISPLAY_ACTION_BAR", "Action Bar Msg", new ItemStack(Items.NAME_TAG),
                                "Shows educational message above hotbar."));

                // Skyblock Education: Skill-specific quizzes
                register(new ActionType("QUIZ_SKYBLOCK_SKILL", "Skyblock Skill Quiz",
                                new ItemStack(Items.EXPERIENCE_BOTTLE),
                                "Proposes a quiz about a Skyblock skill (Mining, Combat, etc.)."));

                // Course Management
                register(new ActionType("OPEN_COURSE", "Open Course", new ItemStack(Items.BOOK),
                                "Opens a specific educational course."));
                register(new ActionType("ADD_CHAPTER", "Add Chapter", new ItemStack(Items.WRITABLE_BOOK),
                                "Unlocks a new chapter in a course."));

                // Skyblock Actions
                register(new ActionType("SB_GIVE_ITEM", "Give Item", new ItemStack(Items.EMERALD),
                                "Give Skyblock Item"));
                register(new ActionType("SB_PLAY_SOUND", "SB Sound", new ItemStack(Items.JUKEBOX),
                                "Play Skyblock Sound"));
        }

        public static class ActionType {
                public final String id;
                public final String displayName;
                public final ItemStack icon;
                public final String description;

                public ActionType(String id, String displayName, ItemStack icon, String description) {
                        this.id = id;
                        this.displayName = displayName;
                        this.icon = icon;
                        this.description = description;
                }
        }
}
