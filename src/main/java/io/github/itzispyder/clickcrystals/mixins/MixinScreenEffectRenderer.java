package io.github.itzispyder.clickcrystals.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoOverlay;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.TotemPopScale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ScreenEffectRenderer.class)
public abstract class MixinScreenEffectRenderer {

    @Inject(method = "renderFire", at = @At("HEAD"), cancellable = true)
    private static void renderFireOverlay(PoseStack matrices, MultiBufferSource vertexConsumers, TextureAtlasSprite sprite, CallbackInfo ci) {
        if (Module.isEnabled(NoOverlay.class)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderWater", at = @At("HEAD"), cancellable = true)
    private static void renderUnderwaterOverlay(Minecraft client, PoseStack matrices, MultiBufferSource vertexConsumers, CallbackInfo ci) {
        if (Module.isEnabled(NoOverlay.class)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderTex", at = @At("HEAD"), cancellable = true)
    private static void renderInWallOverlay(TextureAtlasSprite sprite, PoseStack matrices, MultiBufferSource vertexConsumers, CallbackInfo ci) {
        if (Module.isEnabled(NoOverlay.class)) {
            ci.cancel();
        }
    }

    @ModifyArgs(method = "renderItemActivationAnimation", at = @At(target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V", value = "INVOKE"))
    public void renderFloatingItemScaled(Args args) {
        if (!Module.isEnabled(TotemPopScale.class)) {
            return;
        }

        double scale = Module.get(TotemPopScale.class).scale.getVal();
        float f = (float)scale;
        args.set(0, (float)args.get(0) * f);
        args.set(1, (float)args.get(1) * f);
        args.set(2, (float)args.get(2) * f);
    }

    @ModifyArgs(method = "renderItemActivationAnimation", at = @At(target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", value = "INVOKE"))
    public void renderFloatingItemTranslated(Args args) {
        if (!Module.isEnabled(TotemPopScale.class)) {
            return;
        }

        TotemPopScale tps = Module.get(TotemPopScale.class);
        double deltaX = tps.translateX.getVal();
        double deltaY = tps.translateY.getVal();
        float x = (float)deltaX;
        float y = (float)deltaY;
        args.set(0, (float)args.get(0) + x);
        args.set(1, (float)args.get(1) - y);
    }
}
