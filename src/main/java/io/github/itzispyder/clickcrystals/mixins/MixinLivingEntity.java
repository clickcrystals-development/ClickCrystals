package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoOverlay;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.SlowSwing;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @Shadow @Final private Map<RegistryEntry<StatusEffect>, StatusEffectInstance> activeStatusEffects;

    @Inject(method = "getHandSwingDuration", at = @At("RETURN"), cancellable = true)
    public void getHandSwingDuration(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Module.isEnabled(SlowSwing.class) ? 12 : cir.getReturnValue());
    }

    @Inject(method = "getStatusEffects", at = @At("HEAD"), cancellable = true)
    private void getStatusEffects(CallbackInfoReturnable<Collection<StatusEffectInstance>> cir) {
        if (Module.get(NoOverlay.class).isEnabled()) {
            activeStatusEffects.remove(StatusEffects.BLINDNESS);
            activeStatusEffects.remove(StatusEffects.DARKNESS);
            cir.setReturnValue(activeStatusEffects.values());
        }
    }

    @Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z", at = @At("HEAD"), cancellable = true)
    private void addStatusEffect(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        if (Module.get(NoOverlay.class).isEnabled()) {
            if (effect.getEffectType() == StatusEffects.BLINDNESS || effect.getEffectType() == StatusEffects.DARKNESS) {
                cir.setReturnValue(false);
            }
        }
    }
}
