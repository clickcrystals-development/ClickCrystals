package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.TotemPopColor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TotemParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TotemParticle.class)
public abstract class MixinTotemParticle extends SimpleAnimatedParticle {

    protected MixinTotemParticle(ClientLevel world, double x, double y, double z, SpriteSet sprite, float grav) {
        super(world, x, y, z, sprite, grav);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onRender(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites, CallbackInfo ci) {
        TotemPopColor t = Module.get(TotemPopColor.class);
        if (!t.isEnabled())
            return;

        setColor(
                t.red.getVal().floatValue() / 0xFF,
                t.green.getVal().floatValue() / 0xFF,
                t.blue.getVal().floatValue() / 0xFF
        );
        setAlpha(t.alpha.getVal().floatValue() / 0xFF);
    }
}
