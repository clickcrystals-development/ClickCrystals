package io.github.itzispyder.clickcrystals.events.events.client;

import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

public class RenderInventorySlotEvent extends Event {

    private final GuiGraphicsExtractor guiGraphicsExtractor;
    private final ItemStack item;
    private final int x, y, slotX, slotY;

    public RenderInventorySlotEvent(GuiGraphicsExtractor guiGraphicsExtractor, ItemStack item, int x, int y, int slotX, int slotY) {
        this.guiGraphicsExtractor = guiGraphicsExtractor;
        this.item = item;
        this.x = x;
        this.y = y;
        this.slotX = slotX;
        this.slotY = slotY;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSlotX() {
        return slotX;
    }

    public int getSlotY() {
        return slotY;
    }

    public GuiGraphicsExtractor getGuiGraphicsExtractor() {
        return guiGraphicsExtractor;
    }
}
