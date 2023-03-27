package io.github.itzispyder.clickcrystals.client;

import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.events.EventBus;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.modules.Module;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * ClickCrystal system
 */
public class ClickCrystalsSystem implements Serializable {

    public final EventBus eventBus = new EventBus();
    private final Map<Class<? extends Command>, Command> commands;
    private final Map<Class<? extends Module>, Module> modules;

    public ClickCrystalsSystem() {
        this.commands = new HashMap<>();
        this.modules = new HashMap<>();
    }

    /**
     * Add a command to the system
     * @param command client command
     */
    public void addCommand(Command command) {
        if (command == null) return;
        commands.remove(command.getClass());
        commands.put(command.getClass(),command);
        command.registerThis();
    }

    /**
     * Add a module to the system.
     * @param module toggleable module.
     */
    public void addModule(Module module) {
        if (module == null) return;
        modules.remove(module.getClass());
        modules.put(module.getClass(),module);
    }

    /**
     * Subscribe a listener to the event bus
     * @param listener listener
     */
    public void addListener(Listener listener) {
        eventBus.subscribe(listener);
    }

    /**
     * Unsubscribes a listener from the event bus
     * @param listener listener
     */
    public void removeListener(Listener listener) {
        eventBus.unsubscribe(listener);
    }

    public HashMap<Class<? extends Module>, Module> modules() {
        return new HashMap<>(modules);
    }

    public HashMap<Class<? extends Command>, Command> commands() {
        return new HashMap<>(commands);
    }

    public HashMap<Class<? extends Listener>, Listener> listeners() {
        return eventBus.listeners();
    }
}
