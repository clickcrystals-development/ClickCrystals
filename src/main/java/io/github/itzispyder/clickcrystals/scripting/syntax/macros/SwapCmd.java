package io.github.itzispyder.clickcrystals.scripting.syntax.macros;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;

// @Format swap
public class SwapCmd extends ScriptCommand {

    public SwapCmd() {
        super("swap");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (PlayerUtils.valid()) {
            HotbarUtils.swapWithOffhand();

            if (args.match(0, "then")) {
                args.executeAll(1);
            }
        }
    }
}
