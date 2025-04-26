package com.example.educationmod.events;

import com.example.educationmod.content.ContentLoader;
import com.example.educationmod.content.EducationalContent;
import com.example.educationmod.EducationMod;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.example.educationmod.EducationMod;
import com.example.educationmod.content.EducationalContent;
import com.example.educationmod.content.ContentLoader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class EventHandlers {
    @SideOnly(Side.CLIENT)
    private void sendEducationalContent(EntityPlayer player, String topic) {
        EducationalContent content = ContentLoader.contents.stream()
            .filter(c -> c.getTopic() != null && c.getTopic().equalsIgnoreCase(topic))
            .findAny()
            .orElse(null);
        if (content != null) {
            player.addChatMessage(new ChatComponentText(String.format("[%s] %s", content.getTopic(), content.getContent())));
        }
    }

    @SubscribeEvent
    public void onPlayerMine(PlayerEvent.BreakSpeed event) {
        Block block = event.state.getBlock();
        String topic = block.getUnlocalizedName();
        sendEducationalContent(event.entityPlayer, topic);
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        if (event.phase == Phase.END) {
            EntityPlayer player = event.player;
            if (player.isInWater()) {
                sendEducationalContent(player, "Water");
            } else if (!player.onGround && player.motionY < 0) {
                sendEducationalContent(player, "Gravity");
            }
        }
    }

    @SubscribeEvent
    public void onBlockInteract(PlayerInteractEvent event) {
        Block block = event.world.getBlockState(event.pos).getBlock();
        String topic = block.getUnlocalizedName();
        sendEducationalContent(event.entityPlayer, topic);
    }

    @SubscribeEvent
    public void onPlayerLook(PlayerInteractEvent event) {
        EntityPlayer player = event.entityPlayer;
        MovingObjectPosition mop = EducationMod.proxy.getPlayerLookTarget(player);
        
        if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            Block block = event.world.getBlockState(mop.getBlockPos()).getBlock();
            String topic = block.getUnlocalizedName();
            sendEducationalContent(player, topic);
        }
    }

    private static int tickCounter = 0;

    @SubscribeEvent
    public void onWorldTick(WorldTickEvent event) {
        if (!event.world.isRemote) {
            tickCounter++;
            if (tickCounter >= 1200) { // 1200 ticks = 1 minute
                @SuppressWarnings("unchecked")
                List<EntityPlayer> players = event.world.playerEntities;
                for (EntityPlayer player : players) {
                    sendEducationalContent(player, "General");
                }
                tickCounter = 0;
            }
        }
    }
}