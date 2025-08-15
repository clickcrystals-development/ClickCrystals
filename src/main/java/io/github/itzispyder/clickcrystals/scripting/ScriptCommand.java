package io.github.itzispyder.clickcrystals.scripting;

import io.github.itzispyder.clickcrystals.Global;

public abstract class ScriptCommand implements Global {

    private final String name;
    private final String[] aliases;

    public ScriptCommand(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public abstract void onCommand(ScriptCommand command, String line, ScriptArgs args);

    public void dispatch(ClickScript script, String label, String cmd) {
        try {
            if (cmd != null && !cmd.isEmpty() && cmd.startsWith(label)) {
                String[] args = cmd
                        .replaceFirst(label, "")
                        .replaceAll("(\\s+[><=!]*)\\s*(-?\\d*(\\.\\d*)?)", "$1$2")
                        .replaceAll("\s{2,}", " ")
                        .trim().split(" ");
                onCommand(this, cmd, new ScriptArgs(script, args));
            }
        }
        catch (Exception ex) {
            script.printErrorDetails(ex, cmd);
        }
    }

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public static ScriptCommand create(String name, Execution execution, String... aliases) {
        return new ScriptCommand(name, aliases) {
            @Override
            public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
                execution.execute(command, line, args);
            }
        };
    }

    @FunctionalInterface
    public interface Execution {
        void execute(ScriptCommand command, String line, ScriptArgs args);
    }
}
