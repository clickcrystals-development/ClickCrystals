package io.github.itzispyder.clickcrystals.modules.scripts.syntax;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;

public class WaitRandomCmd extends ScriptCommand implements Global {

    public WaitRandomCmd() {
        super("wait_random");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        long delay = (long)(system.random.getRandomDouble(args.get(0).toDouble(), args.get(1).toDouble()) * 1000L);
        system.scheduler.runDelayedTask(() -> OnEventCmd.executeWithThen(args, 2), delay);
    }
}
