package com.example.educationmod.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.io.File;

public class ModConfig {

    private static Configuration config;

    public static boolean enableQuizzes;
    public static int quizFrequency;

    public static void init(FMLPreInitializationEvent event) {
        // Initialize the config file
        File configFile = new File(event.getModConfigurationDirectory(), "educationmod.cfg");
        config = new Configuration(configFile);

        try {
            // Load the configuration file
            config.load();

            // Define default config values
            enableQuizzes = config.getBoolean("Enable Quizzes", Configuration.CATEGORY_GENERAL, true, "Enable or disable the quiz feature");
            quizFrequency = config.getInt("Quiz Frequency", Configuration.CATEGORY_GENERAL, 10, 1, 100, "How often quizzes should appear (in minutes)");

        } catch (Exception e) {
            // Handle any errors loading the config
            System.err.println("There was a problem loading the config file.");
        } finally {
            // Save the config file
            if (config.hasChanged()) {
                config.save();
            }
        }
    }
}
