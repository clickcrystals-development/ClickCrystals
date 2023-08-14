package io.github.itzispyder.clickcrystals.client.system;
// Imports
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.events.EventBus;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.modules.Module;https://github.com/ItziSpyder/ClickCrystals/pulls
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.io.Serializable;
import java.util.*;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;
// The main thing
public class ClickCrystalsSystem implements Serializable {

    private static final ClickCrystalsSystem system = new ClickCrystalsSystem();
    public static ClickCrystalsSystem getInstance() {
        return system;
    }


    public final EventBus eventBus = new EventBus();
    private final Map<Class<? extends Module>, Module> modules;
    private final Map<Class<? extends Command>, Command> commands;
    private final Map<Class<? extends HudRenderCallback>, HudRenderCallback> huds;
    private final Set<Keybind> keybinds;

    public ClickCrystalsSystem() {
        this.commands = new HashMap<>();
        this.modules = new HashMap<>();
        this.huds = new HashMap<>();
        this.keybinds = new HashSet<>();
    }
    // Functions to open Url
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
    // Add commands using this function (import and use the funtion)
    public void addCommand(Command command) {
        if (command == null) return;
        commands.put(command.getClass(), command);
        command.register();
    }
    // Add Modules using this function (import and use the funtion)
    public void addModule(Module module) {
        if (module == null) return;
        modules.put(module.getClass(),module);
    }
    // Add HUDs using this function (import and use the funtion)
    public void addHud(HudRenderCallback hudRenderer) {
        HudRenderCallback.EVENT.register(hudRenderer);
        huds.put(hudRenderer.getClass(), hudRenderer);
    }
    // Add keybinds using this function (import and use the funtion)
    public void addKeybind(Keybind bind) {
        if (bind == null) return;
        this.keybinds.add(bind);
    }
    // Remove keybinds using this function (import and use the funtion)
    public void removeKeybind(Keybind bind) {
        this.keybinds.remove(bind);
    }
    // Adds a listener ( A listener is something that recives a certain input )
    public void addListener(Listener listener) {
        eventBus.subscribe(listener);
    }
    // Removes Listners
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
        return new HashMap<>(huds);
    }

    public Set<Keybind> keybinds() {
        return new HashSet<>(keybinds);
    }

    public List<Keybind> getBindsOf(int key) {
        return keybinds().stream().filter(bind -> bind.getKey() == key).toList();
    }
}
