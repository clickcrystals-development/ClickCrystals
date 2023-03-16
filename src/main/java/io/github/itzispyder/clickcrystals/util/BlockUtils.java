package io.github.itzispyder.clickcrystals.util;

import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

/**
 * Block utils for the client player entity
 */
public abstract class BlockUtils {

    public static ActionResult interact(Vec3d vec) {
        BlockPos pos = new BlockPos(vec);
        BlockHitResult result = new BlockHitResult(vec, Direction.UP,pos,false);
        return mc.interactionManager.interactBlock(mc.player,mc.player.getActiveHand(),result);
    }
}
