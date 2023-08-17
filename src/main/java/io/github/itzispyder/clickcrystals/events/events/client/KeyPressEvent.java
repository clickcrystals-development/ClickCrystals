package io.github.itzispyder.clickcrystals.events.events.client;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;
import io.github.itzispyder.clickcrystals.gui.ClickType;

public class KeyPressEvent extends Event implements Cancellable {

    private final int keycode, scancode;
    private final ClickType action;
    private boolean cancelled;

    public KeyPressEvent(int keycode, int scancode, ClickType action) {
        this.keycode = keycode;
        this.action = action;
        this.scancode = scancode;
        this.cancelled = false;
    }

    public boolean isScreenNull() {
        return mc.currentScreen == null;
    }

    public ClickType getAction() {
        return action;
    }

    public int getKeycode() {
        return keycode;
    }

    public int getScancode() {
        return scancode;
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
