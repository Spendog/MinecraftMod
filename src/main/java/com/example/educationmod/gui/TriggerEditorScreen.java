package com.example.educationmod.gui;

import com.example.educationmod.ModConfigManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;

public class TriggerEditorScreen extends Screen {
    private final Screen parent;
    private List<ModConfigManager.EventDefinition> events;

    public TriggerEditorScreen(Screen parent) {
        super(Text.literal("Trigger Editor"));
        this.parent = parent;
        this.events = ModConfigManager.getEvents();
        if (this.events == null) {
            this.events = new java.util.ArrayList<>();
            com.example.educationmod.ActivityLogger.logError("Failed to load events: List was null", null);
        }
    }

    @Override
    protected void init() {
        int center = this.width / 2;

        // Close Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("X"), button -> {
            this.close();
        }).dimensions(this.width - 30, 10, 20, 20).build());

        // Back Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), button -> this.client.setScreen(parent))
                .dimensions(center - 100, this.height - 40, 200, 20).build());

        // Add Trigger Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("+ Add Trigger"), button -> {
            ModConfigManager.EventDefinition newEvent = new ModConfigManager.EventDefinition();
            newEvent.trigger = "BLOCK_BREAK";
            newEvent.action = new ModConfigManager.ActionDefinition();
            newEvent.action.type = "QUIZ";
            this.client.setScreen(new TriggerConfigScreen(this, newEvent));
        }).dimensions(center - 100, 30, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Use Z-translation to ensure background draws over everything (fixing "blur"
        // artifacts)
        context.getMatrices().push();
        context.getMatrices().translate(0, 0, 500); // Move forward in Z
        context.fill(0, 0, this.width, this.height, 0xFF100010); // Solid dark purple/black
        context.getMatrices().pop();

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);

        int y = 60;
        int center = this.width / 2;

        if (events.isEmpty()) {
            context.drawCenteredTextWithShadow(this.textRenderer, "No triggers configured.", center, y, 0xAAAAAA);
        } else {
            for (ModConfigManager.EventDefinition event : events) {
                String triggerText = "Trigger: " + event.trigger;
                String conditionText = "Condition: " + (event.condition != null ? event.condition : "None");
                String actionText = "Action: " + event.action.type + " (" + event.action.data + ")";

                // Force integer coordinates to prevent sub-pixel blurring
                int textY = (int) y;
                int textCenterX = (int) center;

                context.drawText(this.textRenderer, triggerText, textCenterX - 150, textY, 0xFFAAAA, false);
                context.drawText(this.textRenderer, conditionText, textCenterX + 10, textY, 0xAAAAAA, false);

                // Edit Button (Clickable area check)
                // We can't easily add buttons in a loop in render(), so we'll check clicks in
                // mouseClicked
                // For now, just draw the text
                context.drawText(this.textRenderer, "[Edit]", textCenterX + 120, textY, 0xFFFF55, false);
                context.drawText(this.textRenderer, "[Delete]", textCenterX + 160, textY, 0xFF5555, false);

                y += 15;
                textY = (int) y;
                context.drawText(this.textRenderer, actionText, textCenterX - 140, textY, 0xAAFFAA, false);
                y += 25;
            }
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button))
            return true;

        int y = 60;
        int center = this.width / 2;

        // Simple hit detection for the list
        for (int i = 0; i < events.size(); i++) {
            ModConfigManager.EventDefinition event = events.get(i);
            int textY = y;
            int textCenterX = center;

            // Edit Button: x ~ center + 120, width ~ 30
            if (mouseY >= textY && mouseY <= textY + 10) {
                if (mouseX >= textCenterX + 120 && mouseX <= textCenterX + 150) {
                    this.client.setScreen(new TriggerConfigScreen(this, event));
                    return true;
                }
                // Delete Button: x ~ center + 160, width ~ 40
                if (mouseX >= textCenterX + 160 && mouseX <= textCenterX + 200) {
                    ModConfigManager.deleteEvent(i);
                    com.example.educationmod.ActivityLogger.log("Deleted trigger: " + event.trigger);
                    // Refresh list
                    this.events = ModConfigManager.getEvents();
                    return true;
                }
            }
            y += 40; // 15 + 25
        }
        return false;
    }
}
