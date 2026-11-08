package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;

/**
 * task after <name> <ticks> then <command>
 * task every <name> <ticks> then <command>
 * task cancel <name>
 * task cancel_all
 * task count <state-variable>
 *
 * Time is measured in game ticks; not milliseconds.
 */
public final class TaskCmd extends ScriptCommand {
    public TaskCmd() { super("task"); }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        ClickScript script = args.getExecutorOrDef();
        switch (args.get(0).toString().toLowerCase(java.util.Locale.ROOT)) {
            case "after", "every" -> {
                if (args.getSize() < 5 || !args.match(3, "then"))
                    throw new IllegalArgumentException("Usage: task after|every <name> <ticks> then <command>");
                String name = args.get(1).toString();
                int ticks = args.get(2).toInt();
                String continuation = args.getAll(4).toString();
                if (continuation.isBlank())
                    throw new IllegalArgumentException("Scheduled command cannot be empty");
                script.scheduleTask(name, ticks, args.match(0,"every") ? ticks : 0, continuation);
            }
            case "cancel" -> script.cancelTask(args.get(1).toString());
            case "cancel_all" -> script.cancelTasks();
            case "count" -> script.getScriptState().set(args.get(1).toString(), Integer.toString(script.activeTaskCount()));
            default -> throw new IllegalArgumentException("Usage: task after|every|cancel|cancel_all|count");
        }
    }
}
