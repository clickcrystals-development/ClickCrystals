package io.github.itzispyder.clickcrystals.mixins;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Particle.class)
public abstract class MixinParticle {

    @Unique
    public abstract void setColor(float red, float green, float blue);

    @Shadow protected void setAlpha(float alpha) {}
}