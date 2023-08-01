package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import net.minecraft.command.CommandSource;

public class GmspCommand extends Command {

    public GmspCommand() {
        super("gmsp","Gamemode spectator",",gmsp");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            ChatUtils.sendChatCommand("gamemode spectator");
            return SINGLE_SUCCESS;
        });
    }
}
