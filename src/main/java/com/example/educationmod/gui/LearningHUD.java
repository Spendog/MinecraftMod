package com.example.educationmod.gui;

import com.example.educationmod.PassiveLearningManager;
import com.example.educationmod.PlayerStats;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

/**
 * LearningHUD - Movable learning box that displays facts and streak counter
 */
public class LearningHUD {

    private static LearningHUD INSTANCE;

    // HUD Settings
    private boolean enabled = true;
    private int x = 10;
    private int y = 10;
    private int width = 200;
    private int height = 60;
    private float opacity = 0.7f;

    // Content
    private String currentFact = "Welcome to Immersive Learning!";
    private long lastFactUpdate = 0;

    private LearningHUD() {
    }

    public static LearningHUD getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LearningHUD();
        }
        return INSTANCE;
    }

    public static void init() {
        getInstance();

        // Register HUD renderer (Minecraft 1.21.5 uses RenderTickCounter)
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            getInstance().render(drawContext);
        });
    }

    public void render(DrawContext context) {
        if (!enabled) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return;
        }

        // Update fact periodically
        long now = System.currentTimeMillis();
        if (now - lastFactUpdate > 30000) {
            updateFact();
            lastFactUpdate = now;
        }

        // Calculate colors with opacity
        int bgColor = (int) (opacity * 255) << 24 | 0x000000;
        int borderColor = (int) (opacity * 255) << 24 | 0x555555;
        int textColor = 0xFFFFFFFF;

        // Draw background
        context.fill(x, y, x + width, y + height, bgColor);

        // Draw border
        context.fill(x, y, x + width, y + 1, borderColor);
        context.fill(x, y + height - 1, x + width, y + height, borderColor);
        context.fill(x, y, x + 1, y + height, borderColor);
        context.fill(x + width - 1, y, x + width, y + height, borderColor);

        // Draw title
        context.drawText(client.textRenderer, "§6Learning", x + 5, y + 5, textColor, true);

        // Draw streak counter
        int streak = getHighestStreak();
        String streakText = "§e⚡ " + streak;
        int streakWidth = client.textRenderer.getWidth(streakText);
        context.drawText(client.textRenderer, streakText, x + width - streakWidth - 5, y + 5, textColor, true);

        // Draw current fact (simple, no wrapping for now)
        context.drawText(client.textRenderer, currentFact.substring(0, Math.min(30, currentFact.length())) + "...",
                x + 5, y + 20, textColor, true);
    }

    private void updateFact() {
        String[] facts = {
                "Learning in context improves retention",
                "Spaced repetition builds long-term memory",
                "Active recall beats passive review"
        };
        currentFact = facts[(int) (System.currentTimeMillis() / 30000) % facts.length];
    }

    private int getHighestStreak() {
        PlayerStats stats = PlayerStats.getInstance();
        int maxStreak = 0;
        for (int streak : stats.topicStreaks.values()) {
            if (streak > maxStreak) {
                maxStreak = streak;
            }
        }
        return maxStreak;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setOpacity(float opacity) {
        this.opacity = Math.max(0.5f, Math.min(1.0f, opacity));
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void resetPosition() {
        this.x = 10;
        this.y = 10;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public float getOpacity() {
        return opacity;
    }
}
