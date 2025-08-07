package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.commands.arguments.CommandArgumentType;
import io.github.itzispyder.clickcrystals.commands.arguments.ModuleArgumentType;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import net.minecraft.command.CommandSource;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help","ClickCrystals Info and help",",help <item>");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
                    ChatUtils.sendPrefixMessage("§cPlease include an item!");
                    return SINGLE_SUCCESS;
                })
                .then(literal("modules")
                        .then(argument("module", ModuleArgumentType.create())
                                .executes(context -> {
                                    Module module = context.getArgument("module", Module.class);
                                    infoRaw(module.getHelp());
                                    return SINGLE_SUCCESS;
                                })))
                .then(literal("scripts")
                        .then(argument("command", CommandArgumentType.create())
                                .executes(context -> {
                                    Command command = context.getArgument("command", Command.class);
                                    infoRaw(command.getHelp());
                                    return SINGLE_SUCCESS;
                                })));
    }
}
