package io.github.itzispyder.clickcrystals.modules.scripts.macros;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptParser;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;

public class SwitchCmd extends ScriptCommand {

    public static int lastSlot = -1;

    public SwitchCmd() {
        super("switch");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (lastSlot != -1 && args.match(0, "back")) {
            InvUtils.select(lastSlot);
            return;
        }

        ScriptArgs.Arg first = args.get(0);

        if (first.toString().matches("\\d+")) {
            int slot = first.toInt() - 1;
            if (slot < 0 || slot > 8)
                throw new IllegalArgumentException(first + " is not a valid hotbar slot.");
            InvUtils.select(slot);
        }
        else {
            lastSlot = InvUtils.selected();
            HotbarUtils.search(ScriptParser.parseItemPredicate(first.toString()));
        }

        if (args.match(1, "then")) {
            args.executeAll(2);
        }
    }
}
