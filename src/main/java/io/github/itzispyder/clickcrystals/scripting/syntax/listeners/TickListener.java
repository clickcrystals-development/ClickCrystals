package io.github.itzispyder.clickcrystals.scripting.syntax.listeners;

import io.github.itzispyder.clickcrystals.events.Event;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;

public class TickListener {

    public static TickListener fromScript(ScriptArgs args, ThenChainable command) {
        int period = 0;
        int execOffset = 1;

        if (args.get(execOffset).isNum())
            period = args.get(execOffset++).toInt();

        int begin = execOffset;
        return new TickListener(period, () -> command.executeWithThen(args, begin));
    }

    private final int period;
    private int tick;
    private final Runnable event;

    public TickListener(int period, Runnable event) {
        this.period = period;
        this.event = event;
    }

    public void pass(Event e) {
        if (period == 0 || tick++ % period == 0)
            event.run();
    }
}
