package io.github.itzispyder.clickcrystals.client.clickscript.components;

import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;

public record CommandLine(String line) {

    public CommandLine(String line) {
        this.line = line.trim();
    }

    public void execute() {
        ClickScript.executeSingle(line);
    }
}
