package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class GuiSwitchCmd extends ScriptCommand {

    public GuiSwitchCmd() {
        super("gui_switch");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        Predicate<ItemStack> item = OnEventCmd.parseItemPredicate(args.get(0).toString());
        InteractionUtils.searchGuiItem(item);

        if (args.match(1, "then")) {
            args.executeAll(2);
        }
    }
}
