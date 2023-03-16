package io.github.itzispyder.clickcrystals.events;

/**
 * Cancellable event
 */
public interface Cancellable {

    void setCancelled(boolean cancelled);

    boolean isCancelled();
}
