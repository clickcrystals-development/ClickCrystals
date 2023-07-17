package io.github.itzispyder.clickcrystals.gui.hud;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.misc.ArmorHud;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;

public class ArmorItemHud implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext context, float delta) {
        ArmorHud hud = Module.get(ArmorHud.class);

        if (hud.isEnabled()) {
            hud.onRender(context);
        }
    }
}
