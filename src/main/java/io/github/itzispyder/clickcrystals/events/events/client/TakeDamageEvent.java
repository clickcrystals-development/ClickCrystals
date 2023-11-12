package io.github.itzispyder.clickcrystals.events.events.client;

import io.github.itzispyder.clickcrystals.events.Event;
import io.github.itzispyder.clickcrystals.util.PlayerUtils;

public class TakeDamageEvent extends Event {

    private final float amount;

    public TakeDamageEvent(float amount) {
        this.amount = amount;
    }

    public float getCurrentHealth() {
        return PlayerUtils.playerNull() ? 0.0F : PlayerUtils.player().getHealth();
    }

    public float getAmount() {
        return amount;
    }
}
