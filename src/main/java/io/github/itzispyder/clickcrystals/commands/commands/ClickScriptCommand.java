package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import io.github.itzispyder.clickcrystals.util.misc.Timer;
import net.minecraft.command.CommandSource;

public class ClickScriptCommand extends Command {

    public ClickScriptCommand() {
        super("clickscript", "Manages ClickScript compiler", "/clickscript <action>", "ccs");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("compile")
                .then(argument("commandline", StringArgumentType.greedyString())
                        .executes(cxt -> {
                            ClickScript.executeOneLine(cxt.getArgument("commandline", String.class));
                            return SINGLE_SUCCESS;
                        })))
                .then(literal("reload-scripts")
                        .executes(cxt -> {
                            Timer timer = Timer.start();
                            ChatUtils.sendPrefixMessage("-> Reloading all scripts!");
                            system.reloadScripts();
                            ChatUtils.sendPrefixMessage("-> Scripts reloaded! (%s)".formatted(timer.end().getStampPrecise()));
                            return SINGLE_SUCCESS;
                        }));
    }
}
