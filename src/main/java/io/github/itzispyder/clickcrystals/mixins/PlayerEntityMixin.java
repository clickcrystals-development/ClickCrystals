package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.events.events.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.SlowSwing;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;
import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

/**
 * Mixin for player entity
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "getAttackCooldownProgress", at = @At("RETURN"), cancellable = true)
    public void getAttackCooldownProgress(float baseTime, CallbackInfoReturnable<Float> cir) {
        Module slowHandSwing = Module.get(SlowSwing.class);
        if (slowHandSwing.isEnabled()) cir.setReturnValue(1.0F);
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void attack(Entity target, CallbackInfo ci) {
        PlayerAttackEntityEvent event = new PlayerAttackEntityEvent(mc.player, target, mc.crosshairTarget);
        if (system.eventBus.pass(event)) ci.cancel();
    }
}
