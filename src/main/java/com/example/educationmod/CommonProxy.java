// CommonProxy.java
package com.example.educationmod;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

public class CommonProxy {

    private static final Logger LOGGER = LogManager.getLogger("EducationMod");

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
