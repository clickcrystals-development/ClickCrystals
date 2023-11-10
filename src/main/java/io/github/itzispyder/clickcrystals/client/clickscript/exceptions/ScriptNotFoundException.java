package io.github.itzispyder.clickcrystals.client.clickscript.exceptions;

import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;

public class ScriptNotFoundException extends RuntimeException {

    public ScriptNotFoundException(ClickScript script) {
        super("Script [%s] does not exist!".formatted(script.getPath()));
    }
}
