package io.github.itzispyder.clickcrystals.modules.scripts.macros;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.modules.scripts.InputType;
import io.github.itzispyder.clickcrystals.modules.scripts.ThenChainable;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;

public class InputCmd extends ScriptCommand implements Global, ThenChainable {

    public InputCmd() {
        super("input");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        InputType a = args.get(0).toEnum(InputType.class, null);
        if (a != InputType.KEY) {
            a.run();
            executeWithThen(args, 1);
        }
        else {
            InteractionUtils.pressKeyExtendedName(args.get(1).toString());
            executeWithThen(args, 2);
        }
    }

}
