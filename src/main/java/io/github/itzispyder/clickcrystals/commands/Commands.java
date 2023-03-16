package io.github.itzispyder.clickcrystals.commands;

import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

/**
 * Client command registry and management
 */
public abstract class Commands {

    /**
     * Registers a command
     * @param command command
     */
    public static void register(Command command) {
        system.addCommand(command);
    }
}
