package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;

// @Format macro ... "..."
public class MacroCmd extends ScriptCommand implements Global {

    public MacroCmd() {
        super("macro");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        String name = args.get(0).toString();
        String rest = args.getAll(1).toString();

        if (rest.equals("\"\""))
            rest = "";
        else if (rest.length() > 2 && rest.startsWith("\"") && rest.endsWith("\""))
            rest = rest.substring(1, rest.length() - 1);

        ClickScript exe = args.getExecutor();
        exe.createMacro(name, rest);
    }

    public synchronized void exc(ScriptArgs args) {
        mc.execute(() -> args.executeAll(2));
    }
}
