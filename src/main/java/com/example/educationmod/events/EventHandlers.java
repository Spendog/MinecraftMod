package com.example.educationmod.events;

import com.example.educationmod.EducationMod;
import com.example.educationmod.content.ContentLoader;
import com.example.educationmod.content.QuizContent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EventHandlers {

    private static int tickCounter = 0;

    @SideOnly(Side.CLIENT)
    private void sendEducationalContent(EntityPlayer player, String topic) {
        QuizContent content = ContentLoader.contents.stream()
            .filter(c -> c.getQuestion() != null && c.getQuestion().equalsIgnoreCase(topic))
            .findAny()
            .orElse(null);

        if (content != null) {
            player.addChatMessage(new ChatComponentText(String.format("[%s] %s", content.getQuestion(), content.getAnswer())));
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
        if (event.phase == TickEvent.Phase.END) {
            EntityPlayer player = event.player;
            if (player.isInWater()) {
                sendEducationalContent(player, "water");
            } else if (!player.onGround && player.motionY < 0) {
                sendEducationalContent(player, "gravity");
            }
        }
    }

    @SubscribeEvent
    public void onBlockInteract(PlayerInteractEvent event) {
        if (event.world != null && event.pos != null) {
            Block block = event.world.getBlockState(event.pos).getBlock();
            String topic = block.getUnlocalizedName();
            sendEducationalContent(event.entityPlayer, topic);
        }
    }

    @SubscribeEvent
    public void onPlayerLook(PlayerInteractEvent event) {
        EntityPlayer player = event.entityPlayer;
        MovingObjectPosition mop = EducationMod.proxy.getPlayerLookTarget(player);

        if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            Block block = player.worldObj.getBlockState(mop.getBlockPos()).getBlock();
            String topic = block.getUnlocalizedName();
            sendEducationalContent(player, topic);
        }
    }

    @SubscribeEvent
    public void onWorldTick(WorldTickEvent event) {
        if (!event.world.isRemote) {
            tickCounter++;
            if (tickCounter >= 1200) { // 1200 ticks = 1 minute
                @SuppressWarnings("unchecked")
                List<EntityPlayer> players = event.world.playerEntities;
                for (EntityPlayer player : players) {
                    sendEducationalContent(player, "general");
                }
                tickCounter = 0;
            }
        }
    }
}
