package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;

// @Format wait <num> {}
public class WaitCmd extends ScriptCommand implements Global, ThenChainable {

    public WaitCmd() {
        super("wait");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (args.getSize() == 1)
            throw new IllegalArgumentException("You need to specify what to do after the wait time! example: \"wait 0.05 input attack\"");
        long delay = (long)(args.get(0).toDouble() * 1000L);
        system.scheduler.runDelayedTask(() -> executeWithThen(args, 1), delay);
    }
}
