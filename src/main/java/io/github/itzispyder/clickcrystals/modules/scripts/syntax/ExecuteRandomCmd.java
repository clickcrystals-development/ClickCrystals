package io.github.itzispyder.clickcrystals.modules.scripts.syntax;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptParser;
import io.github.itzispyder.clickcrystals.client.clickscript.components.CommandLine;
import io.github.itzispyder.clickcrystals.util.misc.Randomizer;

import java.util.List;

public class ExecuteRandomCmd extends ScriptCommand {

    public ExecuteRandomCmd() {
        super("execute_random");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        String all = args.getAll().toString();

        if (all.length() < 2) {
            return;
        }

        all = all.substring(1, all.length() - 1);
        List<CommandLine> lines = ScriptParser.getStackLines(all);

        if (!lines.isEmpty()) {
            Randomizer random = new Randomizer();
            random.getRandomElement(lines).executeDynamic(args.getExecutorOrDef());
        }
    }
}
