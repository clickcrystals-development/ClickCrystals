package io.github.itzispyder.clickcrystals.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.commands.Command;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;

public class GmaCommand extends Command {

    public GmaCommand() {
        super("gma","Gamemode adventure",",gma");
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        builder.executes(context -> {
            ChatUtils.sendChatCommand("gamemode adventure");
            return SINGLE_SUCCESS;
        });
    }
}
