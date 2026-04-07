package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoOverlay;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.SlowSwing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @Inject(method = "getCurrentSwingDuration", at = @At("RETURN"), cancellable = true)
    public void getHandSwingDuration(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Module.isEnabled(SlowSwing.class) ? 12 : cir.getReturnValue());
    }

    @Inject(method = "getActiveEffects", at = @At("HEAD"), cancellable = true)
    private void getStatusEffects(CallbackInfoReturnable<Collection<MobEffectInstance>> cir) {
        if (!Module.get(NoOverlay.class).isEnabled())
            return;

        AccessorLivingEntity ale = (AccessorLivingEntity) this;
        var activeStatusEffects = ale.accessActiveStatusEffects();

        activeStatusEffects.remove(MobEffects.BLINDNESS);
        activeStatusEffects.remove(MobEffects.DARKNESS);
        cir.setReturnValue(activeStatusEffects.values());
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z", at = @At("HEAD"), cancellable = true)
    private void addStatusEffect(MobEffectInstance newEffect, CallbackInfoReturnable<Boolean> cir) {
        if (Module.get(NoOverlay.class).isEnabled()) {
            if (newEffect.getEffect() == MobEffects.BLINDNESS || newEffect.getEffect() == MobEffects.DARKNESS) {
                cir.setReturnValue(false);
            }
        }
    }
}
