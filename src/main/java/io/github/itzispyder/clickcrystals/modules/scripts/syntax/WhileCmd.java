package io.github.itzispyder.clickcrystals.modules.scripts.syntax;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.modules.scripts.Conditionals;
import io.github.itzispyder.clickcrystals.modules.scripts.ThenChainable;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.misc.Scheduler;
import net.minecraft.entity.Entity;

import java.util.concurrent.atomic.AtomicReference;

public class WhileCmd extends ScriptCommand implements ThenChainable {

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

        Entity ref = AsCmd.getCurrentReferenceEntity();
        AtomicReference<Scheduler.Task> task = new AtomicReference<>();
        task.set(system.scheduler.runRepeatingTask(() -> {
            try {
                var condition = Conditionals.parseCondition(ref, args, beginIndex);
                if (condition.value()) {
                    executeOnClient(args, condition.nextIndex());
                }
                else if (task.get() != null) {
                    task.get().cancel();
                }
            }
            catch (Exception ignore) {}
        }, 0, period, Scheduler.INFINITE_ITERATIONS));
    }

    private void executeOnClient(ScriptArgs args, int beginIndex) {
        mc.execute(() -> executeWithThen(args, beginIndex));
    }
}

