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
            this.client.setScreen(new ItemSelectionScreen(this, selectedId -> {
                // For now, just log it or add a dummy event
                // In a real app, we'd open a full configuration screen for the trigger
                // Here we just add a dummy BLOCK_BREAK trigger for the selected item
                ModConfigManager.EventDefinition newEvent = new ModConfigManager.EventDefinition();
                newEvent.trigger = "BLOCK_BREAK";
                newEvent.condition = selectedId;
                newEvent.action = new ModConfigManager.ActionDefinition();
                newEvent.action.type = "QUIZ";
                newEvent.action.data = "example_quiz.json";

                ModConfigManager.getEvents().add(newEvent);
                this.client.setScreen(this); // Return to this screen
            }));
        }).dimensions(center - 100, 30, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Gradient Background
        context.fillGradient(0, 0, this.width, this.height, 0xFF100010, 0xFF300030);

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

                // Edit Button (Placeholder)
                context.drawText(this.textRenderer, "[Edit]", textCenterX + 120, textY, 0xFFFF55, false);

                y += 15;
                textY = (int) y;
                context.drawText(this.textRenderer, actionText, textCenterX - 140, textY, 0xAAFFAA, false);
                y += 25;
            }
        }

        super.render(context, mouseX, mouseY, delta);
    }
}
