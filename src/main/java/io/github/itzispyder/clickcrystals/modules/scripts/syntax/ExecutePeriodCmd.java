package io.github.itzispyder.clickcrystals.modules.scripts.syntax;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptParser;
import io.github.itzispyder.clickcrystals.client.clickscript.components.CommandLine;

import java.util.LinkedList;
import java.util.Queue;

public class ExecutePeriodCmd extends ScriptCommand {

    public ExecutePeriodCmd() {
        super("execute_period");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        long period = (long)(args.get(0).toDouble() * 1000);
        String all = args.getAll(1).toString();

        if (all.length() < 2) {
            return;
        }

        all = all.substring(1, all.length() - 1);
        Queue<CommandLine> lines = new LinkedList<>(ScriptParser.parse(all));

        if (lines.isEmpty())
            return;

        system.scheduler.runRepeatingTask(() -> executeOnClient(args, lines.poll()), 0, period, lines.size());
    }

    private void executeOnClient(ScriptArgs args, CommandLine script) {
        if (script != null)
            mc.execute(() -> script.executeDynamic(args.getExecutorOrDef()));
    }
}
