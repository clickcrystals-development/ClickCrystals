package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.CustomCommand;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import net.minecraft.command.CommandSource;

public class TestCommand extends CustomCommand {

    public TestCommand() {
        super("testcommand", "Testing, serves no purpose.", "/testcommand", "tc", "tcmd");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("testarg")
                .executes(context -> {
                    ChatUtils.sendPrefixMessage("testarg");
                    return SINGLE_SUCCESS;
                })
                .then(literal("argtwo")
                        .executes(context -> {
                            ChatUtils.sendPrefixMessage("argtwo");
                            return SINGLE_SUCCESS;
                        })));
    }
}
