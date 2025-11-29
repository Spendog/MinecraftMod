package com.example.educationmod.gui;

import com.example.educationmod.ModConfigManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class TriggerConfigScreen extends Screen {
    private final Screen parent;
    private final ModConfigManager.EventDefinition event;

    private String currentTrigger;
    private String currentCondition;
    private String currentActionType;
    private String currentActionData;

    private final String[] TRIGGER_TYPES = { "BLOCK_BREAK", "ITEM_USE", "ENTITY_KILL", "DIMENSION_CHANGE" };
    private final String[] ACTION_TYPES = { "QUIZ", "TEXT", "COMMAND", "REWARD" };

    public TriggerConfigScreen(Screen parent, ModConfigManager.EventDefinition event) {
        super(Text.literal("Edit Trigger"));
        this.parent = parent;
        this.event = event;

        if (this.event.action == null) {
            this.event.action = new ModConfigManager.ActionDefinition();
        }

        this.currentTrigger = event.trigger != null ? event.trigger : TRIGGER_TYPES[0];
        this.currentCondition = event.condition != null ? event.condition : "minecraft:dirt";
        this.currentActionType = event.action.type != null ? event.action.type : ACTION_TYPES[0];
        this.currentActionData = event.action.data != null ? event.action.data : "example_quiz.json";
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        int y = 40;

        // Trigger Type Cycle Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Trigger: " + currentTrigger), button -> {
            currentTrigger = cycleValue(currentTrigger, TRIGGER_TYPES);
            button.setMessage(Text.literal("Trigger: " + currentTrigger));
        }).dimensions(center - 100, y, 200, 20).build());
        y += 40;

        // Condition Label & Select Button
        // We render the label in render(), just add the button here
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Select Condition Item"), button -> {
            this.client.setScreen(new ItemSelectionScreen(this, selectedId -> {
                this.currentCondition = selectedId;
                this.client.setScreen(this);
            }));
        }).dimensions(center - 100, y, 200, 20).build());
        y += 40;

        // Action Type Cycle Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Action: " + currentActionType), button -> {
            currentActionType = cycleValue(currentActionType, ACTION_TYPES);
            button.setMessage(Text.literal("Action: " + currentActionType));
        }).dimensions(center - 100, y, 200, 20).build());
        y += 40;

        // Action Data Label & Select Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Select Action File"), button -> {
            this.client.setScreen(new FileSelectionScreen(this, selectedFile -> {
                this.currentActionData = selectedFile;
                this.client.setScreen(this);
            }));
        }).dimensions(center - 100, y, 200, 20).build());
        y += 40;

        // Save Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Save"), button -> {
            save();
            this.client.setScreen(parent);
        }).dimensions(center - 105, this.height - 40, 100, 20).build());

        // Cancel Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Cancel"), button -> {
            this.client.setScreen(parent);
        }).dimensions(center + 5, this.height - 40, 100, 20).build());
    }

    private String cycleValue(String current, String[] values) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(current)) {
                return values[(i + 1) % values.length];
            }
        }
        return values[0];
    }

    private void save() {
        event.trigger = currentTrigger;
        event.condition = currentCondition;
        event.action.type = currentActionType;
        event.action.data = currentActionData;

        ModConfigManager.saveEvent(event);
        com.example.educationmod.ActivityLogger.log("Saved trigger: " + event.trigger + " -> " + event.action.type);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Use Z-translation to ensure background draws over everything
        context.getMatrices().push();
        context.getMatrices().translate(0, 0, 500); // Move forward in Z
        context.fill(0, 0, this.width, this.height, 0xFF100010); // Solid dark purple/black
        context.getMatrices().pop();

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);

        int center = this.width / 2;
        int y = 40;

        // Trigger Type (Button is here)
        y += 40;

        // Condition Label
        context.drawCenteredTextWithShadow(this.textRenderer, "Condition: " + currentCondition, center, y - 12,
                0xAAAAAA);
        y += 40;

        // Action Type (Button is here)
        y += 40;

        // Action Data Label
        context.drawCenteredTextWithShadow(this.textRenderer, "Data: " + currentActionData, center, y - 12, 0xAAAAAA);

        super.render(context, mouseX, mouseY, delta);
    }
}
