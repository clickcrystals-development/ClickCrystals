package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;

/** if_state <name> eq|ne|gt|ge|lt|le <value> then <command> */
public final class IfStateCmd extends ScriptCommand {
    public IfStateCmd() { super("if_state"); }
    @Override public void onCommand(ScriptCommand command,String line,ScriptArgs args) {
        if (!args.match(3,"then")) throw new IllegalArgumentException("Use if_state <key> <operator> <value> then <command>");
        if (args.getExecutorOrDef().getScriptState().matches(args.get(0).toString(),args.get(1).toString(),args.get(2).toString()))
            ClickScript.executeDynamic(args.getExecutorOrDef(),args.getAll(4).toString());
    }
}
