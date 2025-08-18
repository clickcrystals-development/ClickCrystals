package io.github.itzispyder.clickcrystals.scripting.syntax.macros;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.syntax.InputType;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;

// @Format input <input>
// @Format input key ...
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
