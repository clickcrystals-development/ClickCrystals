package io.github.itzispyder.clickcrystals.modules.scripts.syntax;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.misc.Scheduler;

public class WhileCmd extends ScriptCommand {

    public WhileCmd() {
        super("while");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        long period;
        int beginIndex;

        if (StringUtils.isNumber(args.get(0).toString())) {
            period = (long)(args.get(0).toDouble() * 1000);
            beginIndex = 1;
        }
        else {
            period = 50;
            beginIndex = 0;
        }

        system.scheduler.runRepeatingTask(() -> {
            try {
                var condition = IfCmd.parseCondition(args, beginIndex);
                if (condition.left) {
                    executeOnClient(args, condition.right);
                }
            }
            catch (Exception ignore) {}
        }, 0, period, Scheduler.INFINITE_ITERATIONS);
    }

    private void executeOnClient(ScriptArgs args, int beginIndex) {
        mc.execute(() -> OnEventCmd.executeWithThen(args, beginIndex));
    }
}

