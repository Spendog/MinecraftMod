package com.example.educationmod.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ItemSelectionScreen extends Screen {
    private final Screen parent;
    private final Consumer<String> onSelect;
    private TextFieldWidget searchField;
    private List<Item> allItems;
    private List<Item> filteredItems;
    private int scrollOffset = 0;

    public ItemSelectionScreen(Screen parent, Consumer<String> onSelect) {
        super(Text.literal("Select Item"));
        this.parent = parent;
        this.onSelect = onSelect;
        this.allItems = new ArrayList<>();
        Registries.ITEM.forEach(allItems::add);
        this.filteredItems = new ArrayList<>(allItems);
    }

    @Override
    protected void init() {
        int center = this.width / 2;

        // Search Field
        this.searchField = new TextFieldWidget(this.textRenderer, center - 100, 40, 200, 20, Text.literal("Search"));
        this.searchField.setChangedListener(this::updateFilter);
        this.addDrawableChild(searchField);

        // Back Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Cancel"), button -> this.client.setScreen(parent))
                .dimensions(center - 100, this.height - 30, 200, 20).build());
    }

    private void updateFilter(String query) {
        if (query.isEmpty()) {
            filteredItems = new ArrayList<>(allItems);
        } else {
            String lowerQuery = query.toLowerCase();
            filteredItems = allItems.stream()
                    .filter(item -> item.getName().getString().toLowerCase().contains(lowerQuery) ||
                            Registries.ITEM.getId(item).toString().contains(lowerQuery))
                    .collect(Collectors.toList());
        }
        scrollOffset = 0;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollOffset = Math.max(0, scrollOffset - (int) (verticalAmount * 20));
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button))
            return true;

        // Handle item clicks
        int x = this.width / 2 - 100;
        int y = 70 - scrollOffset;

        for (Item item : filteredItems) {
            if (y > 60 && y < this.height - 40) {
                if (mouseX >= x && mouseX <= x + 200 && mouseY >= y && mouseY <= y + 20) {
                    onSelect.accept(Registries.ITEM.getId(item).toString());
                    this.close();
                    return true;
                }
            }
            y += 22;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);

        // Render List
        int x = this.width / 2 - 100;
        int y = 70 - scrollOffset;

        // Scissor test would be better here, but for prototype we just don't draw out
        // of bounds
        for (Item item : filteredItems) {
            if (y > 60 && y < this.height - 40) {
                String name = item.getName().getString();
                boolean hovered = mouseX >= x && mouseX <= x + 200 && mouseY >= y && mouseY <= y + 20;
                int color = hovered ? 0xFFFFFF : 0xAAAAAA;

                context.fill(x, y, x + 200, y + 20, 0xFF000000);
                context.drawBorder(x, y, 200, 20, hovered ? 0xFFFFFF00 : 0xFF555555);
                context.drawItem(item.getDefaultStack(), x + 2, y + 2);
                context.drawText(this.textRenderer, name, x + 25, y + 6, color, false);
            }
            y += 22;
        }

        super.render(context, mouseX, mouseY, delta);
    }
}
