package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.SlowHandSwing;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for player entity
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityClassMixin {

    @Inject(method = "getAttackCooldownProgress", at = @At("RETURN"), cancellable = true)
    public void getAttackCooldownProgress(float baseTime, CallbackInfoReturnable<Float> cir) {
        Module slowHandSwing = Module.get(SlowHandSwing.class);
        if (slowHandSwing.isEnabled()) cir.setReturnValue(1.0F);
    }
}
