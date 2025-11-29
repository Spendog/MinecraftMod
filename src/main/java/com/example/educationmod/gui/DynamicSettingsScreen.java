package com.example.educationmod.gui;

import com.example.educationmod.config.ConfigManager;
import com.example.educationmod.config.ConfigSchema;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class DynamicSettingsScreen extends Screen {
    private final Screen parent;
    private final Map<String, TextFieldWidget> textFields = new HashMap<>();

    public DynamicSettingsScreen(Screen parent) {
        super(Text.literal("Mod Settings (Dynamic)"));
        this.parent = parent;
        if (ConfigManager.getOptions().isEmpty()) {
            ConfigManager.init();
        }
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        int y = 40;

        for (ConfigSchema<?> option : ConfigManager.getOptions()) {
            int finalY = y;
            if (option.type == ConfigSchema.ConfigType.BOOLEAN) {
                ConfigSchema<Boolean> boolOption = (ConfigSchema<Boolean>) option;
                this.addDrawableChild(ButtonWidget.builder(
                        Text.literal(option.label + ": " + (boolOption.getter.get() ? "ON" : "OFF")),
                        button -> {
                            if ("Safe Mode".equals(option.label) && boolOption.getter.get()) {
                                this.client.setScreen(new net.minecraft.client.gui.screen.ConfirmScreen(
                                        (confirmed) -> {
                                            if (confirmed) {
                                                boolOption.setter.accept(false);
                                            }
                                            this.client.setScreen(this);
                                        },
                                        Text.literal("Disable Safe Mode?"),
                                        Text.literal("Are you sure? This will allow automated commands.")));
                            } else {
                                boolean newState = !boolOption.getter.get();
                                boolOption.setter.accept(newState);
                                button.setMessage(Text.literal(option.label + ": " + (newState ? "ON" : "OFF")));
                            }
                        }).dimensions(center - 100, y, 200, 20).build());
            } else {
                // Text Field for Numbers/Strings
                TextFieldWidget field = new TextFieldWidget(this.textRenderer, center - 100, y, 200, 20,
                        Text.literal(option.label));
                field.setMaxLength(256);
                field.setText(String.valueOf(option.getter.get()));
                field.setChangedListener(text -> {
                    try {
                        if (option.type == ConfigSchema.ConfigType.INTEGER) {
                            int val = Integer.parseInt(text);
                            ((ConfigSchema<Integer>) option).setter.accept(val);
                        } else if (option.type == ConfigSchema.ConfigType.FLOAT) {
                            float val = Float.parseFloat(text);
                            ((ConfigSchema<Float>) option).setter.accept(val);
                        } else if (option.type == ConfigSchema.ConfigType.STRING) {
                            ((ConfigSchema<String>) option).setter.accept(text);
                        }
                    } catch (NumberFormatException e) {
                        // Ignore invalid input
                    }
                });
                this.addDrawableChild(field);
                this.textFields.put(option.key, field);

                // Label for the text field
                // We'll draw this in render()
            }
            y += 25;
        }

        // Back Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), button -> {
            this.client.setScreen(parent);
        }).dimensions(center - 100, this.height - 40, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Solid Background to prevent "blur" issues
        // this.renderBackground(context, mouseX, mouseY, delta); // Removed to prevent
        // blur
        context.fill(0, 0, this.width, this.height, 0xFF000000); // Solid black

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);

        // Draw labels for text fields
        int y = 40;
        int center = this.width / 2;
        for (ConfigSchema<?> option : ConfigManager.getOptions()) {
            if (option.type != ConfigSchema.ConfigType.BOOLEAN) {
                context.drawTextWithShadow(this.textRenderer, option.label, center - 210, y + 6, 0xAAAAAA);
            }
            y += 25;
        }

        super.render(context, mouseX, mouseY, delta);
    }
}
