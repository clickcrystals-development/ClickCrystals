package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
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

        lastSlot = InvUtils.selected();
        HotbarUtils.search(OnEventCmd.parseItemPredicate(args.get(0).toString()));

        if (args.match(1, "then")) {
            args.executeAll(2);
        }
    }
}
