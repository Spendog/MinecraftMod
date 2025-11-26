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

        // Add Trigger Button (Placeholder for now)
        this.addDrawableChild(ButtonWidget.builder(Text.literal("+ Add Trigger"), button -> {
            // Future: Open Trigger Creation Screen
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

                context.drawTextWithShadow(this.textRenderer, triggerText, center - 150, y, 0xFFAAAA);
                context.drawTextWithShadow(this.textRenderer, conditionText, center + 10, y, 0xAAAAAA);

                // Edit Button (Placeholder)
                // Note: We can't easily add buttons in render(), so we'd normally do this in
                // init() with a scroll list.
                // For this prototype, we'll just show the text. To make it interactive, we need
                // a proper list widget.
                // For now, let's just render a "fake" button text or similar to indicate
                // intent.
                context.drawTextWithShadow(this.textRenderer, "[Edit]", center + 120, y, 0xFFFF55);

                y += 15;
                context.drawTextWithShadow(this.textRenderer, actionText, center - 140, y, 0xAAFFAA);
                y += 25;
            }
        }

        super.render(context, mouseX, mouseY, delta);
    }
}
