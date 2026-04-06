package io.github.itzispyder.clickcrystals.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.commands.Command;
import io.github.itzispyder.clickcrystals.client.commands.arguments.CommandArgumentType;
import io.github.itzispyder.clickcrystals.client.commands.arguments.ModuleArgumentType;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help","ClickCrystals Info and help",",help <item>");
    }

    @Override
    public void build(LiteralArgumentBuilder<SharedSuggestionProvider> builder) {
        builder.executes(context -> {
                    ChatUtils.sendPrefixMessage("§bCommands (§f" + system.commands().size() + "§b):");
                    
                    MutableComponent commands = Component.literal("");
                    system.commands().values().forEach(command -> commands.append(getCommandText(command)));
                    ChatUtils.sendRawText(commands);
                    
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
    
    private MutableComponent getCommandText(Command command) {
        MutableComponent tooltip = Component.literal("");
        tooltip.append(Component.literal(command.getName()).withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD)).append("\n");
        tooltip.append(Component.literal("," + command.getName()).withStyle(ChatFormatting.GRAY)).append("\n\n");
        tooltip.append(Component.literal(command.getDescription()).withStyle(ChatFormatting.WHITE));
        
        MutableComponent text = Component.literal(command.getName());
        var commandsList = system.commands().values().stream().toList();
        if (!commandsList.getLast().equals(command))
            text.append(Component.literal(", ").withStyle(ChatFormatting.GRAY));
        text.setStyle(text.getStyle()
            .withHoverEvent(new HoverEvent.ShowText(tooltip))
            .withClickEvent(new ClickEvent.SuggestCommand("," + command.getName()))
        );
        
        return text;
    }
}
