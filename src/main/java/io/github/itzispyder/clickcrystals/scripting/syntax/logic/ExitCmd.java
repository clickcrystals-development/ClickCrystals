package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;

// @Format exit <int>
public class ExitCmd extends ScriptCommand {

    public ExitCmd() {
        super("exit");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        System.exit(args.get(0).toInt());
    }
}
