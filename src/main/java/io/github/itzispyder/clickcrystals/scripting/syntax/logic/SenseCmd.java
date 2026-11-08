package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.SenseValues;

/** sense <metric> <state-key>: read one local game snapshot into script state. */
public final class SenseCmd extends ScriptCommand {
    public SenseCmd() { super("sense", "read_sense"); }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (args.getSize() != 2)
            throw new IllegalArgumentException("Usage: sense <metric> <variable>");
        String value = SenseValues.read(args.get(0).toString());
        if (value != null)
            args.getExecutorOrDef().getScriptState().set(args.get(1).toString(), value);
        else
            args.getExecutorOrDef().getScriptState().clear(args.get(1).toString());
    }
}
