package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.commands.arguments.ModuleArgumentType;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.BrowsingScreen;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.minecraft.command.CommandSource;

public class CCToggleCommand extends Command {

    public CCToggleCommand() {
        super("toggle","§7Toggles the modules from this mod. THIS CAN ALSO BE DONE VIA GUI MENU, PRESS YOUR §l§oAPOSTROPHE §7KEY!",",toggle <module> [on|off|help]","t");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
                    system.scheduler.runDelayedTask(() -> {
                        mc.execute(() -> mc.setScreen(new BrowsingScreen()));
                    }, 5 * 50);
                    return SINGLE_SUCCESS;
                })
                .then(argument("module", ModuleArgumentType.create())
                        .executes(context -> {
                            Module module = context.getArgument("module", Module.class);
                            module.setEnabled(!module.isEnabled(), true);
                            return SINGLE_SUCCESS;
                        })
                        .then(literal("help")
                                .executes(context -> {
                                    Module module = context.getArgument("module", Module.class);
                                    info(module.getHelp());
                                    return SINGLE_SUCCESS;
                                }))
                        .then(literal("on")
                                .executes(context -> {
                                    Module module = context.getArgument("module", Module.class);
                                    module.setEnabled(true, true);
                                    return SINGLE_SUCCESS;
                                }))
                        .then(literal("off")
                                .executes(context -> {
                                    Module module = context.getArgument("module", Module.class);
                                    module.setEnabled(false, true);
                                    return SINGLE_SUCCESS;
                                })));
    }
}
