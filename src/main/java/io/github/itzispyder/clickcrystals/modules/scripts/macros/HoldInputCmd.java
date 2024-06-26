package io.github.itzispyder.clickcrystals.modules.scripts.macros;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.events.listeners.TickEventListener;
import io.github.itzispyder.clickcrystals.modules.scripts.InputType;
import io.github.itzispyder.clickcrystals.modules.scripts.ThenChainable;

public class HoldInputCmd extends ScriptCommand implements ThenChainable {

    public HoldInputCmd() {
        super("hold_input");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (args.match(0, "cancel")) {
            TickEventListener.cancelTickInputs();
            return;
        }

        InputType a = args.get(0).toEnum(InputType.class, null);
        long holdTime = (long)(args.get(1).toDouble() * 1000L);

        if (a.isDummy())
            throw new IllegalArgumentException("unsupported operation, input '%s' cannot be held".formatted(a));

        a.runFor(holdTime);
        executeWithThen(args, 2);
    }
}
