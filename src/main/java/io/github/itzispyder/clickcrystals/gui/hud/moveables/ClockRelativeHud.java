package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;

import java.time.LocalDateTime;

public class ClockRelativeHud extends Hud {

    public ClockRelativeHud() {
        super("clock-hud", 10, 105, 50, 12);
    }

    @Override
    public void render(DrawContext context) {
        renderBackdrop(context);

        String text = getTime();
        int x = getX() + getWidth() / 2;
        int y = getY() + (int)(getHeight() * 0.33);
        RenderUtils.drawCenteredText(context, text, x, y, 1.0F, true);
    }

    public String getTime() {
        LocalDateTime now = LocalDateTime.now();
        int hr = now.getHour();
        boolean bl;

        if (hr >= 12) {
            bl = true;
        }
        else {
            bl = hr != 0;
        }

        return (hr > 12 ? hr - 12 : (hr == 0 ? 12 : hr)) + ":" + formatTime(now.getMinute()) + (bl ? " pm" : " am");
    }

    public String formatTime(int i) {
        i = Math.abs(i);
        return i <= 9 ? "0" + i : "" + i;
    }

    @Override
    public boolean canRender() {
        return super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudClock.getVal());
    }
}
