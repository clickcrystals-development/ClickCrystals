package io.github.itzispyder.clickcrystals.events.events;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerWasAttackedEvent extends Event implements Cancellable {

    private final PlayerEntity player;
    private final Entity attacker;
    private boolean cancelled;

    public PlayerWasAttackedEvent(Entity attacker, PlayerEntity player) {
        this.attacker = attacker;
        this.player = player;
        this.cancelled = false;
    }

    public Entity getAttacker() {
        return attacker;
    }

    public PlayerEntity getPlayer() {
        return player;
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
