package com.example.educationmod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModSettings {
    private static final File SETTINGS_FILE = FabricLoader.getInstance().getConfigDir()
            .resolve("mod_data/mod_settings.json").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static SettingsData data = new SettingsData();

    public static void load() {
        if (SETTINGS_FILE.exists()) {
            try (FileReader reader = new FileReader(SETTINGS_FILE)) {
                data = GSON.fromJson(reader, SettingsData.class);
                if (data == null)
                    data = new SettingsData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            save(); // Create default
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(SETTINGS_FILE)) {
            GSON.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isConsoleEnabled() {
        return data.consoleEnabled;
    }

    public static void setConsoleEnabled(boolean enabled) {
        data.consoleEnabled = enabled;
        save();
    }

    public static boolean isShowDebugDocs() {
        return data.showDebugDocs;
    }

    public static void setShowDebugDocs(boolean show) {
        data.showDebugDocs = show;
        save();
    }

    public static class SettingsData {
        public boolean consoleEnabled = false; // Default off
        public boolean showDebugDocs = false;
        public boolean safeMode = false;
        public boolean immersiveMode = false;
        public float hudOpacity = 0.7f;
        public boolean idleQuizEnabled = true;
        public int hudX = 10;
        public int hudY = 10;
    }

    public static boolean isSafeMode() {
        return data.safeMode;
    }

    public static void setSafeMode(boolean safeMode) {
        data.safeMode = safeMode;
        save();
    }

    public static boolean isImmersiveMode() {
        return data.immersiveMode;
    }

    public static void setImmersiveMode(boolean immersiveMode) {
        data.immersiveMode = immersiveMode;
        save();
    }

    public static float getHudOpacity() {
        return data.hudOpacity;
    }

    public static void setHudOpacity(float opacity) {
        data.hudOpacity = opacity;
        save();
    }

    public static boolean isIdleQuizEnabled() {
        return data.idleQuizEnabled;
    }

    public static void setIdleQuizEnabled(boolean enabled) {
        data.idleQuizEnabled = enabled;
        save();
    }

    public static int getHudX() {
        return data.hudX;
    }

    public static void setHudX(int x) {
        data.hudX = x;
        save();
    }

    public static int getHudY() {
        return data.hudY;
    }

    public static void setHudY(int y) {
        data.hudY = y;
        save();
    }
}
