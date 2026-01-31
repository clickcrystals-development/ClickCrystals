package io.github.itzispyder.clickcrystals.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.commands.Command;
import io.github.itzispyder.clickcrystals.client.commands.arguments.CommandArgumentType;
import io.github.itzispyder.clickcrystals.client.commands.arguments.ModuleArgumentType;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help","ClickCrystals Info and help",",help <item>");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
                    ChatUtils.sendPrefixMessage("§bCommands (§f" + system.commands().size() + "§b):");
                    
                    MutableText commands = Text.literal("");
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
    
    private MutableText getCommandText(Command command) {
        // Hover tooltip
        MutableText tooltip = Text.literal("");
        tooltip.append(Text.literal(command.getName()).formatted(Formatting.AQUA, Formatting.BOLD)).append("\n");
        tooltip.append(Text.literal("," + command.getName()).formatted(Formatting.GRAY)).append("\n\n");
        tooltip.append(Text.literal(command.getDescription()).formatted(Formatting.WHITE));
        
        // Clickable text
        MutableText text = Text.literal(command.getName());
        var commandsList = system.commands().values().stream().toList();
        if (!commandsList.getLast().equals(command))
            text.append(Text.literal(", ").formatted(Formatting.GRAY));
        text.setStyle(text.getStyle()
            .withHoverEvent(new HoverEvent.ShowText(tooltip))
            .withClickEvent(new ClickEvent.SuggestCommand("," + command.getName()))
        );
        
        return text;
    }
}
