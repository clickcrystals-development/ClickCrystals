package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.commands.arguments.ModuleArgumentType;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.minecraft.command.CommandSource;

public class CCToggleCommand extends Command {

    public CCToggleCommand() {
        super("toggle","§7Toggles the modules from this mod. THIS CAN ALSO BE DONE VIA GUI MENU, PRESS YOUR §l§oLEFT_SHIFT §7KEY!","/toggle <module> [on|off|help]","t");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("module", ModuleArgumentType.create())
                .executes(context -> {
                    Module module = context.getArgument("module", Module.class);
                    module.setEnabled(!module.isEnabled(), true);
                    return SINGLE_SUCCESS;
                })
                .then(literal("help")
                        .executes(context -> {
                            Module module = context.getArgument("module", Module.class);
                            print(module.getHelp());
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
