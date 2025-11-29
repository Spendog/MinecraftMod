package com.example.educationmod.config;

import com.example.educationmod.ModSettings;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private static final List<ConfigSchema<?>> options = new ArrayList<>();

    public static void init() {
        // Register all settings here.
        // We bind them directly to ModSettings static methods for now to maintain
        // compatibility.

        register(new ConfigSchema<>("consoleEnabled", "Developer Console", ConfigSchema.ConfigType.BOOLEAN, false,
                ModSettings::isConsoleEnabled, ModSettings::setConsoleEnabled));

        register(new ConfigSchema<>("showDebugDocs", "Show Debug Docs", ConfigSchema.ConfigType.BOOLEAN, false,
                ModSettings::isShowDebugDocs, ModSettings::setShowDebugDocs));

        register(new ConfigSchema<>("safeMode", "Safe Mode", ConfigSchema.ConfigType.BOOLEAN, false,
                ModSettings::isSafeMode, ModSettings::setSafeMode));

        register(new ConfigSchema<>("immersiveMode", "Immersive Mode", ConfigSchema.ConfigType.BOOLEAN, false,
                ModSettings::isImmersiveMode, ModSettings::setImmersiveMode));

        register(new ConfigSchema<>("hudOpacity", "HUD Opacity", ConfigSchema.ConfigType.FLOAT, 0.7f,
                ModSettings::getHudOpacity, ModSettings::setHudOpacity, 0.0f, 1.0f));

        register(new ConfigSchema<>("idleQuizEnabled", "Idle Quiz", ConfigSchema.ConfigType.BOOLEAN, true,
                ModSettings::isIdleQuizEnabled, ModSettings::setIdleQuizEnabled));

        // HUD Position is usually not manually edited via a list, but we can add it if
        // we want sliders
        register(new ConfigSchema<>("hudX", "HUD X Position", ConfigSchema.ConfigType.INTEGER, 10,
                ModSettings::getHudX, ModSettings::setHudX, 0, 1000)); // Max depends on screen, handled in UI?

        register(new ConfigSchema<>("hudY", "HUD Y Position", ConfigSchema.ConfigType.INTEGER, 10,
                ModSettings::getHudY, ModSettings::setHudY, 0, 1000));
    }

    public static void register(ConfigSchema<?> schema) {
        options.add(schema);
    }

    public static List<ConfigSchema<?>> getOptions() {
        return options;
    }
}
