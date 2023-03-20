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
 * Block utils for the client player entity
 */
public abstract class BlockUtils {

    public static ActionResult interact(Vec3d vec) {
        return interact(vec,Direction.UP);
    }

    public static ActionResult interact(BlockPos pos, Direction dir) {
        Vec3d vec = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
        return interact(vec,dir);
    }

    public static ActionResult interact(Vec3d vec, Direction dir) {
        BlockPos pos = new BlockPos(vec);
        BlockHitResult result = new BlockHitResult(vec, dir,pos,false);
        return mc.interactionManager.interactBlock(mc.player,mc.player.getActiveHand(),result);
    }

    public static ActionResult interact(BlockHitResult result) {
        return mc.interactionManager.interactBlock(mc.player,mc.player.getActiveHand(),result);
    }

    public static boolean matchTargetBlock(Block block) {
        World world = mc.player.getWorld();
        Vec3d vec = mc.crosshairTarget.getPos();
        BlockPos pos = new BlockPos(vec);
        BlockState state = world.getBlockState(pos);
        return state != null && state.isOf(block);
    }
}
