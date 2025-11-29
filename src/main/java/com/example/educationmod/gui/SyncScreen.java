package com.example.educationmod.gui;

import com.example.educationmod.ModConfigManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;

public class SyncScreen extends Screen {
    private final Screen parent;
    private CheckboxWidget eventsCheckbox;
    private CheckboxWidget topicsCheckbox;
    private CheckboxWidget coursesCheckbox;

    public SyncScreen(Screen parent) {
        super(Text.literal("Sync Library"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        int y = this.height / 4;

        this.eventsCheckbox = CheckboxWidget.builder(Text.literal("Sync Events"), this.textRenderer)
                .pos(center - 50, y)
                .checked(true)
                .build();
        this.addDrawableChild(eventsCheckbox);

        this.topicsCheckbox = CheckboxWidget.builder(Text.literal("Sync Topics"), this.textRenderer)
                .pos(center - 50, y + 25)
                .checked(true)
                .build();
        this.addDrawableChild(topicsCheckbox);

        this.coursesCheckbox = CheckboxWidget.builder(Text.literal("Sync Courses"), this.textRenderer)
                .pos(center - 50, y + 50)
                .checked(true)
                .build();
        this.addDrawableChild(coursesCheckbox);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Sync Selected"), button -> {
            ModConfigManager.importLibrary(
                    eventsCheckbox.isChecked(),
                    topicsCheckbox.isChecked(),
                    coursesCheckbox.isChecked());
            this.client.setScreen(parent);
        }).dimensions(center - 100, y + 85, 200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Cancel"), button -> {
            this.client.setScreen(parent);
        }).dimensions(center - 100, y + 110, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
