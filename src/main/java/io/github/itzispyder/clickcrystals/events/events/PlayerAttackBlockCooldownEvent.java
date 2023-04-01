package io.github.itzispyder.clickcrystals.events.events;

import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * Called after a player punches a block and a cooldown is set
 */
public class PlayerAttackBlockCooldownEvent extends Event {

    private final Direction dir;
    private final BlockPos pos;
    private int cooldown;

    /**
     * Constructs a player attack block event
     * @param pos block position
     * @param dir punch direction/side
     */
    public PlayerAttackBlockCooldownEvent(BlockPos pos, Direction dir, int cooldown) {
        this.pos = pos;
        this.dir = dir;
        this.cooldown = cooldown;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Direction getDir() {
        return dir;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
}
