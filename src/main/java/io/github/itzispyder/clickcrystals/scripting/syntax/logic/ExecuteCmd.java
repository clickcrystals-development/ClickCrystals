package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;

// @Format execute {}
public class ExecuteCmd extends ScriptCommand {

    public ExecuteCmd() {
        super("execute");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        args.executeAll();
    }
}
