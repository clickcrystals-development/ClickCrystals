package io.github.itzispyder.clickcrystals.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.commands.Command;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;

public class GmcCommand extends Command {

    public GmcCommand() {
        super("gmc","Gamemode creative",",gmc");
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        builder.executes(context -> {
            ChatUtils.sendChatCommand("gamemode creative");
            return SINGLE_SUCCESS;
        });
    }
}
