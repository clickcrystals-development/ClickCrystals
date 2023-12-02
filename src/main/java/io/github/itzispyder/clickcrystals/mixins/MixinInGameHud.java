package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.HealthAsBar;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoOverlay;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoScoreboard;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud {

    @Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
    public void renderHealthBar(MatrixStack context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
        Module.acceptFor(HealthAsBar.class, hpBar -> {
            hpBar.renderHealthBar(context, x, y, maxHealth, lastHealth, health, absorption);
            ci.cancel();
        });
    }

    @Inject(method = "renderSpyglassOverlay", at = @At("HEAD"), cancellable = true)
    public void renderSpyglassOverlay(MatrixStack context, float scale, CallbackInfo ci) {
        Module noOverlay = Module.get(NoOverlay.class);
        if (noOverlay.isEnabled()) ci.cancel();
    }

    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    public void renderPortalOverlay(MatrixStack context, float nauseaStrength, CallbackInfo ci) {
        Module noOverlay = Module.get(NoOverlay.class);
        if (noOverlay.isEnabled()) ci.cancel();
    }

    @Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
    public void renderVignetteOverlay(MatrixStack context, Entity entity, CallbackInfo ci) {
        Module noOverlay = Module.get(NoOverlay.class);
        if (noOverlay.isEnabled()) ci.cancel();
    }

    @Inject(method = "renderOverlay", at = @At("HEAD"), cancellable = true)
    public void renderOverlay(MatrixStack context, Identifier texture, float opacity, CallbackInfo ci) {
        if (!texture.getPath().contains("pumpkinblur")) return;
        Module noOverlay = Module.get(NoOverlay.class);
        if (noOverlay.isEnabled()) ci.cancel();
    }

    @Inject(method = "renderScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    public void renderOverlay(MatrixStack context, ScoreboardObjective objective, CallbackInfo ci) {
        Module noScoreboard = Module.get(NoScoreboard.class);
        if (noScoreboard.isEnabled()) ci.cancel();
    }
}
