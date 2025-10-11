package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.TotemPopColor;
import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.particle.TotemParticle;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TotemParticle.class)
public abstract class MixinTotemParticle extends BillboardParticle {

    protected MixinTotemParticle(ClientWorld world, double x, double y, double z, Sprite sprite) {
        super(world, x, y, z, sprite);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onRender(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider, CallbackInfo ci) {
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
