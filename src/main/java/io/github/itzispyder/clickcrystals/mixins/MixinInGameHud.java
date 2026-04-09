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
public abstract class MixinInGameHud implements Global {

    @ModifyArgs(method = "renderArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"))
    private static void renderArmor(Args args) {
        if (Module.isEnabled(HealthAsBar.class))
            args.set(3, Minecraft.getInstance().getWindow().getGuiScaledHeight() - 50);
    }

    @Inject(method = "renderHearts", at = @At("HEAD"), cancellable = true)
    public void renderHealthBar(GuiGraphics context, Player player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
        HealthAsBar hpBar = Module.get(HealthAsBar.class);
        if (hpBar.isEnabled()) {
            hpBar.renderHealthBar(context, x, y, maxHealth, lastHealth, health, absorption);
            ci.cancel();
        }
    }

    @Inject(method = "renderSpyglassOverlay", at = @At("HEAD"), cancellable = true)
    public void renderSpyglassOverlay(GuiGraphics context, float scale, CallbackInfo ci) {
        if (Module.isEnabled(NoOverlay.class)) ci.cancel();
    }

    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    public void renderPortalOverlay(GuiGraphics context, float nauseaStrength, CallbackInfo ci) {
        if (Module.isEnabled(NoOverlay.class)) ci.cancel();
    }

    @Inject(method = "renderVignette", at = @At("HEAD"), cancellable = true)
    public void renderVignetteOverlay(GuiGraphics context, Entity entity, CallbackInfo ci) {
        if (Module.isEnabled(NoOverlay.class)) ci.cancel();
    }

    @Inject(method = "renderTextureOverlay", at = @At("HEAD"), cancellable = true)
    public void renderOverlay(GuiGraphics context, Identifier texture, float opacity, CallbackInfo ci) {
        if (!texture.getPath().contains("pumpkinblur")) return;
        if (Module.isEnabled(NoOverlay.class)) ci.cancel();
    }

    @Inject(method = "displayScoreboardSidebar(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/scores/Objective;)V", at = @At("HEAD"), cancellable = true)
    public void renderOverlay(GuiGraphics context, Objective objective, CallbackInfo ci) {
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