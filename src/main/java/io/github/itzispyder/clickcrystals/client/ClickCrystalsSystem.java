package io.github.itzispyder.clickcrystals.client;

import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.events.EventBus;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public class ClickCrystalsSystem implements Serializable {

    private static final ClickCrystalsSystem system = new ClickCrystalsSystem();

    public static ClickCrystalsSystem getInstance() {
        return system;
    }

    public final EventBus eventBus = new EventBus();
    private final Map<Class<? extends Command>, Command> commands;
    private final Map<Class<? extends Module>, Module> modules;
    private final Map<Class<? extends HudRenderCallback>, HudRenderCallback> huds;

    public ClickCrystalsSystem() {
        this.commands = new HashMap<>();
        this.modules = new HashMap<>();
        this.huds = new HashMap<>();
    }

    public void openUrl(String url) {
        this.openUrl(url, "Are you sure you want to go to the following link?", mc.currentScreen);
    }

    public void openUrl(String url, String title) {
        this.openUrl(url, title, mc.currentScreen);
    }

    public void openUrl(String url, String title, Screen afterScreen) {
        mc.setScreen(new ConfirmLinkScreen(trusted -> {
            if (trusted) {
                Util.getOperatingSystem().open(url);
            }
            mc.setScreen(afterScreen);
        }, Text.literal(title), url, true));
    }

    public void addCommand(Command command) {
        if (command == null) return;
        commands.put(command.getClass(),command);
        command.registerThis();
    }

    public void addModule(Module module) {
        if (module == null) return;
        modules.put(module.getClass(),module);
    }

    public void addHud(HudRenderCallback hudRenderer) {
        HudRenderCallback.EVENT.register(hudRenderer);
        huds.put(hudRenderer.getClass(), hudRenderer);
    }

    public void addListener(Listener listener) {
        eventBus.subscribe(listener);
    }

    public void removeListener(Listener listener) {
        eventBus.unsubscribe(listener);
    }

    public Map<Class<? extends Module>, Module> modules() {
        return new HashMap<>(modules);
    }

    public Map<Class<? extends Command>, Command> commands() {
        return new HashMap<>(commands);
    }

    public Map<Class<? extends Listener>, Listener> listeners() {
        return eventBus.listeners();
    }

    public Map<Class<? extends HudRenderCallback>, HudRenderCallback> huds() {
        return huds;
    }
}
