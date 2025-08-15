package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;

// @Format (loop_period|repeat_period) <int> <num> {}
public class LoopPeriodCmd extends ScriptCommand implements Global {

    public LoopPeriodCmd() {
        super("loop_period", "repeat_period");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        int times = args.get(0).toInt();
        long period = (long)(args.get(1).toDouble() * 1000L);
        system.scheduler.runRepeatingTask(() -> exc(args), 0, period, times);
    }

    public synchronized void exc(ScriptArgs args) {
        mc.execute(() -> args.executeAll(2));
    }
}
