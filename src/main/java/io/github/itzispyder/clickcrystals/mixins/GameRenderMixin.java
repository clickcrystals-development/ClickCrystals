package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoHurtCam;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoViewBob;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for GameRenderer
 */
@Mixin(GameRenderer.class)
public abstract class GameRenderMixin {

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    public void onHurtViewTilt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        final Module noHurtCam = Module.get(NoHurtCam.class);

        if (noHurtCam.isEnabled()) ci.cancel();
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    public void onViewBob(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        final Module noViewBob = Module.get(NoViewBob.class);

        if (noViewBob.isEnabled()) ci.cancel();
    }
}
