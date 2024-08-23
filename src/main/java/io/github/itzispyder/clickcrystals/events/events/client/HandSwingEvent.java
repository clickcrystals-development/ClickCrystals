package io.github.itzispyder.clickcrystals.events.events.client;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.util.Hand;

public class HandSwingEvent extends Event implements Cancellable {

    private boolean cancelled;
    private final Hand hand;

public HandSwingEvent(Hand hand){
    this.hand = hand;
    this.cancelled = false;
}

    public Hand getHand() {
        return hand;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
