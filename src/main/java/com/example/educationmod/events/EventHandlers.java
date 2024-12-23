package com.example.educationmod.events;

import com.example.educationmod.content.ContentLoader;
import com.example.educationmod.content.EducationalContent;
import com.example.educationmod.EducationMod;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = EducationMod.MODID)
public class EventHandlers {

    /**
     * Framework method to send educational content to a player.
     */
    private static void sendEducationalContent(EntityPlayer player, String topic) {
        List<EducationalContent> contentList = ContentLoader.getContent();
        for (EducationalContent edu : contentList) {
            if (edu.getTopic().equalsIgnoreCase(topic)) {
                player.addChatMessage(new ChatComponentText(edu.getContent()), player.getUniqueID());
                break; // Send the first matching topic content.
            }
        }
    }

    /**
     * Triggered when a player breaks a block.
     */
    @SubscribeEvent
    public static void onPlayerMine(PlayerEvent.BreakSpeed event) {
        EntityPlayer player = event.entityPlayer;
        Block block = event.getState().getBlock();

        if (block.getRegistryName().toString().equals("minecraft:coal_ore")) {
            sendEducationalContent(player, "Geology");
        }
    }

    /**
     * Triggered when a player stands on a block.
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;

        if (!player.level.isClientSide) {
            BlockPos pos = player.getPosition();
            Block blockBelow = player.level.getBlockState(pos.below()).getBlock();

            if (blockBelow.getRegistryName().toString().equals("minecraft:grass_block")) {
                sendEducationalContent(player, "Biomes");
            }
        }
    }

    /**
     * Triggered when a player interacts with a block.
     */
    @SubscribeEvent
    public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
        EntityPlayer player = event.entityPlayer;
        Block block = event.getWorld().getBlockState(event.getPos()).getBlock();

        if (block.getRegistryName().toString().equals("minecraft:oak_log")) {
            sendEducationalContent(player, "Forestry");
        }
    }

    /**
     * Triggered when a player looks at a block.
     */
    @SubscribeEvent
    public static void onPlayerLook(PlayerInteractEvent event) {
        EntityPlayer player = event.entityPlayer;
        RayTraceResult ray = player.pick(5.0D, 0.0F, false);

        if (ray.getType() == MovingObjectPosition.MovingObjectType.BLOCK) {
            String blockName = ray.getBlock().getRegistryName().toString();

            if (blockName.equals("minecraft:stone")) {
                sendEducationalContent(player, "Geology");
            }
        }
    }

    /**
     * Timed event to periodically send educational content.
     */
    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (!event.world.isClientSide) {
            tickCounter++;

            if (tickCounter >= 1200) { // 1200 ticks = 1 minute
                for (EntityPlayer player : event.world.players()) {
                    sendEducationalContent(player, "General");
                }
                tickCounter = 0; // Reset counter.
            }
        }
    }
}