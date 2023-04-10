package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class GmcCommand extends Command {

    public GmcCommand() {
        super("gmc","Gamemode creative","/gmc");
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {
        builder.executes(context -> {
            ChatUtils.sendChatCommand("gamemode creative");
            return SINGLE_SUCCESS;
        });
    }
}
