package io.github.itzispyder.clickcrystals.gui.hud;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.CrystalPerSecondHud;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class ClickPerSecondHud implements HudRenderCallback {

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        Module cpsHud = Module.get(CrystalPerSecondHud.class);
        if (!cpsHud.isEnabled()) return;
        String text = "§f" + CrystalPerSecondHud.getCrystalPerSecond() + " §7c/s";

        final Window win = mc.getWindow();
        final int x = win.getScaledWidth() / 2;
        final int y = win.getScaledHeight() / 2;
        DrawableHelper.drawCenteredTextWithShadow(matrixStack, mc.textRenderer, text, x, y + 5,0);
    }
}
