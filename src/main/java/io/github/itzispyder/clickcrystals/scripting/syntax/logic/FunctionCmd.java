package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;

// @Format (function|func) ...
public class FunctionCmd extends ScriptCommand {

    public FunctionCmd() {
        super("function", "func");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        ClickScript exe = args.getExecutor();
        String name = args.get(0).toString();
        ClickScript.executeDynamic(exe, exe.getFunction(name));
    }
}
