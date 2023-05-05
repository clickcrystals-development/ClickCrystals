package io.github.itzispyder.clickcrystals.gui.hud;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.CrystPerSec;
import io.github.itzispyder.clickcrystals.util.DrawableUtils;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class ClickPerSecondHud implements HudRenderCallback {

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        Module cpsHud = Module.get(CrystPerSec.class);
        if (!cpsHud.isEnabled()) return;
        String text = "§f" + CrystPerSec.getCrystalPerSecond() + " §7c/s";

        final Window win = mc.getWindow();
        final int x = win.getScaledWidth() / 2;
        final int y = win.getScaledHeight() / 2;
        DrawableUtils.drawCenteredText(matrixStack, text, x, y + 5, true);
    }
}
