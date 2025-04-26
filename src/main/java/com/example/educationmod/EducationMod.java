package com.example.educationmod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.SidedProxy;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Mod(modid = EducationMod.MODID, name = EducationMod.NAME, version = EducationMod.VERSION)
public class EducationMod {
    public static final String MODID = "EducationMod";
    public static final String NAME = "EducationMod";
    public static final String VERSION = "1.0";

    // Logger instance
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @SidedProxy(clientSide = "com.yourname.EducationMod.ClientProxy",
            serverSide = "com.yourname.EducationMod.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(MODID)
    public static EducationMod instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Prepare data folder and files
        proxy.setupDataDirectory(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Register keybind and GUI handler
        proxy.registerKeyBindings();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // (no-op for now)
    }
}