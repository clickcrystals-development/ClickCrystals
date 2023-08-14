package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.gui.screens.KeybindsScreen;
import io.github.itzispyder.clickcrystals.scheduler.Scheduler;
import net.minecraft.command.CommandSource;

public class KeybindsCommand extends Command {

    public KeybindsCommand() {
        super("keybinds", "Opens up the keybind screen.", "/keybinds", "keys");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            Scheduler.runTaskLater(() -> {
                mc.execute(() -> mc.setScreen(new KeybindsScreen()));
            }, 5);
            return SINGLE_SUCCESS;
        });
    }
}
