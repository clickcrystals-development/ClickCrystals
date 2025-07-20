package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.TotemPopColor;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.particle.TotemParticle;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(TotemParticle.class)
public abstract class MixinTotemParticle extends Particle {

    protected MixinTotemParticle(ClientWorld world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onRender(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider, CallbackInfo ci) {
        TotemPopColor t = Module.get(TotemPopColor.class);
        if (t.isEnabled()) {
            Color c = t.getColor();
            setColor(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f);
            setAlpha(c.getAlpha() / 255f);
        }
    }
}
