package com.example.educationmod.registries;

import com.example.educationmod.core.ContextManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequirementRegistry {
    private static final Map<String, RequirementType> REGISTRY = new HashMap<>();
    private static final List<RequirementType> ORDERED_LIST = new ArrayList<>();

    public static void register(RequirementType type) {
        REGISTRY.put(type.id, type);
        ORDERED_LIST.add(type);
    }

    public static RequirementType get(String id) {
        return REGISTRY.get(id);
    }

    public static List<RequirementType> getAll() {
        return ORDERED_LIST;
    }

    public static void init() {
        // Base Requirements
        register(new RequirementType("NONE", "No Requirement", new ItemStack(Items.BARRIER),
                "Always passes.", (player, context, data) -> true));

        // Context Requirements
        register(new RequirementType("IN_SKYBLOCK", "In Skyblock", new ItemStack(Items.GRASS_BLOCK),
                "Requires being in Skyblock.",
                (player, context, data) -> context == ContextManager.ContextType.SKYBLOCK));

        register(new RequirementType("IN_DUNGEON", "In Dungeon", new ItemStack(Items.IRON_BARS),
                "Requires being in a Dungeon.",
                (player, context, data) -> context == ContextManager.ContextType.DUNGEON));

        // Player State Requirements
        register(new RequirementType("HOLDING_PICKAXE", "Holding Pickaxe", new ItemStack(Items.IRON_PICKAXE),
                "Requires holding any pickaxe.",
                (player, context, data) -> player.getMainHandStack().getItem().toString().contains("pickaxe")));

        register(new RequirementType("HOLDING_SWORD", "Holding Sword", new ItemStack(Items.IRON_SWORD),
                "Requires holding any sword.",
                (player, context, data) -> player.getMainHandStack().getItem().toString().contains("sword")));

        register(new RequirementType("SNEAKING", "Is Sneaking", new ItemStack(Items.LEATHER_BOOTS),
                "Requires player to be sneaking.",
                (player, context, data) -> player.isSneaking()));

        // Item Requirements
        register(new RequirementType("HOLDING_ITEM", "Holding Item", new ItemStack(Items.CHEST),
                "Requires holding a specific item.",
                (player, context, data) -> {
                    if (data == null || data.isEmpty())
                        return true; // Pass if no data
                    return player.getMainHandStack().getItem().toString().contains(data) ||
                            net.minecraft.registry.Registries.ITEM.getId(player.getMainHandStack().getItem()).toString()
                                    .equals(data);
                }));
    }

    public static class RequirementType {
        public final String id;
        public final String displayName;
        public final ItemStack icon;
        public final String description;
        private final RequirementCheck check;

        public RequirementType(String id, String displayName, ItemStack icon, String description,
                RequirementCheck check) {
            this.id = id;
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
            this.check = check;
        }

        public boolean check(PlayerEntity player, ContextManager.ContextType context, String data) {
            return check.test(player, context, data);
        }
    }

    @FunctionalInterface
    public interface RequirementCheck {
        boolean test(PlayerEntity player, ContextManager.ContextType context, String data);
    }
}
