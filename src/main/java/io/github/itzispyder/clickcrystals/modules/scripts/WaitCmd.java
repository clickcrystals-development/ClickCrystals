package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;

public class WaitCmd extends ScriptCommand implements Global {

    public WaitCmd() {
        super("wait");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        system.scheduler.runDelayedTask(() -> ClickScript.executeOneLine(args.getAll(1).stringValue()), (long)(args.get(0).doubleValue() * 1000L));
    }
}
