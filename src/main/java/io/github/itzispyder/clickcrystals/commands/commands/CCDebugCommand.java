package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.PacketInfo;
import io.github.itzispyder.clickcrystals.client.PacketMapper;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.scheduler.DelayedTask;
import io.github.itzispyder.clickcrystals.scheduler.RepeatingTask;
import io.github.itzispyder.clickcrystals.scheduler.Scheduler;
import io.github.itzispyder.clickcrystals.util.ArrayUtils;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.List;
import java.util.Map;

import static io.github.itzispyder.clickcrystals.ClickCrystals.packetLogger;

public class CCDebugCommand extends Command {

    public CCDebugCommand() {
        super("ccdebug", "ClickCrystals Debug Info", "/ccdebug <item>", "clickcrystaldebug");
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {
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
                        }).then(literal("list")
                                .executes(context -> {
                                    ChatUtils.sendBlank(2);
                                    ChatUtils.sendPrefixMessage("Packets Info:");
                                    ChatUtils.sendMessage(StringUtils.color("&a&l•&7Received    &b&l•&7Sent    &c&l•&7Unmapped"));
                                    ChatUtils.sendMessage(StringUtils.color("&7Logged (" + packetLogger.getLog().size() + ") packets"));
                                    ChatUtils.sendBlank(1);
                                    for (Map.Entry<String, PacketInfo> entry : packetLogger.getLog().entrySet()) {
                                        ChatUtils.sendMessage(entry.getKey() + "§7: " + entry.getValue().getCount());
                                    }
                                    ChatUtils.sendBlank(2);
                                    return SINGLE_SUCCESS;
                                })));
    }
}
