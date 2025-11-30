package com.example.educationmod.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
// import net.minecraft.text.Text; // Unused

import java.util.ArrayList;
import java.util.List;

public class ContextManager {
    private static final ContextManager INSTANCE = new ContextManager();
    private ContextType currentContext = ContextType.VANILLA;
    private int tickCounter = 0;
    private final List<ContextListener> listeners = new ArrayList<>();

    public enum ContextType {
        VANILLA,
        SKYBLOCK,
        DUNGEON
    }

    public interface ContextListener {
        void onContextChanged(ContextType newContext);
    }

    public static ContextManager getInstance() {
        return INSTANCE;
    }

    public void addListener(ContextListener listener) {
        listeners.add(listener);
    }

    public ContextType getCurrentContext() {
        return currentContext;
    }

    public void tick(MinecraftClient client) {
        tickCounter++;
        if (tickCounter >= 20) { // Check every second
            tickCounter = 0;
            detectContext(client);
        }
    }

    private void detectContext(MinecraftClient client) {
        if (client.world == null || client.player == null)
            return;

        ContextType newContext = ContextType.VANILLA;

        // Check Scoreboard for Hypixel-specific titles
        Scoreboard scoreboard = client.world.getScoreboard();
        for (ScoreboardObjective objective : scoreboard.getObjectives()) {
            String title = objective.getDisplayName().getString().toLowerCase();
            if (title.contains("skyblock")) {
                newContext = ContextType.SKYBLOCK;
            }
            if (title.contains("catacombs") || title.contains("dungeon")) {
                newContext = ContextType.DUNGEON;
            }
        }

        if (newContext != currentContext) {
            currentContext = newContext;
            notifyListeners(newContext);
            System.out.println("Context Changed: " + newContext);
        }
    }

    private void notifyListeners(ContextType newContext) {
        for (ContextListener listener : listeners) {
            listener.onContextChanged(newContext);
        }
    }
}
