package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.client.system.Notification;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.data.announce.BulletinBoard;
import io.github.itzispyder.clickcrystals.data.pixelart.PixelArtGenerator;
import io.github.itzispyder.clickcrystals.events.listeners.TickEventListener;
import io.github.itzispyder.clickcrystals.modules.modules.ScriptedModule;
import io.github.itzispyder.clickcrystals.scripting.syntax.logic.AsCmd;
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
        ScriptCache scriptCache = new ScriptCache();
        system.scriptedModules().values().forEach(scriptCache::cache);
        system.reloadScripts(false);

        // config load
        config.loadEntireConfig();
        system.scriptedModules().values().forEach(scriptCache::restore);
        scriptCache.clear();
        system.println("<- config loaded [bulk action completed]");

        // info request
        system.println("-> requesting data from https://itzispyder.github.io/");
        BulletinBoard.request().thenRun(ClickCrystals::checkUpdates);

        String result = "Reloaded ClickCrystals client! (%s)".formatted(timer.end().getStampPrecise());
        system.println(result);
        ChatUtils.sendPrefixMessage(result);
    }

    private static class ScriptCache {
        private final Map<String, CacheData> scriptCache;

        public ScriptCache() {
            this.scriptCache = new HashMap<>();
        }

        public void cache(ScriptedModule m) {
            CacheData data = new CacheData(m.isEnabled(), m.getData().getBind().getKey());
            scriptCache.put(m.getId(), data);
        }

        public void restore(ScriptedModule m) {
            CacheData data = scriptCache.get(m.getId());
            if (data == null)
                return;
            m.setEnabled(data.enabled, false);
            m.getData().getBind().setKey(data.bind);
        }

        public void clear() {
            scriptCache.clear();
        }

        public record CacheData(boolean enabled, int bind) {

        }
    }
}
