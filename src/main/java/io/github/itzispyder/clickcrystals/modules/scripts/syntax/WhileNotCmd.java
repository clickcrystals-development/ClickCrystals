package io.github.itzispyder.clickcrystals.modules.scripts.syntax;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;

import java.util.concurrent.CompletableFuture;

public class WhileNotCmd extends ScriptCommand {

    public WhileNotCmd() {
        super("while_not");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        double period = args.get(0).toDouble();
        long millis = (long)(period * 1000);
        int beginIndex = 1;
        IfCmd.ConditionType type = args.get(beginIndex).toEnum(IfCmd.ConditionType.class, null);

        CompletableFuture.runAsync(() -> {
            var condition = IfCmd.parseCondition(type, args, beginIndex);
            while (!condition.left) {
                try {
                    executeOnClient(args, condition.right);
                    Thread.sleep(millis);
                    condition = IfCmd.parseCondition(type, args, beginIndex);
                }
                catch (Exception ignore) {}
            }
        });
    }

    private void executeOnClient(ScriptArgs args, int beginIndex) {
        mc.execute(() -> OnEventCmd.executeWithThen(args, beginIndex));
    }
}

