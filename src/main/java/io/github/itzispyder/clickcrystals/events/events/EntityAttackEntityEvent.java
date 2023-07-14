package io.github.itzispyder.clickcrystals.events.events;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.entity.Entity;

public class EntityAttackEntityEvent extends Event implements Cancellable {

    private final Entity attacker, victim;
    private boolean cancelled;

    public EntityAttackEntityEvent(Entity attacker, Entity victim) {
        this.attacker = attacker;
        this.victim = victim;
        this.cancelled = false;
    }

    public Entity getAttacker() {
        return attacker;
    }

    public Entity getVictim() {
        return victim;
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
