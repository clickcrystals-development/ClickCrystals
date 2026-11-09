package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;

/** reset_once <key>: reset a single event latch for another manual trigger. */
public final class StateResetCmd extends ScriptCommand {
    public StateResetCmd() { super("reset_once"); }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (args.getSize() != 1)
            throw new IllegalArgumentException("Usage: reset_once <variable>");
        args.getExecutorOrDef().getScriptState().clear(args.get(0).toString());
    }
}
