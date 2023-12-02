package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.RotationAxis;

public class IconRelativeHud extends Hud {

    public IconRelativeHud() {
        super("icon-hud", 10, 30, 120, 30);
    }

    @Override
    public void render(DrawContext context) {
        renderBackdrop(context);

        int ogw = getWidth();
        setWidth(getHeight());
        int x = getX() + 2;
        int y = getY() + 2;
        int w = getWidth() - 4;
        int h = getHeight() - 4;
        int cx = getCenter().getX();
        int cy = getCenter().getY();

        context.getMatrices().push();

        context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-10.0F), cx, cy, 0);
        context.drawTexture(Tex.ICON, x, y, 0, 0, w, h, w, h);

        context.getMatrices().pop();

        setWidth(ogw);
        RenderUtils.drawText(context, "§l§oClickCrystals", getX() + getHeight(), getY() + (int)(getHeight() * 0.33), 1.0F, true);
    }

    @Override
    public boolean canRender() {
        return super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudIcon.getVal());
    }

    @Override
    public int getArgb() {
        return Module.getFrom(InGameHuds.class, m -> m.getArgb());
    }

    @Override
    public boolean canRenderBorder() {
        return Module.getFrom(InGameHuds.class, m -> m.renderHudBorders.getVal());
    }
}
