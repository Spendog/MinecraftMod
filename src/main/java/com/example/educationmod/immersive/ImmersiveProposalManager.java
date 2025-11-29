package com.example.educationmod.immersive;

import com.example.educationmod.ModConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class ImmersiveProposalManager {
    private static ImmersiveProposalManager INSTANCE;
    private ModConfigManager.EventDefinition currentProposal;
    private long proposalTime;

    private ImmersiveProposalManager() {
    }

    public static ImmersiveProposalManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ImmersiveProposalManager();
        }
        return INSTANCE;
    }

    public void proposeRule(String triggerId, String context) {
        // Only propose if no proposal is active (or replace old one)
        if (currentProposal != null && System.currentTimeMillis() - proposalTime < 10000) {
            return; // Busy
        }

        ModConfigManager.EventDefinition proposal = new ModConfigManager.EventDefinition();
        proposal.trigger = triggerId;
        proposal.condition = "NONE"; // Default
        proposal.action = new ModConfigManager.ActionDefinition();

        // Logic: If new context, propose opening a Course
        // If existing context, propose adding a Chapter
        // For prototype, we'll default to OPEN_COURSE
        proposal.action.type = "OPEN_COURSE";
        proposal.action.data = "course_" + context.toLowerCase() + ".json";

        this.currentProposal = proposal;
        this.proposalTime = System.currentTimeMillis();

        // Notify User
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal("§b[Immersive] §fNew Subject Detected: §e" + context)
                    .append(Text.literal("\n§7Action: Start Course '" + context + "'?"))
                    .append(Text.literal("\n§a[Accept] §c[Reject]")), false);
        }
    }

    public void acceptProposal() {
        if (currentProposal != null) {
            ModConfigManager.saveEvent(currentProposal);
            MinecraftClient.getInstance().player.sendMessage(Text.literal("§aRule Saved!"), true);
            currentProposal = null;
        }
    }

    public void rejectProposal() {
        if (currentProposal != null) {
            MinecraftClient.getInstance().player.sendMessage(Text.literal("§cRule Rejected."), true);
            currentProposal = null;
        }
    }

    public ModConfigManager.EventDefinition getCurrentProposal() {
        // Expire after 15 seconds
        if (System.currentTimeMillis() - proposalTime > 15000) {
            currentProposal = null;
        }
        return currentProposal;
    }
}
