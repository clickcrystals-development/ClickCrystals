package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

/**
 * /cctoggle command
 */
public class CCToggleCommand extends Command {

    /**
     * Init command
     */
    public CCToggleCommand() {
        super("clickcrystaltoggle","§7Toggles the modules from this mod. THIS CAN ALSO BE DONE VIA GUI MENU, PRESS YOUR §l§oAPOSTROPHE §7KEY!","/cctoggle [on|off|help]","cctoggle");
    }

    /**
     * Command builder
     * @param builder builder
     */
    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {

        for (Module module : system.modules().values()) {
            builder.executes(context -> {
                        ChatUtils.sendMessage(super.getHelp());
                        return SINGLE_SUCCESS;
                    })
                    .then(literal(module.getId())
                            .executes(context -> {
                                module.setEnabled(!module.isEnabled());
                                return SINGLE_SUCCESS;
                            })
                            .then(literal("help")
                                    .executes(context -> {
                                        ChatUtils.sendMessage(module.getHelp());
                                        return SINGLE_SUCCESS;
                                    }))
                            .then(literal("on")
                                    .executes(context -> {
                                        module.setEnabled(true);
                                        return SINGLE_SUCCESS;
                                    }))
                            .then(literal("off")
                                    .executes(context -> {
                                        module.setEnabled(false);
                                        return SINGLE_SUCCESS;
                                    })));
        }
    }
}
