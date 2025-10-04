package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;

// @Format print "..."
public class PrintCmd extends ScriptCommand implements ThenChainable {

    public PrintCmd() {
        super("print");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        var read = args.getReader();
        system.println(read.nextQuote());
        read.executeThenChain();
    }
}
