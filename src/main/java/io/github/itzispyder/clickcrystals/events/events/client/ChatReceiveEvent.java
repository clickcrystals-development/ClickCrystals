package io.github.itzispyder.clickcrystals.events.events.client;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;

/**
 * Called when a chat message is received
 */
public class ChatReceiveEvent extends Event implements Cancellable {

    private static boolean locked = false;
    private final String message;
    private boolean cancelled;

    public ChatReceiveEvent(String message) {
        this.message = message;
        this.cancelled = false;
    }

    public static void lock() {
        locked = true;
    }

    public static void unlock() {
        locked = false;
    }

    public static boolean isLocked() {
        return locked;
    }

    public boolean locked() {
        return isLocked();
    }

    public String getMessage() {
        return message;
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
