package io.github.itzispyder.clickcrystals.client.system;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.client.client.CapeManager;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.data.Config;
import io.github.itzispyder.clickcrystals.events.EventBus;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Category;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.modules.ScriptedModule;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.misc.Randomizer;
import io.github.itzispyder.clickcrystals.util.misc.Scheduler;
import net.minecraft.util.Util;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Consumer;

import static io.github.itzispyder.clickcrystals.ClickCrystals.config;

public class ClickCrystalsSystem implements Serializable {

    private static final ClickCrystalsSystem system = new ClickCrystalsSystem();
    public static ClickCrystalsSystem getInstance() {
        return system;
    }


    public final EventBus eventBus = new EventBus();
    public final CapeManager capeManager = new CapeManager();
    public final Randomizer random = new Randomizer();
    public final Scheduler scheduler = new Scheduler();
    public final ClickCrystalsLogger logger = new ClickCrystalsLogger(new File(Config.PATH_LOG));
    private final Map<Class<? extends Module>, Module> modules;
    private final Map<String, ScriptedModule> scriptedModules;
    private final Map<Class<? extends Command>, Command> commands;
    private final Map<Class<? extends Hud>, Hud> huds;
    private final Set<Keybind> keybinds;

    public ClickCrystalsSystem() {
        this.commands = new LinkedHashMap<>();
        this.modules = new LinkedHashMap<>();
        this.scriptedModules = new LinkedHashMap<>();
        this.huds = new LinkedHashMap<>();
        this.keybinds = new HashSet<>();
    }

    public void openUrl(String url) {
        try {
            Util.getOperatingSystem().open(new URI(url));
        }
        catch (URISyntaxException ex) {
            system.printErr("Failed to open url " + url);
            system.printErr(ex.getMessage());
        }
    }

    public void openFile(String path) {
        try {
            Util.getOperatingSystem().open(new File(path));
        }
        catch (Exception ex) {
            system.printErr("Failed to open file " + path);
            system.printErr(ex.getMessage());
        }
    }

    public void addCommand(Command command) {
        if (command == null) return;
        commands.put(command.getClass(), command);
        command.register();
    }

    public void addModule(Module module) {
        if (module == null) return;
        if (module instanceof ScriptedModule scripted) {
            scriptedModules.put(scripted.getId(), scripted);
        }
        else {
            modules.put(module.getClass(),module);
        }
    }

    public void addHud(Hud hud) {
        if (hud == null) return;
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

    public Map<String, ScriptedModule> scriptedModules() {
        return new HashMap<>(scriptedModules);
    }

    public List<Module> collectModules() {
        return new ArrayList<>() {{
            this.addAll(modules.values());
            this.addAll(scriptedModules.values());
        }};
    }

    public Map<Class<? extends Command>, Command> commands() {
        return new HashMap<>(commands);
    }

    public Set<Listener> listeners() {
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

    public List<Module> getModuleByCategory(Category category) {
        return collectModules().stream()
                .filter(m -> m.getCategory() == category)
                .sorted(Comparator.comparing(Module::getId))
                .toList();
    }

    public Module getModuleById(String id) {
        for (Module m : collectModules()) {
            if (m.getId().equalsIgnoreCase(id)) {
                return m;
            }
        }
        return null;
    }

    public void runModuleById(String id, Consumer<Module> action) {
        Module m = getModuleById(id);
        if (m != null) {
            action.accept(m);
        }
    }

    public void unloadModule(Module module) {
        if (module instanceof ScriptedModule scripted) {
            scripted.clearListeners();
            scriptedModules.remove(scripted.getId());
        }
        else {
            modules.remove(module.getClass());
        }

        if (module instanceof Listener listener) {
            removeListener(listener);
        }
        keybinds.remove(module.getData().getBind());
        module.setEnabled(false, false);
    }

    public void reloadScripts() {
        reloadScripts(true);
    }

    public void reloadScripts(boolean notifyClient) {
        println("-> reloading all scripts");

        if (notifyClient) {
            Notification.create()
                    .ccsIcon()
                    .stayTime(1000)
                    .id("clickscript-triggered-notification")
                    .title("ClickCrystals System")
                    .text("Reloading all scripts...")
                    .build()
                    .sendToClient();
        }

        scriptedModules().values().forEach(this::unloadModule);
        config.save();
        scriptedModules.clear();
        int total = ScriptedModule.runModuleScripts();
        scriptedModules.values().forEach(config::loadModule);

        println("<- Scripts reloaded!");

        if (notifyClient) {
            Notification.create()
                    .ccsIcon()
                    .stayTime(1000)
                    .id("clickscript-triggered-notification")
                    .title("ClickCrystals System")
                    .text("%s scripts reloaded!".formatted(total))
                    .build()
                    .sendToClient();
        }
    }

    public void onClientStopping() {
        println("Stopping client!");
        println("<- saving data...");
        ClickCrystals.config.saveKeybinds();
        ClickCrystals.config.saveHuds();
        ClickCrystals.config.saveModules();

        println("<- saving config...");
        ClickCrystals.config.save();
    }

    public void println(String msg) {
        logger.info(msg);
    }

    public void printErr(String msg) {
        logger.error(msg);
    }

    public void printf(String msg, Object... args) {
        println(StringUtils.format(msg, args));
    }

    public void printErrF(String msg, Object... args) {
        printErr(StringUtils.format(msg, args));
    }
}
