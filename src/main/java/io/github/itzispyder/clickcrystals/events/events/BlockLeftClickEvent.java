package io.github.itzispyder.clickcrystals.events.events;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/**
 * Block left click event
 */
public class BlockLeftClickEvent extends Event implements Cancellable {

    private final PlayerEntity player;
    private final World world;
    private final Hand hand;
    private final BlockPos pos;
    private final Direction direction;
    private boolean cancelled;

    public BlockLeftClickEvent(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) {
        this.player = player;
        this.world = world;
        this.hand = hand;
        this.pos = pos;
        this.direction = direction;
        this.cancelled = false;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Direction getDirection() {
        return direction;
    }

    public Hand getHand() {
        return hand;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void setCancelled(boolean cancelled) {

    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}
