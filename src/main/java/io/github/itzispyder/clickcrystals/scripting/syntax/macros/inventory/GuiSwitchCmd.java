package io.github.itzispyder.clickcrystals.scripting.syntax.macros.inventory;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.syntax.InputType;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

// @Format gui_switch <identifier>
public class GuiSwitchCmd extends ScriptCommand {

    public GuiSwitchCmd() {
        super("gui_switch");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (InputType.INVENTORY.isActive()) {
            Predicate<ItemStack> item = ScriptParser.parseItemPredicate(args.get(0).toString());
            InteractionUtils.searchGuiItem(item);
        }

        if (args.match(1, "then")) {
            args.executeAll(2);
        }
    }
}
