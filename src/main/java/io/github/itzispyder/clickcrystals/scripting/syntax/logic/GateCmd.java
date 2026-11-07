package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;

/** gate <name> <milliseconds> then <command> */
public final class GateCmd extends ScriptCommand {
    public GateCmd() { super("gate"); }
    @Override public void onCommand(ScriptCommand command,String line,ScriptArgs args) {
        if (!args.match(2,"then")) throw new IllegalArgumentException("Use gate <key> <milliseconds> then <command>");
        if (args.getExecutorOrDef().getScriptState().acquire(args.get(0).toString(),Long.parseLong(args.get(1).toString())))
            ClickScript.executeDynamic(args.getExecutorOrDef(),args.getAll(3).toString());
    }
}
