package io.github.itzispyder.clickcrystals.events.events.world;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.slot.Slot;

public class DrawSlotEvent extends Event implements Cancellable {

    private boolean cancelled;
    private final Slot slot;
    private final DrawContext context;

    public DrawSlotEvent(DrawContext context, Slot slot) {
        this.context = context;
        this.slot = slot;
        this.cancelled = false;
    }

    public DrawContext getContext() {
        return context;
    }

    public Slot getSlot() {
        return slot;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
