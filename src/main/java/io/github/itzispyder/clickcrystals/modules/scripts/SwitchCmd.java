package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;

public class SwitchCmd extends ScriptCommand {

    public SwitchCmd() {
        super("switch");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        HotbarUtils.search(OnEventCmd.parsePredicate(args.get(0).stringValue()));
    }
}
