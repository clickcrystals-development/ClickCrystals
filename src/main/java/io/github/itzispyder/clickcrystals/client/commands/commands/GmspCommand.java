package io.github.itzispyder.clickcrystals.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.commands.Command;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import net.minecraft.commands.SharedSuggestionProvider;

public class GmspCommand extends Command {

    public GmspCommand() {
        super("gmsp","Gamemode spectator",",gmsp");
    }

    @Override
    public void build(LiteralArgumentBuilder<SharedSuggestionProvider> builder) {
        builder.executes(context -> {
            ChatUtils.sendChatCommand("gamemode spectator");
            return SINGLE_SUCCESS;
        });
    }
}
