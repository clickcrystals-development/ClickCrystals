package io.github.itzispyder.clickcrystals.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.itzispyder.clickcrystals.gui.TexturesIdentifiers;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.misc.IconHud;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class ClickCrystalIconHud implements HudRenderCallback {

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        Module hudModule = Module.get(IconHud.class);
        if (!hudModule.isEnabled()) return;

        RenderSystem.setShaderTexture(0, TexturesIdentifiers.SCREEN_BANNER_TEXTURE);
        DrawableHelper.drawTexture(matrixStack,20,35,0,0,100,25,100,25);
    }
}
