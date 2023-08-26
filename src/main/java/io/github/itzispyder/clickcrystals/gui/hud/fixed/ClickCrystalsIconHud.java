package io.github.itzispyder.clickcrystals.gui.hud.fixed;

import io.github.itzispyder.clickcrystals.gui.GuiTextures;
import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.IconHud;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;

@Environment(EnvType.CLIENT)
public class ClickCrystalsIconHud extends Hud {

    public ClickCrystalsIconHud() {

    }

    @Override
    public void render(DrawContext context) {
        Module hudModule = Module.get(IconHud.class);
        if (!hudModule.isEnabled()) return;

        context.drawTexture(GuiTextures.ICON_BANNER,20,35,0,0,100,25,100,25);
    }
}