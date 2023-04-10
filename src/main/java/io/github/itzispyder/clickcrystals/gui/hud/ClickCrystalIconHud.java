package io.github.itzispyder.clickcrystals.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.itzispyder.clickcrystals.gui.screens.ClickCrystalMenuScreen;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.ClickCrystalHud;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class ClickCrystalIconHud implements HudRenderCallback {

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        Module hudModule = Module.get(ClickCrystalHud.class);
        if (!hudModule.isEnabled()) return;

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, ClickCrystalMenuScreen.SCREEN_TITLE_BANNER_TEXTURE);
        DrawableHelper.drawTexture(matrixStack,20,40,0,0,120,30,120,30);
    }
}
