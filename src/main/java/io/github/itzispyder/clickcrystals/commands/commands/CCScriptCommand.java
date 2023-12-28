package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptParser;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.data.Config;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.BrowsingScreen;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import io.github.itzispyder.clickcrystals.util.misc.Timer;
import net.minecraft.command.CommandSource;

import java.io.File;

public class CCScriptCommand extends Command {

    public CCScriptCommand() {
        super("clickscript", "Manages ClickScript compiler", "/clickscript <action>", "ccs");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(cxt -> {
                    system.scheduler.runDelayedTask(() -> {
                        BrowsingScreen.currentCategory = Categories.SCRIPTED;
                        mc.execute(() -> mc.setScreen(new BrowsingScreen()));
                    }, 5 * 50);
                    return SINGLE_SUCCESS;
                })
                .then(literal("compile")
                        .then(argument("commandline", StringArgumentType.greedyString())
                                .executes(cxt -> {
                                    ClickScript.executeDynamic(cxt.getArgument("commandline", String.class));
                                    return SINGLE_SUCCESS;
                                })))
                .then(literal("copy-file")
                        .then(argument("script-file-path", StringArgumentType.greedyString())
                                .executes(cxt -> {
                                    String path = Config.PATH_SCRIPTS + "/" + cxt.getArgument("script-file-path", String.class);
                                    File file = new File(path);
                                    String name = file.getName();
                                    boolean nameValid = name.endsWith(".ccs") || name.endsWith(".txt");

                                    if (!file.exists() || !nameValid) {
                                        error("Script '%s' not found!".formatted(name));
                                        return SINGLE_SUCCESS;
                                    }

                                    String script = ScriptParser.condenseLines(ScriptParser.readFile(file.getPath()));
                                    mc.keyboard.setClipboard(script);
                                    info("Copied script contents from '%s' to clipboard!".formatted(name));
                                    info("&7" + mc.keyboard.getClipboard());
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
