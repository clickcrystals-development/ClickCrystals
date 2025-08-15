package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationResult;
import io.github.itzispyder.clickcrystals.scripting.components.Conditionals;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.misc.Scheduler;
import net.minecraft.entity.Entity;

import java.util.concurrent.atomic.AtomicReference;

public class WhileNotCmd extends ScriptCommand implements ThenChainable {

    public WhileNotCmd() {
        super("while_not", "!while");
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

        Entity ref = AsCmd.getCurrentReferenceEntity();
        AtomicReference<Scheduler.Task> task = new AtomicReference<>();
        task.set(system.scheduler.runRepeatingTask(() -> {
            try {
                ScriptArgs copy = new ScriptArgs(args.getExecutor(), args.args());
                ConditionEvaluationResult condition = Conditionals.evaluate(ref, copy, beginIndex);
                if (!condition.getValue()) {
                    executeOnClient(copy);
                }
                else if (task.get() != null) {
                    task.get().cancel();
                }
            }
            catch (Exception ignore) {}
        }, 0, period, Scheduler.INFINITE_ITERATIONS));
    }

    private void executeOnClient(ScriptArgs args) {
        mc.execute(() -> executeWithThen(args));
    }
}

