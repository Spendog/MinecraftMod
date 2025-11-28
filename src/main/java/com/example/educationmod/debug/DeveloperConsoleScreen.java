package com.example.educationmod.debug;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class DeveloperConsoleScreen extends Screen {
    private final DeveloperConsole console;
    private final Screen parent;

    public DeveloperConsoleScreen(Screen parent) {
        super(Text.literal("Developer Console"));
        this.console = DeveloperConsole.getInstance();
        this.parent = parent;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render parent screen first (if exists) to simulate overlay
        if (parent != null) {
            parent.render(context, mouseX, mouseY, delta);
            // Darken background slightly to make console pop
            context.fillGradient(0, 0, this.width, this.height, 0x88000000, 0x88000000);
        } else {
            // Default background if no parent
            this.renderBackground(context, mouseX, mouseY, delta);
        }

        // Render the console logic
        console.render(context, this.width, this.height);
    }

    @Override
    public void close() {
        if (parent != null) {
            this.client.setScreen(parent);
        } else {
            super.close();
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Pass to console logic
        if (console.keyPressed(keyCode, scanCode, modifiers)) {
            // If console processed it (e.g. toggle closed), check visibility
            if (!console.isVisible()) {
                this.close();
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (console.charTyped(chr, modifiers)) {
            return true;
        }
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
