package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;

// @Format (loop|repeat) <int> {}
public class LoopCmd extends ScriptCommand {

    public LoopCmd() {
        super("loop", "repeat");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        int times = args.get(0).toInt();
        for (int i = 0; i < times; i++)
            args.executeAll(1);
    }
}
