package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;

// @Format (throw|error) "..."
public class ThrowCmd extends ScriptCommand {

    public ThrowCmd() {
        super("throw", "error");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        throw new RuntimeException(args.getReader().nextQuote());
    }
}
