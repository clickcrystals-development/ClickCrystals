package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;

public class WaitCmd extends ScriptCommand implements Global {

    public WaitCmd() {
        super("wait");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        system.scheduler.runDelayedTask(() -> OnEventCmd.executeWithThen(args, 1), (long)(args.get(0).toDouble() * 1000L));
    }
}
