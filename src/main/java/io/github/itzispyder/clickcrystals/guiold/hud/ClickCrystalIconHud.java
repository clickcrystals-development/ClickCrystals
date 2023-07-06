package io.github.itzispyder.clickcrystals.guiold.hud;

import io.github.itzispyder.clickcrystals.gui.TexturesIdentifiers;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.IconHud;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;

@Environment(EnvType.CLIENT)
public class ClickCrystalIconHud implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        Module hudModule = Module.get(IconHud.class);
        if (!hudModule.isEnabled()) return;

        context.drawTexture(TexturesIdentifiers.SCREEN_BANNER_TEXTURE,20,35,0,0,100,25,100,25);
    }
}
