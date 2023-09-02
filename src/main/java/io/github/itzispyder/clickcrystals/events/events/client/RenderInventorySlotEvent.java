package io.github.itzispyder.clickcrystals.events.events.client;

import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.item.ItemStack;

public class RenderInventorySlotEvent extends Event {

    private final ItemStack item;
    private final int x, y;

    public RenderInventorySlotEvent(ItemStack item, int x, int y) {
        this.item = item;
        this.x = x;
        this.y = y;
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
}
