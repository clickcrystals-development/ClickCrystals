package io.github.itzispyder.clickcrystals.modules.scripts.macros;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;

public class SwapCmd extends ScriptCommand {

    public SwapCmd() {
        super("swap");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (PlayerUtils.playerNotNull()) {
            HotbarUtils.swapWithOffhand();

            if (args.match(0, "then")) {
                args.executeAll(1);
            }
        }
    }
}
