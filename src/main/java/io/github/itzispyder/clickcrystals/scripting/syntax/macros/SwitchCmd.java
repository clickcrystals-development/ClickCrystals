package io.github.itzispyder.clickcrystals.scripting.syntax.macros;

import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.runtime.SelectionHistory;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;

// @Format switch back
// @Format switch <int>
// @Format switch <identifier>
public class SwitchCmd extends ScriptCommand {

    /**
     * Legacy field kept for binary compatibility only.
     * "switch back" now uses each script's isolated SelectionHistory.
     */
    @Deprecated
    public static int lastSlot = -1;

    public SwitchCmd() {
        super("switch");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (args.getSize() < 1)
            throw new IllegalArgumentException("Usage: switch back|<slot>|<item-predicate> [then command]");
        if (PlayerUtils.invalid()) return;

        ClickScript script = args.getExecutorOrDef();
        SelectionHistory history = script.getSelectionHistory();

        if (args.match(0, "back")) {
            int original = history.consumeReturn(InvUtils.selected(), PlayerUtils.getWorld());
            if (original >= 0) {
                InvUtils.select(original);
                if (args.match(1, "then")) args.executeAll(2);
            }
            return;
        }

        ScriptArgs.Arg requested = args.get(0);
        int target;
        if (requested.toString().matches("\\d+")) {
            int oneBasedSlot = requested.toInt();
            if (oneBasedSlot < 1 || oneBasedSlot > 9)
                throw new IllegalArgumentException(requested + " is not a valid hotbar slot");
            target = oneBasedSlot - 1;
        } else {
            target = HotbarUtils.searchSlot(ScriptParser.parseItemPredicate(requested.toString()));
        }

        // Never modify the return slot if the requested item is unavailable.
        if (target < 0) return;
        int previous = InvUtils.selected();
        if (previous != target) {
            history.remember(previous, target, PlayerUtils.getWorld());
            lastSlot = previous;
            InvUtils.select(target);
        }

        if (args.match(1, "then")) args.executeAll(2);
    }
}
