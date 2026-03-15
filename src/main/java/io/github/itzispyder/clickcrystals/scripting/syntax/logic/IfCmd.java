package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.components.Conditionals;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;

public class IfCmd extends ScriptCommand implements Global, ThenChainable {

    public IfCmd() {
        super("if");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        var condition = Conditionals.evaluate(AsCmd.getCurrentReferenceEntity(), args, 0);
        if (condition.getValue()) {
            executeWithThen(args);
        }

//        LogicalEvaluator logic = new LogicalEvaluator();
//        logic.getScope().importScope(ScriptReflectFactory.extract(ScriptPlayer.class));
//
//        ChatUtils.sendMessage("" + logic.getScope().isValidVar("player.health"));
//
//        if (logic.evalLogic(args)) {
//            executeWithThen(args);
//        }
    }
}
