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
        switch (args.get(0).toEnum(Type.class)) {
            case MODULE -> ClickScript.executeDynamic(args.getExecutorOrDef(), "module create " + args.get(1));
            case DESCRIPTION, DESC -> ClickScript.executeDynamic(args.getExecutorOrDef(), "description " + args.getAll(1));
            case FUNCTION, FUNC -> args.getExecutorOrDef().createFunction(args.get(1).toString(), args.getAll(2).toString());
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
