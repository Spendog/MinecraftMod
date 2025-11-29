package com.example.educationmod.gui;

import com.example.educationmod.ModSettings;

import com.example.educationmod.PlayerStats;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

/**
 * LearningHUD - Movable learning box that displays facts and streak counter
 */
public class LearningHUD {

    private static LearningHUD INSTANCE;

    // Local state for content only
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
        // Immersive Mode: Show Proposals instead of generic facts
        if (ModSettings.isImmersiveMode()) {
            com.example.educationmod.immersive.ImmersiveProposalManager proposalManager = com.example.educationmod.immersive.ImmersiveProposalManager
                    .getInstance();
            com.example.educationmod.ModConfigManager.EventDefinition proposal = proposalManager.getCurrentProposal();

            if (proposal != null) {
                this.currentFact = "§bPROPOSAL: §f" + proposal.trigger + "\n§7Press [Y] to Accept, [N] to Reject";
            } else {
                this.currentFact = "§7Immersive Mode Active\n§8Waiting for significant events...";
            }
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

        // Get settings from ModSettings
        float opacity = ModSettings.getHudOpacity();
        int x = ModSettings.getHudX();
        int y = ModSettings.getHudY();

        // Safety check: Reset if off-screen or invalid
        if (x < 0 || y < 0 || x > client.getWindow().getScaledWidth() || y > client.getWindow().getScaledHeight()) {
            x = 10;
            y = 10;
            ModSettings.setHudX(10);
            ModSettings.setHudY(10);
        }

        // Ensure opacity is visible
        if (opacity < 0.1f) {
            opacity = 0.7f;
            ModSettings.setHudOpacity(0.7f);
        }

        int width = 200;
        int height = 60;

        // Calculate colors with opacity
        int bgColor = (int) (opacity * 255) << 24 | 0x000000;
        int borderColor = (int) (opacity * 255) << 24 | 0x555555;
        int textColor = 0xFFFFFFFF;

        // Draw current fact (with wrapping)
        String fact = currentFact;
        int maxWidth = width - 10;
        java.util.List<String> lines = client.textRenderer.wrapLines(net.minecraft.text.Text.literal(fact), maxWidth)
                .stream().map(net.minecraft.text.OrderedText::toString).collect(java.util.stream.Collectors.toList()); // This
                                                                                                                       // is
                                                                                                                       // a
                                                                                                                       // bit
                                                                                                                       // hacky
                                                                                                                       // for
                                                                                                                       // 1.21,
                                                                                                                       // but
                                                                                                                       // wrapLines
                                                                                                                       // returns
                                                                                                                       // List<OrderedText>

        // Adjust height based on lines
        int lineHeight = 10;
        height = 30 + (lines.size() * lineHeight);

        // Redraw background with new height
        context.fill(x, y, x + width, y + height, bgColor);
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

        // Draw lines
        int textY = y + 20;
        for (net.minecraft.text.OrderedText line : client.textRenderer.wrapLines(net.minecraft.text.Text.literal(fact),
                maxWidth)) {
            context.drawText(client.textRenderer, line, x + 5, textY, textColor, true);
            textY += lineHeight;
        }
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
}
