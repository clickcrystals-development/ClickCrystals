package io.github.itzispyder.clickcrystals.client.clickscript.components;

import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptParser;

public record CommandLine(String line) {

    public CommandLine(String line) {
        this.line = line.trim();
    }

    public boolean isDeep() {
        return ScriptParser.getStackLines(line).size() > 1;
    }

    public void execute() {
        execute(ClickScript.DEFAULT_DISPATCHER);
    }

    public void execute(ClickScript executor) {
        ClickScript.executeSingle(executor, line);
    }

    public void executeDynamic() {
        ClickScript.executeDynamic(line);
    }
}
