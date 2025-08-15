package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.components.Conditionals;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;

public class IfNotCmd extends ScriptCommand implements Global, ThenChainable {

    public IfNotCmd() {
        super("if_not", "!if");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        var condition = Conditionals.evaluate(AsCmd.getCurrentReferenceEntity(), args, 0);
        if (!condition.getValue()) {
            executeWithThen(args);
        }
    }
}
