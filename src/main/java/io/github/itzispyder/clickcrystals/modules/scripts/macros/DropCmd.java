package io.github.itzispyder.clickcrystals.modules.scripts.macros;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;

public class DropCmd extends ScriptCommand {

    public DropCmd() {
        super("drop");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (args.match(0, "all")) {
            InteractionUtils.inputDropFull();
        }
        else {
            int times = MathUtils.clamp(args.get(0).toInt(), 0, 64);
            for (int i = 0; i < times; i++) {
                InteractionUtils.inputDrop();
            }
        }

        if (args.match(1, "then")) {
            args.executeAll(2);
        }
    }
}
