package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.SenseValues;

/**
 * sense_item minecraft:totem_of_undying variable
 * Counts item quantity in hotbar slots 1-9; does not modify inventory.
 */
public final class SenseItemCmd extends ScriptCommand {
    public SenseItemCmd() { super("sense_item"); }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (args.getSize() != 2)
            throw new IllegalArgumentException("Usage: sense_item <item_id> <variable>");
        String item = args.get(0).toString();
        int count = SenseValues.countItem(item);
        if (count >= 0)
            args.getExecutorOrDef().getScriptState().set(args.get(1).toString(), Integer.toString(count));
        else
            args.getExecutorOrDef().getScriptState().clear(args.get(1).toString());
    }
}
