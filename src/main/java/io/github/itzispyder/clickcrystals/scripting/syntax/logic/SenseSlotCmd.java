package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.SenseValues;

/** sense_slot <1..9> <variable>: store the item ID at that hotbar position. */
public final class SenseSlotCmd extends ScriptCommand {
    public SenseSlotCmd() { super("sense_slot"); }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (args.getSize() != 2)
            throw new IllegalArgumentException("Usage: sense_slot <1..9> <variable>");
        int slot = args.get(0).toInt();
        String value = SenseValues.slotItem(slot);
        if (value != null)
            args.getExecutorOrDef().getScriptState().set(args.get(1).toString(), value);
        else
            args.getExecutorOrDef().getScriptState().clear(args.get(1).toString());
    }
}
