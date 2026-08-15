package io.github.itzispyder.clickcrystals.scripting.exceptions;

import io.github.itzispyder.clickcrystals.scripting.ClickScript;

public class ScriptException extends RuntimeException {

    private ClickScript executor;
    private String line;

    public ScriptException(String message, ClickScript executor, String line) {
        super(message);
        this.executor = executor;
        this.line = line;
    }

    public ScriptException(Throwable cause,  ClickScript executor, String line) {
        super(cause);
        this.executor = executor;
        this.line = line;
    }

    public ScriptException(String message) {
        this(message, ClickScript.DEFAULT_DISPATCHER, "null");
    }

    public ScriptException(Throwable cause) {
        this(cause, ClickScript.DEFAULT_DISPATCHER, "null");
    }

    public ClickScript getExecutor() {
        return executor;
    }

    public void setExecutor(ClickScript executor) {
        this.executor = executor;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
