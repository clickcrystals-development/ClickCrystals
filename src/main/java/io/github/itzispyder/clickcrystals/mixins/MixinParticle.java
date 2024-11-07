package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.interfaces.ParticleAccessor;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Particle.class)
public abstract class MixinParticle implements ParticleAccessor {

    @Shadow public abstract void setColor(float red, float green, float blue);

    @Shadow protected abstract void setAlpha(float alpha);

    @Override
    public void setParticleColor(float red, float green, float blue) {
        this.setColor(red, green, blue);
    }

    @Override
    public void setParticleAlpha(float alpha) {
        this.setAlpha(alpha);
    }
}
