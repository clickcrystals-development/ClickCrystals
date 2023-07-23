package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.PacketMapper;
import io.github.itzispyder.clickcrystals.commands.CustomCommand;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.scheduler.DelayedTask;
import io.github.itzispyder.clickcrystals.scheduler.RepeatingTask;
import io.github.itzispyder.clickcrystals.scheduler.Scheduler;
import io.github.itzispyder.clickcrystals.util.ArrayUtils;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import net.minecraft.command.CommandSource;

import java.util.List;

public class CCDebugCommand extends CustomCommand {

    public CCDebugCommand() {
        super("ccdebug", "ClickCrystals Debug Info", "/ccdebug <item>", "clickcrystaldebug");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
                    ChatUtils.sendPrefixMessage(StringUtils.color("&cPlease provide an item!"));
                    return SINGLE_SUCCESS;
                })
                .then(literal("listeners")
                        .executes(context -> {
                            List<String> activeListeners = system.listeners().values().stream().map(l -> l.getClass().getSimpleName()).toList();
                            List<String> moduleListeners = system.listeners().values().stream().filter(l -> l instanceof Module).map(l -> l.getClass().getSimpleName()).toList();

                            ChatUtils.sendBlank(2);
                            ChatUtils.sendPrefixMessage("Listener Info:");
                            ChatUtils.sendBlank(1);
                            ChatUtils.sendMessage("Active Listeners (" + activeListeners.size() + "): " + ArrayUtils.list2string(activeListeners));
                            ChatUtils.sendBlank(1);
                            ChatUtils.sendMessage("Module Listeners (" + moduleListeners.size() + "): " + ArrayUtils.list2string(moduleListeners));
                            ChatUtils.sendBlank(2);
                            return SINGLE_SUCCESS;
                        }))
                .then(literal("schedulers")
                        .executes(context -> {
                            List<String> activeTasks = Scheduler.getTasks().stream().map(t -> t.getClass().getSimpleName()).toList();
                            List<String> delayedTasks = Scheduler.getTasks().stream().filter(t -> t instanceof DelayedTask).map(t -> t.getClass().getSimpleName()).toList();
                            List<String> repeatingTasks = Scheduler.getTasks().stream().filter(t -> t instanceof RepeatingTask).map(t -> t.getClass().getSimpleName()).toList();

                            ChatUtils.sendBlank(2);
                            ChatUtils.sendPrefixMessage("Scheduler Info");
                            ChatUtils.sendBlank(1);
                            ChatUtils.sendMessage("Active Tasks (" + activeTasks.size() + "): " + ArrayUtils.list2string(activeTasks));
                            ChatUtils.sendBlank(1);
                            ChatUtils.sendMessage("Delayed Tasks (" + delayedTasks.size() + "): " + ArrayUtils.list2string(delayedTasks));
                            ChatUtils.sendBlank(1);
                            ChatUtils.sendMessage("Repeating Tasks (" + repeatingTasks.size() + "): " + ArrayUtils.list2string(repeatingTasks));
                            ChatUtils.sendBlank(2);
                            return SINGLE_SUCCESS;
                        }))
                .then(literal("packets")
                        .executes(context -> {
                            List<String> c2s = PacketMapper.getC2SNames();
                            List<String> s2c = PacketMapper.getS2CNames();

                            ChatUtils.sendBlank(2);
                            ChatUtils.sendPrefixMessage("Packets Info:");
                            ChatUtils.sendBlank(1);
                            ChatUtils.sendMessage("Client to Server (" + c2s.size() + "): " + ArrayUtils.list2string(c2s));
                            ChatUtils.sendBlank(1);
                            ChatUtils.sendMessage("Server to Client (" + s2c.size() + "): " + ArrayUtils.list2string(s2c));
                            ChatUtils.sendBlank(2);
                            return SINGLE_SUCCESS;
                        }))
                .then(literal("keybinds")
                        .executes(context -> {
                            List<String> binds = system.keybinds().stream().map(bind -> bind.getId() + ": " + bind.getKey()).toList();

                            ChatUtils.sendBlank(2);
                            ChatUtils.sendPrefixMessage("Keybinds Info:");
                            ChatUtils.sendBlank(1);
                            ChatUtils.sendMessage("Keybindings (" + binds.size() + "): " + ArrayUtils.list2string(binds));
                            ChatUtils.sendBlank(2);
                            return SINGLE_SUCCESS;
                        }));
    }
}
