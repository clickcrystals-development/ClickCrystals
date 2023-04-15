package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.ParticleBloom;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for rendering particles
 */
@Mixin(Particle.class)
public abstract class ParticleMixin {

    @Inject(method = "getBrightness", at = @At("RETURN"), cancellable = true)
    public void getBrightness(float tint, CallbackInfoReturnable<Integer> cir) {
        Module particleBloom = Module.get(ParticleBloom.class);
        if (particleBloom.isEnabled()) cir.setReturnValue(15728880);
    }
}
