package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;

// @Format wait_random <num> <num> {}
public class WaitRandomCmd extends ScriptCommand implements Global, ThenChainable {

    public WaitRandomCmd() {
        super("wait_random");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (args.getSize() == 2)
            throw new IllegalArgumentException("You need to specify what to do after the wait time! example: \"wait_random 0.05 0.1 input attack\"");
        long delay = (long)(system.random.getRandomDouble(args.get(0).toDouble(), args.get(1).toDouble()) * 1000L);
        system.scheduler.runDelayedTask(() -> executeWithThen(args, 2), delay);
    }
}
