package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class ClickCrystalToggleCommand extends Command implements Listener {

    public ClickCrystalToggleCommand() {
        super("clickcrystaltoggle","Allows you to crystal easier, by using left click to both place and break crystals.","/cctoggle [on|off|help]","cctoggle");
        system.addListener(this);
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {

        for (Module module : system.modules().values()) {
            builder.then(literal(module.getId()).executes(context -> {
                module.setEnabled(!module.isEnabled());
                return SINGLE_SUCCESS;
            }));

            builder.then(literal(module.getId()).then(literal("help").executes(context -> {
                ChatUtils.sendMessage(module.getHelp());
                return SINGLE_SUCCESS;
            })));

            builder.then(literal(module.getId()).then(literal("on").executes(context -> {
                module.setEnabled(true);
                return SINGLE_SUCCESS;
            })));

            builder.then(literal(module.getId()).then(literal("off").executes(context -> {
                module.setEnabled(false);
                return SINGLE_SUCCESS;
            })));
        }
    }
}
