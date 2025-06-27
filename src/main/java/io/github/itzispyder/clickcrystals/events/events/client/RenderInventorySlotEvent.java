package io.github.itzispyder.clickcrystals.events.events.client;

import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

public class RenderInventorySlotEvent extends Event {

    private final DrawContext drawContext;
    private final ItemStack item;
    private final int x, y, slotX, slotY;

    public RenderInventorySlotEvent(DrawContext drawContext, ItemStack item, int x, int y, int slotX, int slotY) {
        this.drawContext = drawContext;
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

    public DrawContext getDrawContext() {
        return drawContext;
    }
}
