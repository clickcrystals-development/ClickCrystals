package io.github.itzispyder.clickcrystals.util.minecraft;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.listeners.TickEventListener;
import io.github.itzispyder.clickcrystals.interfaces.MinecraftClientAccessor;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

/**
 * Client interaction utils
 */
public final class InteractionUtils implements Global {

    /**
     * Left clicks as if the player has inputted the click
     */
    public static void inputAttack() {
        ((MinecraftClientAccessor) mc).inputAttack();
    }

    public static void inputUse() {
        ((MinecraftClientAccessor) mc).inputUse();
    }

    public static void inputJump() {
        TickEventListener.jump(100);
    }

    public static void inputForward() {
        TickEventListener.forward(500);
    }

    public static void inputBackward() {
        TickEventListener.backward(500);
    }

    public static void inputStrafeLeft() {
        TickEventListener.strafeLeft(500);
    }

    public static void inputStrafeRight() {
        TickEventListener.strafeRight(500);
    }

    public static void inputSneak() {
        TickEventListener.sneak(500);
    }

    public static boolean canBreakCrystals() {
        if (mc.player == null) return false;

        final StatusEffectInstance s = mc.player.getStatusEffect(StatusEffects.STRENGTH);
        final StatusEffectInstance w = mc.player.getStatusEffect(StatusEffects.WEAKNESS);

        if (s == null && w == null) return true;
        if (w == null) return true;
        if (s != null && s.getAmplifier() > w.getAmplifier()) return true;

        return HotbarUtils.isHoldingTool();
    }
}
