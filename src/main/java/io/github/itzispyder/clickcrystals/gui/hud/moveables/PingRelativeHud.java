package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.RelativeHud;
import io.github.itzispyder.clickcrystals.util.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class PingRelativeHud extends RelativeHud {

    public PingRelativeHud() {
        super("ping-hud", 0.033, 0.30, 50, 12);
    }

    @Override
    public void render(DrawContext context) {
        renderBackdrop(context);

        String text = PlayerUtils.getPing() + " ms";
        int x = getX() + getWidth() / 2;
        int y = getY() + (int)(getHeight() * 0.33);
        RenderUtils.drawCenteredText(context, text, x, y, 1.0F, true);
    }
}
