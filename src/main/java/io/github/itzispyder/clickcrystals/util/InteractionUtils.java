package io.github.itzispyder.clickcrystals.util;

import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

/**
 * Client interaction utils
 */
public abstract class InteractionUtils {

    /**
     * Left clicks as if the player has inputted the click
     */
    public static void doAttack() {
        HitResult hit = mc.crosshairTarget;
        if (hit == null) return;
        Vec3d vec3d = hit.getPos();
        Vec3i vec3i = new Vec3i((int) vec3d.x,(int) vec3d.y,(int) vec3d.z);
        BlockPos pos = new BlockPos(vec3i);
        switch (hit.getType()) {
            case BLOCK -> mc.interactionManager.attackBlock(pos,Direction.UP);
            case ENTITY -> mc.interactionManager.attackEntity(mc.player,((EntityHitResult) hit).getEntity());
        }
        mc.player.swingHand(Hand.MAIN_HAND);
    }
}
