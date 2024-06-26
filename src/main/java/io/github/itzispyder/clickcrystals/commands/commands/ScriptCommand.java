package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptParser;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.commands.arguments.ScriptFileArgumentType;
import io.github.itzispyder.clickcrystals.data.Config;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.BrowsingScreen;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.scripts.syntax.AsCmd;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;

import java.io.File;

public class ScriptCommand extends Command {

    public ScriptCommand() {
        super("clickscript", "Manages ClickScript compiler", ",clickscript <action>", "ccs");
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
                                    String cmd = cxt.getArgument("commandline", String.class);
                                    try {
                                        ClickScript.executeDynamic(cmd);
                                    }
                                    catch (Exception ex) {
                                        ClickScript.DEFAULT_DISPATCHER.printErrorDetails(ex, cmd);
                                    }
                                    return SINGLE_SUCCESS;
                                })))
                .then(literal("copy-file")
                        .then(argument("script-file-path", ScriptFileArgumentType.create())
                                .executes(cxt -> {
                                    String path = Config.PATH_SCRIPTS + cxt.getArgument("script-file-path", String.class);
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
                            ReloadCommand.reload();
                            return SINGLE_SUCCESS;
                        }))
                .then(literal("current-reference-entity")
                        .executes(cxt -> {
                            Entity ent = AsCmd.getCurrentReferenceEntity();
                            String name = ent == null ? "null" : ent.getName().getString();
                            ChatUtils.sendPrefixMessage("Script reference entity is: " + name);
                            return SINGLE_SUCCESS;
                        }));
    }
}
