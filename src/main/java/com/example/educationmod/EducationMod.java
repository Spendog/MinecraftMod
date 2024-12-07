package com.example.educationmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("educationmod")
public class EducationMod {
    public static final String MODID = "educationmod";

    public EducationMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
    }

    private void setup(final FMLCommonSetupEvent event) {
        ContentLoader.loadContent();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // Register client-side stuff if needed
    }
}