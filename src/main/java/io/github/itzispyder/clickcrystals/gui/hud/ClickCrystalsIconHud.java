package io.github.itzispyder.clickcrystals.gui.hud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.IconHud;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;

@Environment(EnvType.CLIENT)
public class ClickCrystalsIconHud implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        Module hudModule = Module.get(IconHud.class);
        if (!hudModule.isEnabled()) return;

        context.drawTexture(GuiTextures.ICON_BANNER,20,35,0,0,100,25,100,25);
    }
}