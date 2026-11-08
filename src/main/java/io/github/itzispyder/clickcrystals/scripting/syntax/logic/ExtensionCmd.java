package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.SenseValues;
import io.github.itzispyder.clickcrystals.scripting.runtime.ScriptExtensions;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * extension available <namespace:id> <state-variable>
 * extension read <namespace:sensor> <state-variable>
 * extension call <namespace:action> [arguments...]
 * Only actions installed by trusted Java modules can be called.
 */
public final class ExtensionCmd extends ScriptCommand {
    public ExtensionCmd() { super("extension", "ext"); }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        ClickScript script = args.getExecutorOrDef();
        String operation = args.get(0).toString().toLowerCase(Locale.ROOT);
        switch (operation) {
            case "available" -> {
                if (args.getSize() != 3)
                    throw new IllegalArgumentException("Usage: ext available <id> <variable>");
                script.getScriptState().set(args.get(2).toString(),
                        Boolean.toString(ScriptExtensions.has(args.get(1).toString())));
            }
            case "read" -> {
                if (args.getSize() != 3)
                    throw new IllegalArgumentException("Usage: ext read <id> <variable>");
                String id=args.get(1).toString();
                String variable=args.get(2).toString();
                mc.execute(() -> {
                    try {
                        var value=ScriptExtensions.read(id, context(script));
                        if (value == null) script.getScriptState().clear(variable);
                        else script.getScriptState().set(variable, value);
                    } catch (Exception ex) {
                        script.printErrorDetails(ex, "ext read " + id);
                    }
                });
            }
            case "call" -> {
                if (args.getSize() < 2)
                    throw new IllegalArgumentException("Usage: ext call <id> [args...]");
                String id=args.get(1).toString();
                List<String> list=new ArrayList<>();
                for (int i=2;i<args.getSize();i++) list.add(args.get(i).toString());
                mc.execute(() -> {
                    try {
                        ScriptExtensions.call(id,context(script),list);
                    } catch (Exception ex) {
                        script.printErrorDetails(ex, "ext call " + id);
                    }
                });
            }
            default -> throw new IllegalArgumentException("Usage: ext available|read|call");
        }
    }

    private static ScriptExtensions.Context context(ClickScript script) {
        return new ScriptExtensions.Context() {
            public String readGame(String metric) { return SenseValues.read(metric); }
            public String readState(String key) { return script.getScriptState().get(key); }
            public void writeState(String key, String value) { script.getScriptState().set(key,value); }
        };
    }
}
