package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;

// @Format wait <num> {}
public class WaitCmd extends ScriptCommand implements Global, ThenChainable {

    public WaitCmd() {
        super("wait");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (args.getSize() <= 1)
            throw new IllegalArgumentException("Usage: wait <seconds> [then] <command>");
        double seconds = args.get(0).toDouble();
        if (!Double.isFinite(seconds) || seconds < 0 || seconds > 60)
            throw new IllegalArgumentException("wait must be 0..60 seconds");
        long delay = Math.round(seconds * 1000.0);
        // Freeze the continuation now: ScriptArgs can be reused by nested commands.
        int commandOffset = args.match(1, "then") ? 2 : 1;
        String continuation = args.getAll(commandOffset).toString();
        if (continuation.isBlank())
            throw new IllegalArgumentException("wait requires a continuation command");
        ClickScript executor = args.getExecutorOrDef();
        // Scheduled tasks may run on a worker thread. Minecraft input, inventory
        // and rendering APIs are accessible ONLY on the game/client thread.
        system.scheduler.runDelayedTask(() -> mc.execute(() ->
                ClickScript.executeDynamic(executor, continuation)), delay);
    }
}
