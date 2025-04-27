// CommonProxy.java
package com.example.educationmod;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.SidedProxy;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

public class CommonProxy {

    private static final Logger LOGGER = LogManager.getLogger("educationmod");

    public void setupDataDirectory(FMLPreInitializationEvent event) {
        // Create data folder and example JSON files
        File configDir = event.getModConfigurationDirectory();
        File dataDir = new File(configDir, "MinecraftModData");

        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        // Example JSON file: topics.json
        File topicsFile = new File(dataDir, "topics.json");

        if (!topicsFile.exists()) {
            JsonArray topics = new JsonArray();
            JsonObject sample = new JsonObject();
            sample.addProperty("name", "Example Topic 1");
            topics.add(sample);

            try {
                Files.write(topicsFile.toPath(), topics.toString().getBytes());
            } catch (IOException e) {
                LOGGER.error("Failed to write example topics.json", e);
            }
        }

        // Load the JSON back just to confirm it works
        try {
            JsonElement loadedElement = new JsonParser().parse(new FileReader(topicsFile));
            JsonArray loaded = loadedElement.getAsJsonArray();
            LOGGER.info("Loaded topics: " + loaded.toString());
        } catch (Exception e) {
            LOGGER.error("Failed to read topics.json", e);
        }
    }

    public void registerKeyBindings() {
        // Common (server) side doesn't register keys
    }
}
