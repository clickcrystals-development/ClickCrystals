package io.github.itzispyder.clickcrystals.events.events.client;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;

/**
 * Called when a chat message is sent
 */
public class ChatSendEvent extends Event implements Cancellable {

    private String message;
    private boolean cancelled;

    public ChatSendEvent(String message) {
        this.message = message;
        this.cancelled = false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
