package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.CustomCommand;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import net.minecraft.command.CommandSource;

public class GmsCommand extends CustomCommand {

    public GmsCommand() {
        super("gms","Gamemode survival","/gms");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            ChatUtils.sendChatCommand("gamemode survival");
            return SINGLE_SUCCESS;
        });
    }
}
