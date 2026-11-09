package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import java.util.Locale;

/** Mutable state for custom CCS modules; all values are local to the script. */
public final class StateCmd extends ScriptCommand {
    public StateCmd() { super("state"); }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        var state = args.getExecutorOrDef().getScriptState();
        String operation = args.get(0).toString().toLowerCase(Locale.ROOT);
        switch (operation) {
            case "set" -> state.set(args.get(1).toString(), args.getAll(2).toString());
            case "append" -> state.append(args.get(1).toString(), args.getAll(2).toString());
            case "copy" -> state.copy(args.get(1).toString(), args.get(2).toString());
            case "length" -> state.length(args.get(1).toString(), args.get(2).toString());
            case "toggle" -> state.toggle(args.get(1).toString());
            case "inc", "dec" -> state.math(args.get(1).toString(), operation, 1.0);
            case "add", "sub", "mul", "div", "mod", "min", "max" ->
                    state.math(args.get(1).toString(), operation, args.get(2).toDouble());
            case "clamp" -> state.clamp(args.get(1).toString(),
                    args.get(2).toDouble(), args.get(3).toDouble());
            case "clear" -> state.clear(args.get(1).toString());
            case "reset" -> state.reset();
            case "show" -> ChatUtils.sendPrefixMessage(
                    "State [" + args.get(1) + "] = " + state.get(args.get(1).toString()));
            default -> throw new IllegalArgumentException("Unknown state command: " + operation);
        }
    }
}
