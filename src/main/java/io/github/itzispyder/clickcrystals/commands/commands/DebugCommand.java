package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.system.Notification;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.commands.arguments.PlayerArgumentType;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import io.github.itzispyder.clickcrystals.util.ArrayUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

public class DebugCommand extends Command {

    public DebugCommand() {
        super("debug", "ClickCrystals Debug Info", ",debug <item>");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
                    ChatUtils.sendPrefixMessage(StringUtils.color("&cPlease provide an item!"));
                    return SINGLE_SUCCESS;
                })
                .then(literal("script-scripts")
                        .executes(context -> {
                            List<String> scriptCommands = Arrays.asList(ClickScript.collectNames());

                            ChatUtils.sendBlank(2);
                            ChatUtils.sendPrefixMessage("All ClickScript scripts:");
                            ChatUtils.sendBlank(1);
                            ChatUtils.sendMessage("Script scripts (" + scriptCommands.size() + "): " + ArrayUtils.list2string(scriptCommands));
                            ChatUtils.sendBlank(2);
                            return SINGLE_SUCCESS;
                        }))
                .then(literal("listeners")
                        .executes(context -> {
                            List<String> activeListeners = system.listeners().stream().map(l -> l.getClass().getSimpleName()).toList();
                            List<String> moduleListeners = system.listeners().stream().filter(l -> l instanceof Module).map(l -> l.getClass().getSimpleName()).toList();

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
                            ChatUtils.sendBlank(2);
                            ChatUtils.sendPrefixMessage("Scheduler Info");
                            ChatUtils.sendBlank(1);
                            ChatUtils.sendMessage("System Scheduler Active Tasks (" + system.scheduler.count() + ")");
                            ChatUtils.sendMessage("Tick Scheduler Active Tasks (" + system.tickScheduler.count() + ")");
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
                        }))
                .then(literal("players")
                        .then(argument("player", PlayerArgumentType.create())
                                .executes(context -> {
                                    PlayerListEntry entry = context.getArgument("player", PlayerListEntry.class);
                                    ClientPlayerEntity p = PlayerUtils.player();
                                    World world = p.getEntityWorld();

                                    for (PlayerEntity player : world.getPlayers()) {
                                        if (entry.getProfile().id() == player.getGameProfile().id()) {
                                            printPlayerStats(player, entry);
                                            return SINGLE_SUCCESS;
                                        }
                                    }

                                    error("Cannot find player.");
                                    return SINGLE_SUCCESS;
                                })))
                .then(literal("notifications").executes(context -> {
                    List<String> l = Notification.QUEUE.stream().map(Notification::getId).toList();

                    ChatUtils.sendBlank(2);
                    ChatUtils.sendPrefixMessage("Packets Info:");
                    ChatUtils.sendBlank(1);
                    ChatUtils.sendMessage("Queued Notifications (%s): %s".formatted(l.size(), ArrayUtils.list2string(l)));
                    ChatUtils.sendBlank(2);
                    return SINGLE_SUCCESS;
                }));
    }

    private void printPlayerStats(PlayerEntity player, PlayerListEntry entry) {
        String hp = "   &3Health: &c" + (int)player.getHealth() + "/" + (int)player.getMaxHealth() +" hp";
        int ab = (int)player.getAbsorptionAmount();

        infoRaw("");
        info("&b" + player.getGameProfile().name() + "&3 has the following statistics:");
        infoRaw(hp + (ab == 0 ? "" : "   &e+" + (int)player.getAbsorptionAmount() + " ab"));
        infoRaw("   &3Gamemode: &6" + StringUtils.capitalizeWords(entry.getGameMode().name()));
        infoRaw("   &3Latency: &7" + entry.getLatency() + " ms");
        infoRaw("   &3UUID: &7" + entry.getProfile().id());
        infoRaw("   &3Armor:");
        ArrayUtils.reverseForEach(EntityUtils.getArmorItems(player), this::printItem);
        infoRaw("   &3Hand:");
        ArrayUtils.reverseForEach(EntityUtils.getHandItems(player), this::printItem);
        infoRaw("");
    }

    private void printItem(ItemStack stack) {
        if (PlayerUtils.valid()) {
            Item item = stack.getItem();
            String key = item.getTranslationKey();
            String pre = "§8";
            if (key.contains("netherite"))      pre = "§4";
            else if (key.contains("diamond"))   pre = "§b";
            else if (key.contains("iron"))      pre = "§f";
            else if (key.contains("chainmail")) pre = "§7";
            else if (key.contains("gold"))      pre = "§e";

            String[] secs = item.getTranslationKey().split("\\.");
            Text text = Text.literal("      §7- " + pre + StringUtils.capitalizeWords(secs[secs.length - 1]));
            MutableText msg = text.copy();
            Style style = text.getStyle();
            HoverEvent.ShowItem content = new HoverEvent.ShowItem(stack);

            msg.fillStyle(style.withHoverEvent(content));
            PlayerUtils.player().sendMessage(msg, false);
        }
    }
}
