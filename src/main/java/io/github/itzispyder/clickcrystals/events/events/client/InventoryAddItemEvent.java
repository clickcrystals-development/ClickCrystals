package io.github.itzispyder.clickcrystals.events.events.client;

import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.item.ItemStack;

public class InventoryAddItemEvent extends Event {

    private final ItemStack item;
    private final int slot;

    public InventoryAddItemEvent(int slot, ItemStack item) {
        this.slot = slot;
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getSlot() {
        return slot;
    }
}
