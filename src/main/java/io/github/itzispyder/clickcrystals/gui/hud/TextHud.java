package io.github.itzispyder.clickcrystals.gui.hud;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public abstract class TextHud extends Hud {

    public TextHud(String id, int x, int y, int width, int height) {
        super(id, x, y, width, height);
    }

    public abstract String getText();

    @Override
    public void render(DrawContext context, float tickDelta) {
        renderBackdrop(context);

        String text = getText();
        setWidth(mc.textRenderer.getWidth(text) + 6);

        int x = getX() + getWidth() / 2;
        int y = getY() + (int)(getHeight() * 0.33);
        RenderUtils.drawCenteredText(context, text, x, y, 1.0F, true);
    }

    @Override
    public void revertDimensions() {
        setX(getDefaultDimension().x);
        setY(getDefaultDimension().y);
    }

    @Override
    public int getArgb() {
        return Module.getFrom(InGameHuds.class, m -> m.getArgb());
    }
}
