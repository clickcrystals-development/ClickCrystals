package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.SenseValues;
import io.github.itzispyder.clickcrystals.scripting.SenseCompare;

/**
 * await_sense <task_name> <metric> <operator> <value> <timeout_ticks> then <CCS command>
 *
 * Non-blocking condition wait with bounded timeout.
 * Sets the named state key to waiting / matched / timeout.
 *
 * Example: await_sense anchor_ok target_anchor_charge gt 0 20 then print "Charged"
 * This checks client-observed state, not server-side action acknowledgement.
 */
public final class AwaitSenseCmd extends ScriptCommand {
    public AwaitSenseCmd() { super("await_sense"); }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (args.getSize() < 7 || !args.match(5, "then"))
            throw new IllegalArgumentException("Usage: await_sense <name> <metric> <op> <value> <timeout_ticks> then <command>");

        ClickScript script=args.getExecutorOrDef();
        String name=args.get(0).toString();
        if (!name.matches("[A-Za-z_][A-Za-z0-9_-]{0,54}"))
            throw new IllegalArgumentException("Invalid await name");
        String metric=args.get(1).toString();
        String operation=args.get(2).toString();
        String expected=args.get(3).toString();
        int timeout=args.get(4).toInt();
        if (timeout < 1 || timeout > 72000)
            throw new IllegalArgumentException("Timeout must be 1..72000 game ticks");
        String continuation=args.getAll(6).toString();
        if (continuation.isBlank()) throw new IllegalArgumentException("Await command cannot be empty");

        String taskKey="await_"+name;
        final int[] remaining={timeout};
        script.getScriptState().set(name,"waiting");
        script.scheduleCallbackTask(taskKey,1,1,()->{
            String observed=SenseValues.read(metric);
            if (observed!=null && SenseCompare.check(observed,operation,expected)) {
                script.cancelTask(taskKey);
                script.getScriptState().set(name,"matched");
                ClickScript.executeDynamic(script,continuation);
            } else if (--remaining[0]<=0) {
                script.cancelTask(taskKey);
                script.getScriptState().set(name,"timeout");
            }
        });
    }
}
