package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;

/**
 * once <state_key> then <command>
 * Runs at most once until 'state clear <state_key>' or 'state reset'.
 */
public final class OnceCmd extends ScriptCommand {
    public OnceCmd() { super("once"); }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (args.getSize() < 4 || !args.match(1, "then"))
            throw new IllegalArgumentException("Usage: once <variable> then <command>");
        var state = args.getExecutorOrDef().getScriptState();
        String key = args.get(0).toString();
        if (!state.get(key).isEmpty()) return;
        state.set(key, "true");
        ClickScript.executeDynamic(args.getExecutorOrDef(), args.getAll(2).toString());
    }
}
