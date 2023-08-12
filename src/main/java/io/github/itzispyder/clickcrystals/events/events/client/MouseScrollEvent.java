package io.github.itzispyder.clickcrystals.events.events.client;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;

public class MouseScrollEvent extends Event implements Cancellable {

    private final double deltaX, deltaY;
    private boolean cancelled;

    public MouseScrollEvent(double deltaX, double deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.cancelled = false;
    }

    public double getAvg() {
        return (deltaX + deltaY) / 2;
    }

    public boolean isVertical() {
        return Math.abs(deltaY) > Math.abs(deltaX);
    }

    public boolean isHorizontal() {
        return Math.abs(deltaY) < Math.abs(deltaX);
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public int getDeltaXAsInt() {
        return (int)deltaX;
    }

    public int getDeltaYAsInt() {
        return (int)deltaY;
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
