package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.SenseCompare;
import io.github.itzispyder.clickcrystals.scripting.SenseValues;

/**
 * if_sense <metric> <operator> [value] then <CCS command>
 * unless_sense ... executes only on a valid *false* predicate.
 */
public final class IfSenseCmd extends ScriptCommand {
    private final boolean invert;

    public IfSenseCmd() { this("if_sense", false); }

    public IfSenseCmd(String name, boolean invert) {
        super(name);
        this.invert = invert;
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (args.getSize() < 4)
            throw new IllegalArgumentException("Usage: if_sense <metric> <op> [value] then <command>");
        String operation = args.get(1).toString();
        boolean unary = SenseCompare.isUnary(operation);
        int then = unary ? 2 : 3;
        if (!args.match(then, "then") || args.getSize() <= then + 1)
            throw new IllegalArgumentException("Use 'then' before a command");
        String actual = SenseValues.read(args.get(0).toString());
        if (actual == null) return;
        String expected = unary ? "" : args.get(2).toString();
        boolean matched = SenseCompare.check(actual, operation, expected);
        if (invert ? !matched : matched)
            ClickScript.executeDynamic(args.getExecutorOrDef(), args.getAll(then + 1).toString());
    }
}
