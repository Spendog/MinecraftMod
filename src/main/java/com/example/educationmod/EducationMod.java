package com.example.educationmod;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EducationMod implements ModInitializer {
	public static final String MOD_ID = "educationmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Education Mod Trigger-Event Engine");

		// Initialize Managers
		ModConfigManager.init();
		TriggerRegistry.init();
	}
}
