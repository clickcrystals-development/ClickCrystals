package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.util.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class PingRelativeHud extends Hud {

    public PingRelativeHud() {
        super("ping-hud", 10, 75, 50, 12);
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
