package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.data.Config;
import net.minecraft.command.CommandSource;

public class FolderCommand extends Command {

    public FolderCommand() {
        super("folder", "Open up ClickCrystalsClient folder.", ",folder");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(cxt -> {
            system.openFile(Config.PATH);
            info("Opened up '%s' in file explorer.".formatted(Config.PATH));
            return SINGLE_SUCCESS;
        });
    }
}
