package io.github.itzispyder.clickcrystals.scripting.syntax.client;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;

// @Format (description|desc) "..."
public class DescCmd extends ScriptCommand {

    public DescCmd() {
        super("description", "desc");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        ModuleCmd.runOnCurrentScriptModule(m -> m.setDescription(args.getQuoteAndRemove()));
    }
}
