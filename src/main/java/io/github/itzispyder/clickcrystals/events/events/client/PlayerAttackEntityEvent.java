package io.github.itzispyder.clickcrystals.events.events.client;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.HitResult;

public class PlayerAttackEntityEvent extends Event implements Cancellable {

    private final PlayerEntity player;
    private final Entity entity;
    private final HitResult hitResult;
    private boolean cancelled;

    public PlayerAttackEntityEvent(PlayerEntity player, Entity entity, HitResult hitResult) {
        this.player = player;
        this.entity = entity;
        this.hitResult = hitResult;
        this.cancelled = false;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public Entity getEntity() {
        return entity;
    }

    public HitResult getHitResult() {
        return hitResult;
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
