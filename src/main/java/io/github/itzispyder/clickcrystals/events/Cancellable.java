package io.github.itzispyder.clickcrystals.events;

public interface Cancellable {

    void setCancelled(boolean cancelled);

    boolean isCancelled();

    default void cancel() {
        setCancelled(true);
    }
}
