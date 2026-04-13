package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.TotemPopColor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TotemParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TotemParticle.class)
public abstract class MixinTotemParticle extends SingleQuadParticle {

    protected MixinTotemParticle(ClientLevel world, double x, double y, double z, TextureAtlasSprite sprite) {
        super(world, x, y, z, sprite);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onRender(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteSet spriteProvider, CallbackInfo ci) {
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