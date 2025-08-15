package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.components.CommandLine;
import io.github.itzispyder.clickcrystals.util.misc.Randomizer;

import java.util.List;

// @Format execute_random {}
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
        List<CommandLine> lines = ScriptParser.parse(all);

        if (!lines.isEmpty()) {
            Randomizer random = new Randomizer();
            random.getRandomElement(lines).executeDynamic(args.getExecutorOrDef());
        }
    }
}
