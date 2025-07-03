package io.github.itzispyder.clickcrystals.gui.hud.positionable;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class IconPositionableHud extends Hud {

    public IconPositionableHud() {
        super("icon-hud", 10, 25, 120, 30);
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        renderBackdrop(context);

        int ogw = getWidth();
        setWidth(getHeight());
        int x = getX() + 2;
        int y = getY() + 2;
        int w = getWidth() - 4;
        int h = getHeight() - 4;
        int cx = getCenter().getX();
        int cy = getCenter().getY();

        context.getMatrices().pushMatrix();

        context.getMatrices().rotateAbout((float)Math.toRadians(-10), cx, cy);
        RenderUtils.drawTexture(context,Tex.ICON, x, y, w, h);

        context.getMatrices().popMatrix();

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
}
