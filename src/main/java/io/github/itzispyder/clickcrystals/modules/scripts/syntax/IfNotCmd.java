package io.github.itzispyder.clickcrystals.modules.scripts.syntax;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;

public class IfNotCmd extends ScriptCommand implements Global {

    public IfNotCmd() {
        super("if_not");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        int beginIndex = 0;
        IfCmd.ConditionType type = args.get(beginIndex).toEnum(IfCmd.ConditionType.class, null);
        var condition = IfCmd.parseCondition(type, args, beginIndex);

        if (!condition.left) {
            OnEventCmd.executeWithThen(args, condition.right);
        }
    }
}
