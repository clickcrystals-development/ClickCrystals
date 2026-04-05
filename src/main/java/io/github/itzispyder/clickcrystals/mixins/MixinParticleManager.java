package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.client.ParticleReceiveEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.TotemChams;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleEngine.class)
public abstract class MixinParticleManager implements Global {

    @Inject(method = "makeParticle", at = @At("HEAD"), cancellable = true)
    public <T extends ParticleOptions> void addParticle(T p, double x, double y, double z, double xa, double ya, double za, CallbackInfoReturnable<Particle> cir) {
        ParticleReceiveEvent event = new ParticleReceiveEvent(p.getType(), x, y, z, xa, ya, za);
        system.eventBus.passWithCallbackInfo(cir, event);

        if (p.getType() == ParticleTypes.TOTEM_OF_UNDYING && Module.isEnabled(TotemChams.class))
            cir.cancel();
    }
}
