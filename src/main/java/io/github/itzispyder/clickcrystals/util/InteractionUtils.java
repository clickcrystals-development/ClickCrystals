package io.github.itzispyder.clickcrystals.util;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

/**
 * Client interaction utils
 */
public final class InteractionUtils {

    /**
     * Left clicks as if the player has inputted the click
     */
    public static void doAttack() {
        final HitResult hit = mc.crosshairTarget;
        final ClientPlayerInteractionManager im = mc.interactionManager;

        if (hit == null) return;

        switch (hit.getType()) {
            case ENTITY -> im.attackEntity(mc.player, ((EntityHitResult) hit).getEntity());
            case BLOCK -> im.attackBlock(((BlockHitResult) hit).getBlockPos(), ((BlockHitResult) hit).getSide());
        }

        mc.player.swingHand(Hand.MAIN_HAND);
    }

    public static void doUse() {
        final HitResult hit = mc.crosshairTarget;
        final ClientPlayerInteractionManager im = mc.interactionManager;

        if (hit == null) return;

        switch (hit.getType()) {
            case ENTITY -> im.interactEntity(mc.player, ((EntityHitResult) hit).getEntity(), Hand.MAIN_HAND);
            case BLOCK -> im.interactBlock(mc.player, Hand.MAIN_HAND, (BlockHitResult) hit);
            case MISS -> im.interactItem(mc.player, Hand.MAIN_HAND);
        }

        mc.player.swingHand(Hand.MAIN_HAND);
    }
}
