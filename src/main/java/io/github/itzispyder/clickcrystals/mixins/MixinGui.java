package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.HealthAsBar;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoOverlay;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.NoScoreboard;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Gui.class)
public abstract class MixinGui implements Global {

    @ModifyArgs(method = "extractArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"))
    private static void renderArmor(Args args) {
        if (Module.isEnabled(HealthAsBar.class))
            args.set(3, Minecraft.getInstance().getWindow().getGuiScaledHeight() - 50);
    }

    @Inject(method = "extractHearts", at = @At("HEAD"), cancellable = true)
    public void renderHealthBar(GuiGraphicsExtractor graphics, Player player, int xLeft, int yLineBase, int healthRowHeight, int heartOffsetIndex, float maxHealth, int currentHealth, int oldHealth, int absorption, boolean blink, CallbackInfo ci) {
        HealthAsBar hpBar = Module.get(HealthAsBar.class);
        if (hpBar.isEnabled()) {
            hpBar.renderHealthBar(graphics, xLeft, yLineBase, maxHealth, oldHealth, currentHealth, absorption);
            ci.cancel();
        }
    }

    @Inject(method = "extractSpyglassOverlay", at = @At("HEAD"), cancellable = true)
    public void renderSpyglassOverlay(GuiGraphicsExtractor graphics, float scale, CallbackInfo ci) {
        if (Module.isEnabled(NoOverlay.class)) ci.cancel();
    }

    @Inject(method = "extractPortalOverlay", at = @At("HEAD"), cancellable = true)
    public void renderPortalOverlay(GuiGraphicsExtractor graphics, float alpha, CallbackInfo ci) {
        if (Module.isEnabled(NoOverlay.class)) ci.cancel();
    }

    @Inject(method = "extractVignette", at = @At("HEAD"), cancellable = true)
    public void renderVignetteOverlay(GuiGraphicsExtractor graphics, Entity camera, CallbackInfo ci) {
        if (Module.isEnabled(NoOverlay.class)) ci.cancel();
    }

    @Inject(method = "extractTextureOverlay", at = @At("HEAD"), cancellable = true)
    public void renderOverlay(GuiGraphicsExtractor graphics, Identifier texture, float alpha, CallbackInfo ci) {
        if (!texture.getPath().contains("pumpkinblur")) return;
        if (Module.isEnabled(NoOverlay.class)) ci.cancel();
    }

    @Inject(method = "displayScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    public void renderOverlay(GuiGraphicsExtractor graphics, Objective objective, CallbackInfo ci) {
        if (Module.isEnabled(NoScoreboard.class)) {
            ci.cancel();
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void renderHuds(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {
        if (PlayerUtils.invalid()) return;

        for (Hud hud : system.huds().values())
            if (hud.canRender()) hud.render(context, tickCounter.getGameTimeDeltaPartialTick(true));
    }
}