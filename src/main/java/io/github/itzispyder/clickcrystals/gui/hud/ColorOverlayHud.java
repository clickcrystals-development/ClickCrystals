package io.github.itzispyder.clickcrystals.gui.hud;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.BrightOrange;
import io.github.itzispyder.clickcrystals.modules.modules.TotemOverlay;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class ColorOverlayHud implements HudRenderCallback {

    @Override
    public void onHudRender(MatrixStack matrices, float tickDelta) {
        final Module totemOverlay = Module.get(TotemOverlay.class);
        final Module brightOrange = Module.get(BrightOrange.class);

        if (totemOverlay.isEnabled() && !HotbarUtils.isTotemed()) {
            this.renderColor(matrices, 0x30FF0000);
        }

        if (brightOrange.isEnabled()) {
            this.renderColor(matrices, 0x30BE8100);
        }
    }

    private void renderColor(MatrixStack matrices, int color) {
        final Window win = mc.getWindow();
        DrawableHelper.fill(matrices, 0, 0, win.getWidth(), win.getHeight(), color);
    }
}
