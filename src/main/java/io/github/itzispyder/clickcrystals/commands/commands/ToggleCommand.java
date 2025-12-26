package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.commands.arguments.ModuleArgumentType;
import io.github.itzispyder.clickcrystals.events.listeners.UserInputListener;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.SilkTouch;
import net.minecraft.command.CommandSource;

public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("toggle","§7Toggles the modules from this mod. THIS CAN ALSO BE DONE VIA GUI MENU, PRESS YOUR §l§oAPOSTROPHE §7KEY!",",toggle <module> [on|off|help]","t");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
                    system.scheduler.runDelayedTask(UserInputListener::openModulesScreen, 5 * 50);
                    return SINGLE_SUCCESS;
                })
                .then(literal("#ALL")
                        .then(literal("on")
                                .executes(context -> {
                                    for (Module m: system.collectModules())
                                        if (m.getClass() != SilkTouch.class)
                                            m.setEnabled(true, false);
                                    info("&bToggled all modules on");
                                    return SINGLE_SUCCESS;
                                }))
                        .then(literal("off")
                                .executes(context -> {
                                    for (Module m: system.collectModules())
                                        m.setEnabled(false, false);
                                    info("&7Toggled all modules off");
                                    return SINGLE_SUCCESS;
                                }))
                        .executes(context -> {
                            for (Module m: system.collectModules())
                                if (m.getClass() != SilkTouch.class)
                                    m.setEnabled(!m.isEnabled(), false);
                            info("&bToggled all modules (on -> off, off -> on)");
                            return SINGLE_SUCCESS;
                        }))
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
