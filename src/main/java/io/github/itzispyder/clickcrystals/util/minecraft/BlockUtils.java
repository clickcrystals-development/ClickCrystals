package io.github.itzispyder.clickcrystals.util.minecraft;

import io.github.itzispyder.clickcrystals.Global;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class BlockUtils implements Global {

    public static InteractionResult interact(Vec3 vec) {
        return interact(vec,Direction.UP);
    }

    public static InteractionResult interact(BlockPos pos, Direction dir) {
        Vec3 vec = new Vec3(pos.getX(), pos.getY(), pos.getZ());
        return interact(vec, dir);
    }

    public static InteractionResult interact(Vec3 vec3d, Direction dir) {
        if (PlayerUtils.invalid()) {
            return InteractionResult.FAIL;
        }

        Vec3i vec3i = new Vec3i((int) vec3d.x, (int) vec3d.y, (int) vec3d.z);
        BlockPos pos = new BlockPos(vec3i);
        BlockHitResult result = new BlockHitResult(vec3d, dir, pos, false);
        var p = PlayerUtils.player();
        return PlayerUtils.getInteractions().useItemOn(p, p.getUsedItemHand(), result);
    }

    public static InteractionResult interact(BlockHitResult result) {
        if (PlayerUtils.invalid()) {
            return InteractionResult.FAIL;
        }

        var im = PlayerUtils.getInteractions();
        var p = PlayerUtils.player();
        return im.useItemOn(p, p.getUsedItemHand(), result);
    }

    public static boolean matchTargetBlock(Block block) {
        if (mc.hitResult == null) {
            return false;
        }

        Vec3 vec3d = mc.hitResult.getLocation();
        Vec3i vec3i = new Vec3i((int) vec3d.x, (int) vec3d.y, (int) vec3d.z);
        BlockPos pos = new BlockPos(vec3i);
        return matchBlock(pos,block);
    }

    /**
     * If the provided block position matches the provided type
     * @param pos block position
     * @param block block type
     * @return match
     */
    public static boolean matchBlock(BlockPos pos, Block block) {
        if (PlayerUtils.invalid()) {
            return false;
        }

        Level world = PlayerUtils.getWorld();
        BlockState state = world.getBlockState(pos);
        return state != null && state.is(block);
    }

    public static boolean canCrystalOn(BlockPos pos) {
        return matchBlock(pos,Blocks.OBSIDIAN) || matchBlock(pos,Blocks.BEDROCK);
    }
}
