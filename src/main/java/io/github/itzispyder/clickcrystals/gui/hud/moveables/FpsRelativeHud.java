package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class FpsRelativeHud extends Hud {

    public FpsRelativeHud() {
        super("fps-hud", 10, 90, 50, 12);
    }

    @Override
    public void render(DrawContext context) {
        renderBackdrop(context);

        String text = PlayerUtils.getFps() + " fps";
        int x = getX() + getWidth() / 2;
        int y = getY() + (int)(getHeight() * 0.33);
        RenderUtils.drawCenteredText(context, text, x, y, 1.0F, true);
    }

    @Override
    public boolean canRender() {
        return super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudFps.getVal());
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
