package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.commands.arguments.ModuleArgumentType;
import io.github.itzispyder.clickcrystals.events.listeners.UserInputListener;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.SilkTouch;
import net.minecraft.command.CommandSource;

public class ToggleCommand extends Command {

    public static boolean used = false;

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
                                    used = true;
                                    info("&bToggled all modules on");
                                    used = false;
                                    return SINGLE_SUCCESS;
                                }))
                        .then(literal("off")
                                .executes(context -> {
                                    for (Module m: system.collectModules())
                                        m.setEnabled(false, false);
                                    used = true;
                                    info("&7Toggled all modules off");
                                    used = false;
                                    return SINGLE_SUCCESS;
                                }))
                        .executes(context -> {
                            for (Module m: system.collectModules())
                                if (m.getClass() != SilkTouch.class)
                                    m.setEnabled(!m.isEnabled(), false);
                            used = true;
                            info("&bToggled all modules (on -> off, off -> on)");
                            used = false;
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
