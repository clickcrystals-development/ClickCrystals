package io.github.itzispyder.clickcrystals.scripting.syntax.client;

import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;

// @Format (define|def) module ...
// @Format (define|def) (description|desc) "..."
// @Format (define|def) (function|func) ... {}
public class DefineCmd extends ScriptCommand {

    public DefineCmd() {
        super("define", "def");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        var read = args.getReader();

        switch (read.next(Type.class)) {
            case MODULE -> ClickScript.executeDynamic(args.getExecutorOrDef(), "module create " + read.next());
            case DESCRIPTION, DESC -> ClickScript.executeDynamic(args.getExecutorOrDef(), "description " + read.nextQuote());
            case FUNCTION, FUNC -> args.getExecutorOrDef().createFunction(read.nextStr(), read.remainingStr());
        }
    }

    public enum Type {
        FUNCTION,
        FUNC,
        MODULE,
        DESCRIPTION,
        DESC
    }
}
