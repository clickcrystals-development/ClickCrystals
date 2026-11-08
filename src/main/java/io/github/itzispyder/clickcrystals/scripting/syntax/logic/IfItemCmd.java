package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.SenseCompare;
import io.github.itzispyder.clickcrystals.scripting.SenseValues;

/** if_item <minecraft:item> <comparison> <count> then <CCS command> */
public final class IfItemCmd extends ScriptCommand {
    public IfItemCmd() { super("if_item"); }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (args.getSize() < 6 || !args.match(3, "then"))
            throw new IllegalArgumentException("Usage: if_item <item_id> <op> <count> then <command>");
        int count = SenseValues.countItem(args.get(0).toString());
        if (count < 0) return;
        if (SenseCompare.check(Integer.toString(count), args.get(1).toString(), args.get(2).toString()))
            ClickScript.executeDynamic(args.getExecutorOrDef(), args.getAll(4).toString());
    }
}
