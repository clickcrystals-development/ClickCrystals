package io.github.itzispyder.clickcrystals.events.events;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class PlayerAttackEntityEvent extends Event implements Cancellable {

    private final PlayerEntity player;
    private final World world;
    private final Hand hand;
    private final Entity entity;
    private final HitResult hitResult;
    private boolean cancelled;

    public PlayerAttackEntityEvent(PlayerEntity player, World world, Hand hand, Entity entity, HitResult hitResult) {
        this.player = player;
        this.world = world;
        this.hand = hand;
        this.entity = entity;
        this.hitResult = hitResult;
        this.cancelled = false;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public World getWorld() {
        return world;
    }

    public Hand getHand() {
        return hand;
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
