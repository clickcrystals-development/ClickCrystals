package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.HealthAsBar;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoOverlay;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoScoreboard;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud implements Global {

    @ModifyArgs(method = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusBars(Lnet/minecraft/client/gui/DrawContext;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 0))
    public void renderArmor0(Args args) {
        if (Module.isEnabled(HealthAsBar.class)) {
            args.set(2, MinecraftClient.getInstance().getWindow().getScaledHeight() - 50);
        }
    }

    @ModifyArgs(method = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusBars(Lnet/minecraft/client/gui/DrawContext;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 1))
    public void renderArmor1(Args args) {
        if (Module.isEnabled(HealthAsBar.class)) {
            args.set(2, MinecraftClient.getInstance().getWindow().getScaledHeight() - 50);
        }
    }

    @Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
    public void renderHealthBar(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
        HealthAsBar hpBar = Module.get(HealthAsBar.class);
        if (hpBar.isEnabled()) {
            hpBar.renderHealthBar(context, x, y, maxHealth, lastHealth, health, absorption);
            ci.cancel();
        }
    }

    @Inject(method = "renderSpyglassOverlay", at = @At("HEAD"), cancellable = true)
    public void renderSpyglassOverlay(DrawContext context, float scale, CallbackInfo ci) {
        if (Module.isEnabled(NoOverlay.class)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    public void renderPortalOverlay(DrawContext context, float nauseaStrength, CallbackInfo ci) {
        if (Module.isEnabled(NoOverlay.class)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
    public void renderVignetteOverlay(DrawContext context, Entity entity, CallbackInfo ci) {
        if (Module.isEnabled(NoOverlay.class)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderOverlay", at = @At("HEAD"), cancellable = true)
    public void renderOverlay(DrawContext context, Identifier texture, float opacity, CallbackInfo ci) {
        if (!texture.getPath().contains("pumpkinblur")) {
            return;
        }
        if (Module.isEnabled(NoOverlay.class)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At("HEAD"), cancellable = true)
    public void renderOverlay(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        if (Module.isEnabled(NoScoreboard.class)) {
            ci.cancel();
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void renderHuds(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (PlayerUtils.invalid())
            return;
        for (Hud hud : system.huds().values()) {
            if (hud.canRender()) {
                hud.render(context);
            }
        }
    }
}
