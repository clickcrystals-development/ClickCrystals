package io.github.itzispyder.clickcrystals.events.events;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;

public class BlockPlaceEvent extends Event implements Cancellable {

    private final ClientPlayerEntity player;
    private final Hand hand;
    private final BlockHitResult hitResult;
    private boolean cancelled;

    public BlockPlaceEvent(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult) {
        this.hand = hand;
        this.player = player;
        this.hitResult = hitResult;
        this.cancelled = false;
    }

    public ClientPlayerEntity getPlayer() {
        return player;
    }

    public Hand getHand() {
        return hand;
    }

    public BlockHitResult getHitResult() {
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
