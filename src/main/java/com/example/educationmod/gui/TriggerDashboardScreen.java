package com.example.educationmod.gui;

import com.example.educationmod.ModConfigManager;
import com.example.educationmod.ModSettings;
import com.example.educationmod.registries.ActionRegistry;
import com.example.educationmod.registries.ConditionRegistry;
import com.example.educationmod.registries.TriggerRegistry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TriggerDashboardScreen extends Screen {
    private final Screen parent;
    private TextFieldWidget searchField;
    private TextFieldWidget nameField;
    private TextFieldWidget skyblockIdField;
    private List<Object> filteredItems = new ArrayList<>();
    private int selectedSlot = 0; // 0=Trigger, 1=Target (Condition), 2=Requirement, 3=Action
    private TriggerRegistry.TriggerType selectedTrigger;
    private ConditionRegistry.ConditionType selectedTarget;
    private com.example.educationmod.registries.RequirementRegistry.RequirementType selectedRequirement;
    private ActionRegistry.ActionType selectedAction;
    private String targetValue = "";
    private String requirementData = "";
    private String actionData = "";
    private ItemStack requirementIconOverride = ItemStack.EMPTY;

    public TriggerDashboardScreen(Screen parent) {
        super(Text.literal("EDU MC Dashboard"));
        this.parent = parent;
        refreshList("");
    }

    // Called by REI Plugin
    public void handleRequirementDrop(String itemId, ItemStack stack) {
        this.selectedRequirement = com.example.educationmod.registries.RequirementRegistry.get("HOLDING_ITEM");
        this.requirementData = itemId;
        this.requirementIconOverride = stack;
        this.selectedSlot = 2; // Focus Requirement slot
        com.example.educationmod.ActivityLogger.log("Requirement set to Holding: " + itemId);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        int topBarHeight = 30;

        // --- Navigation Bar (Top) ---
        int navY = 5;
        int navBtnWidth = 60;
        int navSpacing = 5;
        int navStart = 10;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Courses"), button -> {
            this.client.setScreen(new BookScreen(this));
        }).dimensions(navStart, navY, navBtnWidth, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Settings"), button -> {
            this.client.setScreen(new DynamicSettingsScreen(this));
        }).dimensions(navStart + (navBtnWidth + navSpacing), navY, navBtnWidth, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Logs"), button -> {
            this.client.setScreen(new BookScreen(this, com.example.educationmod.ActivityLogger.getLogFilePath()));
        }).dimensions(navStart + (navBtnWidth + navSpacing) * 2, navY, navBtnWidth, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Analytics"), button -> {
            this.client.setScreen(new DashboardScreen(this));
        }).dimensions(navStart + (navBtnWidth + navSpacing) * 3, navY, navBtnWidth, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Sync"), button -> {
            this.client.setScreen(new SyncScreen(this));
        }).dimensions(navStart + (navBtnWidth + navSpacing) * 4, navY, 40, 20).build());

        // Safe Mode Indicator (Read-Only)
        this.addDrawableChild(
                ButtonWidget.builder(Text.literal(ModSettings.isSafeMode() ? "Safe: ON" : "Safe: OFF"), button -> {
                }).dimensions(this.width - 100, navY, 60, 20).build()).active = false;

        // --- Dashboard Controls ---

        // Name Field
        this.nameField = new TextFieldWidget(this.textRenderer, center - 160, topBarHeight + 10, 100, 20,
                Text.literal("Name"));
        this.nameField.setPlaceholder(Text.literal("Event Name"));
        this.addDrawableChild(nameField);

        // Search Bar
        this.searchField = new TextFieldWidget(this.textRenderer, center - 50, topBarHeight + 10, 100, 20,
                Text.literal("Search"));
        this.searchField.setChangedListener(this::refreshList);
        this.addDrawableChild(searchField);

        // Skyblock ID Field
        this.skyblockIdField = new TextFieldWidget(this.textRenderer, center + 60, topBarHeight + 10, 80, 20,
                Text.literal("SB ID"));
        this.skyblockIdField.setPlaceholder(Text.literal("Skyblock ID"));
        this.skyblockIdField.setVisible(selectedTrigger != null && "SKYBLOCK_ITEM_USE".equals(selectedTrigger.id));
        this.addDrawableChild(skyblockIdField);

        // Save Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Save Event"), button -> {
            saveEvent();
        }).dimensions(this.width - 120, this.height - 30, 100, 20).build());

        // Close Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("X"), button -> {
            this.close();
        }).dimensions(this.width - 30, 5, 20, 20).build());
    }

    private void refreshList(String query) {
        filteredItems.clear();
        String q = query.toLowerCase();

        if (selectedSlot == 0) {
            filteredItems.addAll(TriggerRegistry.getAll().stream()
                    .filter(t -> t.displayName.toLowerCase().contains(q))
                    .collect(Collectors.toList()));
        } else if (selectedSlot == 1) {
            filteredItems.addAll(ConditionRegistry.getAll().stream()
                    .filter(c -> c.displayName.toLowerCase().contains(q))
                    .collect(Collectors.toList()));
        } else if (selectedSlot == 2) {
            filteredItems.addAll(com.example.educationmod.registries.RequirementRegistry.getAll().stream()
                    .filter(r -> r.displayName.toLowerCase().contains(q))
                    .collect(Collectors.toList()));
        } else if (selectedSlot == 3) {
            filteredItems.addAll(ActionRegistry.getAll().stream()
                    .filter(a -> a.displayName.toLowerCase().contains(q))
                    .collect(Collectors.toList()));
        }
    }

    private void saveEvent() {
        if (selectedTrigger != null && selectedAction != null) {
            ModConfigManager.EventDefinition event = new ModConfigManager.EventDefinition();
            event.trigger = selectedTrigger.id;
            event.condition = (selectedTarget != null) ? selectedTarget.id : "NONE";
            event.requirement = (selectedRequirement != null) ? selectedRequirement.id : "NONE";
            event.requirementData = requirementData;

            if ("SKYBLOCK_ITEM_USE".equals(selectedTrigger.id) && !skyblockIdField.getText().isEmpty()) {
                event.condition = skyblockIdField.getText();
            } else if (!targetValue.isEmpty() && !"NONE".equals(event.condition)) {
                event.condition = targetValue;
            }

            event.action = new ModConfigManager.ActionDefinition();
            event.action.type = selectedAction.id;
            event.action.data = actionData.isEmpty() ? "example_quiz.json" : actionData;

            if (!nameField.getText().isEmpty()) {
                event.name = nameField.getText();
            }

            ModConfigManager.saveEvent(event);
            com.example.educationmod.ActivityLogger.log("Created new event: " + event.trigger);

            selectedTrigger = null;
            selectedTarget = null;
            selectedRequirement = null;
            selectedAction = null;
            requirementData = "";
            requirementIconOverride = ItemStack.EMPTY;
            skyblockIdField.setText("");
            skyblockIdField.setVisible(false);
            nameField.setText("");

            selectedSlot = 0;
            refreshList(searchField.getText());
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        List<ModConfigManager.EventDefinition> events = ModConfigManager.getEvents();
        if (events != null && mouseX < 150 && mouseY > 30) {
            int eventY = 60;
            for (int i = 0; i < events.size(); i++) {
                ModConfigManager.EventDefinition ev = events.get(i);

                if (mouseY >= eventY && mouseY <= eventY + 20) {
                    if (mouseX >= 110 && mouseX <= 122) {
                        this.selectedTrigger = TriggerRegistry.get(ev.trigger);
                        this.selectedTarget = ConditionRegistry.get(ev.condition);
                        this.selectedRequirement = com.example.educationmod.registries.RequirementRegistry
                                .get(ev.requirement);
                        this.selectedAction = ActionRegistry.get(ev.action.type);
                        this.actionData = ev.action.data != null ? ev.action.data : "";
                        this.requirementData = ev.requirementData != null ? ev.requirementData : "";
                        this.nameField.setText(ev.name != null ? ev.name : "");

                        if (this.requirementData != null && !this.requirementData.isEmpty()) {
                            net.minecraft.registry.RegistryKey<net.minecraft.item.Item> key = net.minecraft.registry.RegistryKey
                                    .of(net.minecraft.registry.RegistryKeys.ITEM,
                                            net.minecraft.util.Identifier.of(this.requirementData));
                            this.requirementIconOverride = new ItemStack(
                                    net.minecraft.registry.Registries.ITEM.get(key));
                        } else {
                            this.requirementIconOverride = ItemStack.EMPTY;
                        }

                        boolean isSkyblock = "SKYBLOCK_ITEM_USE".equals(ev.trigger);
                        this.skyblockIdField.setVisible(isSkyblock);
                        if (isSkyblock) {
                            this.skyblockIdField.setText(ev.condition);
                        } else {
                            this.skyblockIdField.setText("");
                        }
                        return true;
                    }

                    if (mouseX >= 130 && mouseX <= 142) {
                        ModConfigManager.deleteEvent(i);
                        com.example.educationmod.ActivityLogger.log("Deleted event: " + ev.trigger);
                        return true;
                    }
                }
                eventY += 20;
            }
        }

        int sidebarWidth = 100;
        int center = this.width / 2 + (sidebarWidth / 2);
        int slotY = 80;
        int slotSize = 50;
        int spacing = 10;
        int startX = center - 130;

        if (isHovering(startX, slotY, slotSize, slotSize, mouseX, mouseY)) {
            selectedSlot = 0;
            refreshList(searchField.getText());
        }
        if (isHovering(startX + slotSize + spacing, slotY, slotSize, slotSize, mouseX, mouseY)) {
            selectedSlot = 1;
            refreshList(searchField.getText());
        }
        if (isHovering(startX + (slotSize + spacing) * 2, slotY, slotSize, slotSize, mouseX, mouseY)) {
            selectedSlot = 2;
            refreshList(searchField.getText());
        }
        if (isHovering(startX + (slotSize + spacing) * 3, slotY, slotSize, slotSize, mouseX, mouseY)) {
            selectedSlot = 3;
            refreshList(searchField.getText());
        }

        int x = center - 150;
        int y = 160;
        int itemSize = 40;
        int columns = 6;

        for (int i = 0; i < filteredItems.size(); i++) {
            Object item = filteredItems.get(i);
            int col = i % columns;
            int row = i / columns;
            int itemX = x + col * (itemSize + 5);
            int itemY = y + row * (itemSize + 5);

            if (isHovering(itemX, itemY, itemSize, itemSize, mouseX, mouseY)) {
                if (selectedSlot == 0 && item instanceof TriggerRegistry.TriggerType) {
                    selectedTrigger = (TriggerRegistry.TriggerType) item;
                    skyblockIdField.setVisible("SKYBLOCK_ITEM_USE".equals(selectedTrigger.id));
                    selectedSlot = 1;
                    refreshList(searchField.getText());
                } else if (selectedSlot == 1 && item instanceof ConditionRegistry.ConditionType) {
                    selectedTarget = (ConditionRegistry.ConditionType) item;
                    selectedSlot = 2;
                    refreshList(searchField.getText());
                } else if (selectedSlot == 2
                        && item instanceof com.example.educationmod.registries.RequirementRegistry.RequirementType) {
                    selectedRequirement = (com.example.educationmod.registries.RequirementRegistry.RequirementType) item;
                    requirementData = "";
                    requirementIconOverride = ItemStack.EMPTY;
                    selectedSlot = 3;
                    refreshList(searchField.getText());
                } else if (selectedSlot == 3 && item instanceof ActionRegistry.ActionType) {
                    selectedAction = (ActionRegistry.ActionType) item;
                }
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean isHovering(int x, int y, int w, int h, double mx, double my) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xFF050505);

        context.fill(0, 0, this.width, 30, 0xFF202020);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);

        int sidebarWidth = 150;
        context.fill(0, 30, sidebarWidth, this.height, 0xFF151515);
        context.drawText(this.textRenderer, "My Events", 10, 40, 0xFFFFFF, false);

        List<ModConfigManager.EventDefinition> events = ModConfigManager.getEvents();
        if (events != null) {
            int eventY = 60;
            for (ModConfigManager.EventDefinition ev : events) {
                String label = (ev.name != null && !ev.name.isEmpty()) ? ev.name : ev.trigger;
                if (label.length() > 12)
                    label = label.substring(0, 10) + "..";

                context.drawText(this.textRenderer, label, 10, eventY, 0xFFFFFF, false);

                int editBtnX = 110;
                int btnSize = 12;
                boolean hoverEdit = mouseX >= editBtnX && mouseX <= editBtnX + btnSize && mouseY >= eventY
                        && mouseY <= eventY + btnSize;
                context.fill(editBtnX, eventY, editBtnX + btnSize, eventY + btnSize,
                        hoverEdit ? 0xFF55FF55 : 0xFF333333);
                context.drawText(this.textRenderer, "E", editBtnX + 3, eventY + 2, 0xFFFFFF, false);

                int delBtnX = 130;
                boolean hoverDel = mouseX >= delBtnX && mouseX <= delBtnX + btnSize && mouseY >= eventY
                        && mouseY <= eventY + btnSize;
                context.fill(delBtnX, eventY, delBtnX + btnSize, eventY + btnSize,
                        hoverDel ? 0xFFFF5555 : 0xFF333333);
                context.drawText(this.textRenderer, "X", delBtnX + 3, eventY + 2, 0xFFFFFF, false);

                eventY += 20;
            }
        }

        int center = this.width / 2 + (sidebarWidth / 2);
        int slotY = 80;
        int slotSize = 50;
        int spacing = 10;
        int startX = center - 130;

        drawSlot(context, startX, slotY, slotSize, selectedTrigger != null ? selectedTrigger.displayName : "Trigger",
                selectedTrigger != null ? selectedTrigger.icon : ItemStack.EMPTY, selectedSlot == 0);
        context.drawText(this.textRenderer, "+", startX + slotSize + 2, slotY + 20, 0xFFFFFF, false);

        drawSlot(context, startX + slotSize + spacing, slotY, slotSize,
                selectedTarget != null ? selectedTarget.displayName : "Target",
                selectedTarget != null ? selectedTarget.icon : ItemStack.EMPTY, selectedSlot == 1);
        context.drawText(this.textRenderer, "+", startX + (slotSize + spacing) * 2 - 8, slotY + 20, 0xFFFFFF, false);

        ItemStack reqIcon = !requirementIconOverride.isEmpty() ? requirementIconOverride
                : (selectedRequirement != null ? selectedRequirement.icon : ItemStack.EMPTY);
        String reqLabel = selectedRequirement != null ? selectedRequirement.displayName : "Req";
        if (!requirementData.isEmpty())
            reqLabel = "Hold: " + requirementData.replace("minecraft:", "");

        drawSlot(context, startX + (slotSize + spacing) * 2, slotY, slotSize, reqLabel, reqIcon, selectedSlot == 2);
        context.drawText(this.textRenderer, "->", startX + (slotSize + spacing) * 3 - 8, slotY + 20, 0xFFFFFF, false);

        drawSlot(context, startX + (slotSize + spacing) * 3, slotY, slotSize,
                selectedAction != null ? selectedAction.displayName : "Action",
                selectedAction != null ? selectedAction.icon : ItemStack.EMPTY, selectedSlot == 3);

        int x = center - 150;
        int y = 160;
        int itemSize = 40;
        int columns = 6;

        for (int i = 0; i < filteredItems.size(); i++) {
            Object item = filteredItems.get(i);
            int col = i % columns;
            int row = i / columns;
            int itemX = x + col * (itemSize + 5);
            int itemY = y + row * (itemSize + 5);

            context.fill(itemX, itemY, itemX + itemSize, itemY + itemSize, 0xFF303030);

            ItemStack icon = ItemStack.EMPTY;
            String name = "";
            int color = 0xFFFFFF;

            if (item instanceof TriggerRegistry.TriggerType) {
                TriggerRegistry.TriggerType t = (TriggerRegistry.TriggerType) item;
                icon = t.icon;
                name = t.displayName;
                color = 0xFFFF5555;
            } else if (item instanceof ActionRegistry.ActionType) {
                ActionRegistry.ActionType a = (ActionRegistry.ActionType) item;
                icon = a.icon;
                name = a.displayName;
                color = 0xFF55FF55;
            } else if (item instanceof ConditionRegistry.ConditionType) {
                ConditionRegistry.ConditionType c = (ConditionRegistry.ConditionType) item;
                icon = c.icon;
                name = c.displayName;
                color = 0xFF5555FF;
            } else if (item instanceof com.example.educationmod.registries.RequirementRegistry.RequirementType) {
                com.example.educationmod.registries.RequirementRegistry.RequirementType r = (com.example.educationmod.registries.RequirementRegistry.RequirementType) item;
                icon = r.icon;
                name = r.displayName;
                color = 0xFFFFFF55;
            }

            context.fill(itemX, itemY, itemX + itemSize, itemY + 2, color);
            context.drawItem(icon, itemX + 12, itemY + 5);

            float scale = 0.6f;
            context.getMatrices().push();
            context.getMatrices().translate(itemX + 2, itemY + 28, 0);
            context.getMatrices().scale(scale, scale, 1.0f);
            context.drawText(this.textRenderer, name, 0, 0, 0xFFFFFF, false);
            context.getMatrices().pop();

            if (mouseX >= itemX && mouseX <= itemX + itemSize && mouseY >= itemY && mouseY <= itemY + itemSize) {
                context.drawTooltip(this.textRenderer, Text.literal(name), mouseX, mouseY);
            }
        }

        super.render(context, mouseX, mouseY, delta);
    }

    private void drawSlot(DrawContext context, int x, int y, int size, String label, ItemStack icon, boolean selected) {
        int color = selected ? 0xFFFFFFFF : 0xFF555555;
        context.drawBorder(x, y, size, size, color);
        context.fill(x + 1, y + 1, x + size - 1, y + size - 1, 0xFF202020);

        if (!icon.isEmpty()) {
            context.drawItem(icon, x + size / 2 - 8, y + size / 2 - 8);
            context.drawCenteredTextWithShadow(this.textRenderer, label, x + size / 2, y + size + 5, 0xFFFFFF);
        } else {
            context.drawCenteredTextWithShadow(this.textRenderer, label, x + size / 2, y + size / 2 - 4, 0xAAAAAA);
        }
    }
}
