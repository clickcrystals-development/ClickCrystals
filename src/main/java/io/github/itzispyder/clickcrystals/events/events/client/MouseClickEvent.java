package io.github.itzispyder.clickcrystals.events.events.client;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;
import io.github.itzispyder.clickcrystals.gui.ClickType;

public class MouseClickEvent extends Event implements Cancellable {

    private final int button;
    private final ClickType action;
    private boolean cancelled;

    public MouseClickEvent(int button, ClickType action) {
        this.button = button;
        this.action = action;
        this.cancelled = false;
    }

    public boolean isScreenNull() {
        return mc.currentScreen == null;
    }

    public ClickType getAction() {
        return action;
    }

    public int getButton() {
        return button;
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
