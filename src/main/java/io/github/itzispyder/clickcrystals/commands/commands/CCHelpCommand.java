package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

/**
 * /cchelp command
 */
public class CCHelpCommand extends Command {

    public CCHelpCommand() {
        super("clickcrystalhelp","Modules info and help","/cchelp <module>", "cchelp");
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {
        for (Module module : system.modules().values()) {
            builder.executes(context -> {
                        ChatUtils.sendPrefixMessage("§cPlease include a module name!");
                        return SINGLE_SUCCESS;
                    })
                    .then(literal(module.getId())
                            .executes(context -> {
                                ChatUtils.sendMessage(module.getHelp());
                                return SINGLE_SUCCESS;
                            }));
        }
    }
}
