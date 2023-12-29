package io.github.itzispyder.clickcrystals.modules.scripts.syntax;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;

public class ElseCmd extends ScriptCommand {

    public ElseCmd() {
        super("else");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (IfCmd.previousOutput.get() == null) {
            throw new IllegalArgumentException("else statement cannot be called not following an if statement");
        }

        if (!IfCmd.previousOutput.get()) {
            IfCmd.previousOutput.set(null);
            args.executeAll();
        }
    }
}
