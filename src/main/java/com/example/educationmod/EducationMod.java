package com.example.educationmod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.SidedProxy;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Mod(modid = EducationMod.MODID, name = EducationMod.NAME, version = EducationMod.VERSION)
public class EducationMod {

    public static final String MODID = "educationmod"; // lowercase modid is the norm
    public static final String NAME = "Education Mod"; // You can put spaces here for prettier mod list
    public static final String VERSION = "1.0";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @SidedProxy(clientSide = "com.example.educationmod.ClientProxy",
                serverSide = "com.example.educationmod.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(MODID)
    public static EducationMod instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.setupDataDirectory(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerKeyBindings();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // No operation for now
    }
}
