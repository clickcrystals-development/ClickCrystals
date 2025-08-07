package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.syntax.Conditionals;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;

public class IfNotCmd extends ScriptCommand implements Global, ThenChainable {

    public IfNotCmd() {
        super("if_not", "!if");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        var condition = Conditionals.parseCondition(AsCmd.getCurrentReferenceEntity(), args, 0);
        if (!condition.value()) {
            executeWithThen(args, condition.nextIndex());
        }
    }
}
