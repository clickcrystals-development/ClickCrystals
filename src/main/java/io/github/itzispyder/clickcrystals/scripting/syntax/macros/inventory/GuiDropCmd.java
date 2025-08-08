package io.github.itzispyder.clickcrystals.scripting.syntax.macros.inventory;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

// @Format gui_drop <identifier> all
// @Format gui_drop <identifier> <int>
public class GuiDropCmd extends ScriptCommand {

    public GuiDropCmd() {
        super("gui_drop");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        Predicate<ItemStack> item = ScriptParser.parseItemPredicate(args.get(0).toString());
        int slot = InvUtils.search(item);

        if (slot != -1) {
            if (args.match(1, "all")) {
                InvUtils.dropSlot(slot, true);
            }
            else {
                int times = MathUtils.clamp(args.get(1).toInt(), 0, 64);
                for (int i = 0; i < times; i++) {
                    InvUtils.dropSlot(slot, false);
                }
            }
        }

        if (args.match(2, "then")) {
            args.executeAll(3);
        }
    }
}
