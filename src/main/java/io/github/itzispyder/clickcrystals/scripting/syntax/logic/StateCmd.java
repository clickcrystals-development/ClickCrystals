package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;

/** state set <name> <value>; state add <name> <number>; state clear <name>; state reset */
public final class StateCmd extends ScriptCommand {
    public StateCmd() { super("state"); }
    @Override public void onCommand(ScriptCommand command,String line,ScriptArgs args) {
        var state=args.getExecutorOrDef().getScriptState();
        switch(args.get(0).toString()) {
            case "set" -> state.set(args.get(1).toString(),args.getAll(2).toString());
            case "add" -> state.add(args.get(1).toString(),args.get(2).toDouble());
            case "clear" -> state.clear(args.get(1).toString());
            case "reset" -> state.reset();
            default -> throw new IllegalArgumentException("Use state set|add|clear|reset");
        }
    }
}
