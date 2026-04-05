package io.github.itzispyder.clickcrystals.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.commands.Command;
import io.github.itzispyder.clickcrystals.client.system.Config;
import net.minecraft.commands.SharedSuggestionProvider;

public class FolderCommand extends Command {

    public FolderCommand() {
        super("folder", "Open up ClickCrystalsClient folder.", ",folder");
    }

    @Override
    public void build(LiteralArgumentBuilder<SharedSuggestionProvider> builder) {
        builder.executes(cxt -> {
            system.openFile(Config.PATH);
            info("Opened up '%s' in file explorer.".formatted(Config.PATH));
            return SINGLE_SUCCESS;
        });
    }
}
