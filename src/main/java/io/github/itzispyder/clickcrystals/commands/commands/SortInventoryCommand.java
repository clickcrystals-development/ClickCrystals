package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.util.InventoryUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

/**
 * /sort-inv command
 */
public class SortInventoryCommand extends Command {

    public SortInventoryCommand() {
        super("sort-inv","Sorts your inventory the ImproperIssue way!","/sort-inv");
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {
        builder.executes(context -> {
            InventoryUtils.sortInv();
            return SINGLE_SUCCESS;
        });
    }
}
