package io.github.itzispyder.clickcrystals.gui.hud;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.BrightOrange;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.TotemOverlay;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.InventoryUtils;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.minecraft.item.Items;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class ColorOverlayHud implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        final Module totemOverlay = Module.get(TotemOverlay.class);
        final Module brightOrange = Module.get(BrightOrange.class);

        if (totemOverlay.isEnabled() && !HotbarUtils.isTotemed() && InventoryUtils.has(Items.TOTEM_OF_UNDYING)) {
            this.renderColor(context, 0x30FF0000);
        }

        if (brightOrange.isEnabled()) {
            this.renderColor(context, 0x30BE8100);
        }
    }

    private void renderColor(DrawContext context, int color) {
        final Window win = mc.getWindow();
        context.fill(0, 0, win.getWidth(), win.getHeight(), color);
    }
}
