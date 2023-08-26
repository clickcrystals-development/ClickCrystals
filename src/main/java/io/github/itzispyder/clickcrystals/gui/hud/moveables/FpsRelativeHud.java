package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.RelativeHud;
import io.github.itzispyder.clickcrystals.util.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class FpsRelativeHud extends RelativeHud {

    public FpsRelativeHud() {
        super(0.033, 0.35, 50, 12);
    }

    @Override
    public void render(DrawContext context) {
        renderBackdrop(context);

        String text = PlayerUtils.getFps() + " fps";
        int x = getX() + getWidth() / 2;
        int y = getY() + (int)(getHeight() * 0.33);
        RenderUtils.drawCenteredText(context, text, x, y, 1.0F, true);
    }
}
