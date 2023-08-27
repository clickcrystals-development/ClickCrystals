package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.TextHud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;

import java.time.LocalDateTime;

public class ClockRelativeHud extends TextHud {

    public ClockRelativeHud() {
        super("clock-hud", 10, 105, 50, 12);
    }

    @Override
    public String getText() {
        LocalDateTime now = LocalDateTime.now();
        int hr = now.getHour();

        if (Module.getFrom(InGameHuds.class, m -> m.hudClockHourDisplay.getVal()) == InGameHuds.ClockDisplay.HOUR_12) {
            return (hr > 12 ? hr - 12 : (hr == 0 ? 12 : hr)) + ":" + formatTime(now.getMinute()) + (hr >= 12 ? " pm" : " am");
        }
        else {
            return hr + ":" + formatTime(now.getMinute());
        }
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
