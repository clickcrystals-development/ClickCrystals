package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.NoOverlay;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for the in game hud
 */
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Inject(method = "renderSpyglassOverlay", at = @At("HEAD"), cancellable = true)
    public void renderSpyglassOverlay(MatrixStack matrices, float scale, CallbackInfo ci) {
        Module noOverlay = Module.get(NoOverlay.class);
        if (noOverlay.isEnabled()) ci.cancel();
    }

    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    public void renderPortalOverlay(MatrixStack matrices, float nauseaStrength, CallbackInfo ci) {
        Module noOverlay = Module.get(NoOverlay.class);
        if (noOverlay.isEnabled()) ci.cancel();
    }

    @Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
    public void renderVignetteOverlay(MatrixStack matrices, Entity entity, CallbackInfo ci) {
        Module noOverlay = Module.get(NoOverlay.class);
        if (noOverlay.isEnabled()) ci.cancel();
    }

    @Inject(method = "renderOverlay", at = @At("HEAD"), cancellable = true)
    public void renderOverlay(MatrixStack matrices, Identifier texture, float opacity, CallbackInfo ci) {
        if (!texture.getPath().contains("pumpkinblur")) return;
        Module noOverlay = Module.get(NoOverlay.class);
        if (noOverlay.isEnabled()) ci.cancel();
    }
}
