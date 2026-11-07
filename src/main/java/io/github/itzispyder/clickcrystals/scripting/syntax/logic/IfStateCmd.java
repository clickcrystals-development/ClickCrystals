package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import java.util.Locale;

/**
 * if_state key eq value then command
 * if_state key empty|nonempty|true|false then command
 */
public final class IfStateCmd extends ScriptCommand {
    public IfStateCmd() { super("if_state"); }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        String key = args.get(0).toString();
        String op = args.get(1).toString().toLowerCase(Locale.ROOT);
        boolean unary = op.equals("empty") || op.equals("nonempty")
                || op.equals("not_empty") || op.equals("true") || op.equals("false");
        int thenIndex = unary ? 2 : 3;
        if (!args.match(thenIndex, "then") || args.getSize() <= thenIndex + 1)
            throw new IllegalArgumentException("Use if_state <key> <op> [value] then <command>");

        String value = unary ? "" : args.get(2).toString();
        if (args.getExecutorOrDef().getScriptState().matches(key, op, value))
            ClickScript.executeDynamic(args.getExecutorOrDef(), args.getAll(thenIndex + 1).toString());
    }
}
