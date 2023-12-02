package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.gui.screens.SettingScreen;
import net.minecraft.command.CommandSource;

public class KeybindsCommand extends Command {

    public KeybindsCommand() {
        super("keybinds", "Opens up the keybind screen.", "/keybinds", "keys");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            system.scheduler.runDelayedTask(() -> {
                mc.execute(() -> mc.setScreen(new SettingScreen()));
            }, 5 * 50);
            return SINGLE_SUCCESS;
        });
    }
}
