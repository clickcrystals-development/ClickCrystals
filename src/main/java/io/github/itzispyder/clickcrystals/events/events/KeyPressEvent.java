package io.github.itzispyder.clickcrystals.events.events;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;
import io.github.itzispyder.clickcrystals.gui.ClickType;

public class KeyPressEvent extends Event implements Cancellable {

    private final int keycode;
    private final ClickType action;
    private boolean cancelled;

    public KeyPressEvent(int keycode, ClickType action) {
        this.keycode = keycode;
        this.action = action;
        this.cancelled = false;
    }

    public ClickType getAction() {
        return action;
    }

    public int getKeycode() {
        return keycode;
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
