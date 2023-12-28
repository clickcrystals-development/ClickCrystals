package io.github.itzispyder.clickcrystals.modules.scripts.macros.inventory;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.modules.scripts.macros.InputCmd;
import io.github.itzispyder.clickcrystals.modules.scripts.syntax.OnEventCmd;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class GuiDropCmd extends ScriptCommand {

    public GuiDropCmd() {
        super("gui_drop");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        Predicate<ItemStack> item = OnEventCmd.parseItemPredicate(args.get(0).toString());
        int slot = InvUtils.search(item);

        if (InputCmd.Action.INVENTORY.isActive() && slot != -1) {
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
