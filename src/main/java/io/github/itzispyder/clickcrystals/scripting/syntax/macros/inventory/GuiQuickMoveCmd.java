package io.github.itzispyder.clickcrystals.scripting.syntax.macros.inventory;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

import java.util.function.Predicate;

// @Format gui_quickmove <identifier> <int>?
public class GuiQuickMoveCmd extends ScriptCommand {

    public GuiQuickMoveCmd() {
        super("gui_quickmove");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        Predicate<ItemStack> item = ScriptParser.parseItemPredicate(args.get(0).toString());
        int slot = InvUtils.search(item);

        if (slot != -1) {
            if (args.getSize() == 1)
                InvUtils.quickMove(slot);
            if (args.getSize() > 1) {
                InvUtils.sendSlotPacket(slot, args.get(1).toInt() - 1, SlotActionType.SWAP);
                if (args.match(2, "then"))
                    args.executeAll(3);
            }
        }
    }
}
