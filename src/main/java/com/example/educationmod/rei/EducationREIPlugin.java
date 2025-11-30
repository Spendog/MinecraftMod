package com.example.educationmod.rei;

import com.example.educationmod.gui.TriggerDashboardScreen;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.client.gui.drag.*;
import me.shedaniel.math.Rectangle;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.shape.VoxelShapes;
import java.util.Collections;
import java.util.stream.Stream;
import me.shedaniel.math.Point;

public class EducationREIPlugin implements REIClientPlugin {

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerDraggableStackVisitor(new DraggableStackVisitor<TriggerDashboardScreen>() {
            @Override
            public <R extends Screen> boolean isHandingScreen(R screen) {
                return screen instanceof TriggerDashboardScreen;
            }

            @Override
            public Stream<BoundsProvider> getDraggableAcceptingBounds(DraggingContext<TriggerDashboardScreen> context,
                    DraggableStack stack) {
                return Stream.empty();
            }

            @Override
            public DraggedAcceptorResult acceptDraggedStack(DraggingContext<TriggerDashboardScreen> context,
                    DraggableStack stack) {
                TriggerDashboardScreen screen = context.getScreen();

                // Calculate Slot Bounds
                int sidebarWidth = 150;
                int center = screen.width / 2 + (sidebarWidth / 2);
                int slotY = 80;
                int slotSize = 50;
                int spacing = 10;
                int startX = center - 130;
                int x = startX + (slotSize + spacing) * 2;
                int y = slotY;

                // Check if mouse is within bounds
                // context.getCurrentPosition() returns Point usually
                Point mouse = context.getCurrentPosition();
                if (mouse != null && mouse.x >= x && mouse.x <= x + slotSize && mouse.y >= y
                        && mouse.y <= y + slotSize) {
                    // Extract Item ID
                    Object value = stack.getStack().getValue();
                    if (value instanceof net.minecraft.item.ItemStack) {
                        net.minecraft.item.ItemStack itemStack = (net.minecraft.item.ItemStack) value;
                        String itemId = net.minecraft.registry.Registries.ITEM.getId(itemStack.getItem()).toString();

                        screen.handleRequirementDrop(itemId, itemStack);
                        return DraggedAcceptorResult.CONSUMED;
                    }
                }
                return DraggedAcceptorResult.PASS;
            }
        });
    }
}
