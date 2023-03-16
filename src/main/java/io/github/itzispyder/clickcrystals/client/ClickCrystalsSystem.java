package io.github.itzispyder.clickcrystals.client;

import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.events.EventBus;
import io.github.itzispyder.clickcrystals.events.Listener;

import java.util.HashMap;
import java.util.Map;

/**
 * ClickCrystal system
 */
public class ClickCrystalsSystem {

    public final EventBus eventBus = new EventBus();

    private final Map<Class<? extends Command>, Command> commands;

    public ClickCrystalsSystem() {
        this.commands = new HashMap<>();
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

    public void addListener(Listener listener) {
        eventBus.subscribe(listener);
    }

    public void removeListener(Listener listener) {
        eventBus.unsubscribe(listener);
    }
}
