package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.RelativeHud;
import io.github.itzispyder.clickcrystals.scheduler.RepeatingTask;
import io.github.itzispyder.clickcrystals.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;

import java.time.LocalDateTime;

public class ClockRelativeHud extends RelativeHud {

    public static boolean blink = true;
    public static final RepeatingTask BLINK_TASK = new RepeatingTask(() -> {
        blink = !blink;
    }, 20);

    public ClockRelativeHud() {
        super("clock-hud", 0.033, 0.4, 50, 12);
        //Scheduler.getTasks().add(BLINK_TASK);
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
        return formatTime(now.getHour()) + (blink ? ":" : " ") + formatTime(now.getMinute());
    }

    public String formatTime(int i) {
        i = Math.abs(i);
        String formatted = i <= 9 ? "0" + i : "" + i;
        return formatted.equals("00") ? "12" : formatted;
    }
}
