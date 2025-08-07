package io.github.itzispyder.clickcrystals.scripting.exceptions;

import io.github.itzispyder.clickcrystals.scripting.ClickScript;

public class ScriptNotFoundException extends RuntimeException {

    public ScriptNotFoundException(ClickScript script) {
        super("Script [%s] does not exist!".formatted(script.getPath()));
    }
}
