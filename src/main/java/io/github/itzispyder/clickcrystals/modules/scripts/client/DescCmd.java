package io.github.itzispyder.clickcrystals.modules.scripts.client;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;

public class DescCmd extends ScriptCommand {

    public DescCmd() {
        super("description");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        ModuleCmd.runOnCurrentScriptModule(m -> m.setDescription(args.getAll().toString()));
    }
}
