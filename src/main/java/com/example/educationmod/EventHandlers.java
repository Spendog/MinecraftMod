package com.example.educationmod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = EducationMod.MODID)
public class EventHandlers {
    @SubscribeEvent
    public static void onPlayerMine(PlayerEvent.BreakSpeed event) {
        PlayerEntity player = event.getPlayer();
        if (event.getState().getBlock().getRegistryName().toString().equals("minecraft:coal_ore")) {
            List<EducationalContent> content = ContentLoader.getContent();
            for (EducationalContent edu : content) {
                if (edu.getTopic().equals("Geology")) {
                    player.sendMessage(new StringTextComponent(edu.getContent()), player.getUUID());
                }
            }
        }
    }
}