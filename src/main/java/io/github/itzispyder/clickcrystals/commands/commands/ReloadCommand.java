package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.client.system.Notification;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.data.announce.BulletinBoard;
import io.github.itzispyder.clickcrystals.data.pixelart.PixelArtGenerator;
import io.github.itzispyder.clickcrystals.events.listeners.TickEventListener;
import io.github.itzispyder.clickcrystals.modules.scripts.syntax.AsCmd;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import io.github.itzispyder.clickcrystals.util.misc.CameraRotator;
import io.github.itzispyder.clickcrystals.util.misc.Timer;
import net.minecraft.command.CommandSource;

import java.util.HashMap;
import java.util.Map;

import static io.github.itzispyder.clickcrystals.ClickCrystals.config;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("reload", "Reloads config, scripts, the entire ClickCrystals Client.", ",reload");
    }
    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(cxt -> {
            reload();
            return SINGLE_SUCCESS;
        });
    }

    public static synchronized void reload() {
        Timer timer = Timer.start();
        system.println("Reloading entire ClickCrystals client, might take a while!");

        // config save
        system.println("-> saving config [bulk action]");
        config.saveKeybinds();
        config.saveHuds();
        config.saveModules();
        config.save();

        // system properties
        System.setProperty("java.awt.headless", "false");

        // clickcrystals system
        system.println("-> stopping clickcrystals tasks");
        CameraRotator.cancelCurrentRotator();
        TickEventListener.cancelTickInputs();
        AsCmd.resetReferenceEntity();
        PixelArtGenerator.cancel();
        Notification.clearNotifications();
        system.scheduler.cancelAllTasks();

        // script reload
        Map<String, Boolean> scriptCache = new HashMap<>();
        system.scriptedModules().values().forEach(m -> scriptCache.put(m.getId(), m.isEnabled()));
        system.reloadScripts(false);
        system.scriptedModules().values().forEach(m -> m.setEnabled(scriptCache.getOrDefault(m.getId(), false), false));

        // config load
        config.loadEntireConfig();
        system.println("<- config loaded [bulk action completed]");

        // info request
        system.println("-> requesting data from https://itzispyder.github.io/");
        BulletinBoard.request().thenRun(ClickCrystals::checkUpdates);

        String result = "Reloaded ClickCrystals client! (%s)".formatted(timer.end().getStampPrecise());
        system.println(result);
        ChatUtils.sendPrefixMessage(result);
    }
}
