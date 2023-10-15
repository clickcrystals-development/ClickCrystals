package io.github.itzispyder.clickcrystals.client.system;

import io.github.itzispyder.clickcrystals.client.client.CapeManager;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.events.EventBus;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.gui_beta.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.scheduler.Scheduler;
import io.github.itzispyder.clickcrystals.util.misc.Randomizer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.*;

import static io.github.itzispyder.clickcrystals.ClickCrystals.prefix;

public class ClickCrystalsSystem implements Serializable {

    private static final ClickCrystalsSystem system = new ClickCrystalsSystem();
    public static ClickCrystalsSystem getInstance() {
        return system;
    }


    public final EventBus eventBus = new EventBus();
    public final CapeManager capeManager = new CapeManager();
    public final Randomizer random = new Randomizer();
    public final Scheduler scheduler = new Scheduler();
    private final Map<Class<? extends Module>, Module> modules;
    private final Map<Class<? extends Command>, Command> commands;
    private final Map<Class<? extends Hud>, Hud> huds;
    private final Set<Keybind> keybinds;

    public ClickCrystalsSystem() {
        this.commands = new HashMap<>();
        this.modules = new HashMap<>();
        this.huds = new HashMap<>();
        this.keybinds = new HashSet<>();
    }

    public void openUrl(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        }
        catch (URISyntaxException | IOException ex) {
            ex.printStackTrace();
        }
    }

    public void openFile(String path) {
        try {
            Desktop.getDesktop().open(new File(path));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void addCommand(Command command) {
        if (command == null) return;
        commands.put(command.getClass(), command);
        command.register();
    }

    public void addModule(Module module) {
        if (module == null) return;
        modules.put(module.getClass(),module);
    }

    public void addHud(Hud hud) {
        HudRenderCallback.EVENT.register(hud);
        huds.put(hud.getClass(), hud);
    }

    public void addKeybind(Keybind bind) {
        if (bind == null) return;
        this.keybinds.add(bind);
    }

    public void removeKeybind(Keybind bind) {
        this.keybinds.remove(bind);
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

    public Map<Class<? extends Hud>, Hud> huds() {
        return new HashMap<>(huds);
    }

    public Set<Keybind> keybinds() {
        return new HashSet<>(keybinds);
    }

    public List<Keybind> getBindsOf(int key) {
        return keybinds().stream().filter(bind -> bind.getKey() == key).toList();
    }

    public void prefixPrint(String msg) {
        System.out.println(prefix + msg);
    }
}
