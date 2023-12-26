package io.github.itzispyder.clickcrystals.client.clickscript.components;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandStack {

    private final List<CommandLine> lines;

    public CommandStack(List<CommandLine> lines) {
        this.lines = lines;
    }

    public CommandStack(CommandLine... lines) {
        this(Arrays.asList(lines));
    }

    public void executeAll() {
        getLines().forEach(CommandLine::execute);
    }

    public List<CommandLine> getLines() {
        List<CommandLine> lines = new ArrayList<>();

        for (CommandLine line : this.lines) {
            String s = line.line();

            if (s.contains("{") || s.contains("}")) {
                lines.addAll(ScriptParser.getStack(s).getLines());
            }
            else {
                for (String cmd : line.line().split(";")) {
                    lines.add(new CommandLine(cmd));
                }
            }
        }

        return lines;
    }
}
