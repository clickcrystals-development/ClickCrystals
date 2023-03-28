package io.github.itzispyder.clickcrystals.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

/**
 * Client block utils
 */
public abstract class BlockUtils {

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
     * @param vec 3d vector
     * @param dir block face direction
     * @return action result
     */
    public static ActionResult interact(Vec3d vec, Direction dir) {
        BlockPos pos = new BlockPos(vec);
        BlockHitResult result = new BlockHitResult(vec, dir,pos,false);
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
        BlockPos pos = new BlockPos(mc.crosshairTarget.getPos());
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
}
