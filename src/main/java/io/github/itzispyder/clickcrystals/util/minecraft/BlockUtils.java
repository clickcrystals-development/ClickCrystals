package io.github.itzispyder.clickcrystals.util.minecraft;

import io.github.itzispyder.clickcrystals.Global;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

/**
 * Client block utils
 */
public final class BlockUtils implements Global {

    /**
     * Interact a block
     * @param vec 3d vector
     * @return action result
     */
    public static ActionResult interact(Vec3d vec) {
        return interact(vec,Direction.UP);
    }

    /**
     * Interact a block
     * @param pos block position
     * @param dir block face direction
     * @return action result
     */
    public static ActionResult interact(BlockPos pos, Direction dir) {
        Vec3d vec = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
        return interact(vec,dir);
    }

    /**
     * Interact a block
     * @param vec3d 3d vector
     * @param dir block face direction
     * @return action result
     */
    public static ActionResult interact(Vec3d vec3d, Direction dir) {
        Vec3i vec3i = new Vec3i((int) vec3d.x, (int) vec3d.y, (int) vec3d.z);
        BlockPos pos = new BlockPos(vec3i);
        BlockHitResult result = new BlockHitResult(vec3d, dir,pos,false);
        return mc.interactionManager.interactBlock(mc.player,mc.player.getActiveHand(),result);
    }

    /**
     * Interact a block
     * @param result block ray-cast hit result
     * @return action result
     */
    public static ActionResult interact(BlockHitResult result) {
        return mc.interactionManager.interactBlock(mc.player,mc.player.getActiveHand(),result);
    }

    /**
     * If the cross-hair targeted block matches the provided type
     * @param block block type
     * @return match
     */
    public static boolean matchTargetBlock(Block block) {
        Vec3d vec3d = mc.crosshairTarget.getPos();
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
        if (pos == null) return false;
        World world = mc.player.getWorld();
        BlockState state = world.getBlockState(pos);
        return state != null && state.isOf(block);
    }

    public static boolean isCrystallabe(BlockPos pos) {
        return matchBlock(pos,Blocks.OBSIDIAN) || matchBlock(pos,Blocks.BEDROCK);
    }

    public static BlockState getCrosshair() {
        final HitResult hit = mc.crosshairTarget;

        if (hit == null) return null;
        if (hit.getType() != HitResult.Type.BLOCK) return null;
        if (mc.player == null) return null;
        if (mc.player.getWorld() == null) return null;

        final BlockPos pos = ((BlockHitResult) hit).getBlockPos();

        return mc.player.getWorld().getBlockState(pos);
    }
}
