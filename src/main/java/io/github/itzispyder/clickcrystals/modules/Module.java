package io.github.itzispyder.clickcrystals.modules;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.client.ClickCrystalsSystem;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import net.minecraft.client.MinecraftClient;

import java.io.Serializable;

import static io.github.itzispyder.clickcrystals.ClickCrystals.starter;

/**
 * Represents a toggleable module
 */
public abstract class Module implements Toggleable, Serializable {

    protected static final MinecraftClient mc = ClickCrystals.mc;
    protected static final ClickCrystalsSystem system = ClickCrystals.system;
    protected boolean enabled;
    private final String name, description, id;

    /**
     * Module constructor
     * @param name name
     * @param description description
     */
    public Module(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = name.toLowerCase().replaceAll("[^a-z0-9]","_");
    }

    /**
     * On module enabled
     */
    protected abstract void onEnable();

    /**
     * On module disabled
     */
    protected abstract void onDisable();

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    /**
     * Gets the name of the module, but in a format that can be saved as a file and
     * without any special characters.
     * @return
     */
    public String getId() {
        return id;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.setEnabled(enabled,true);
    }

    /**
     * Sets the module's enabled state
     * @param enabled state
     * @param sendFeedback send feedback
     */
    public void setEnabled(boolean enabled, boolean sendFeedback) {
        if (enabled) this.onEnable();
        else this.onDisable();
        this.enabled = enabled;
        if (sendFeedback) this.sendUpdateInfo();
    }

    /**
     * Module help
     * @return help message
     */
    public String getHelp() {
        return " \n" + starter + "§f" + name + "\n" + "§7" + description + "\n ";
    }

    /**
     * Send the module update info to the player
     */
    public void sendUpdateInfo() {
        if (enabled) ChatUtils.sendPrefixMessage("§b" + name + " §3is now toggled " + getToggledStateMessage());
        else ChatUtils.sendPrefixMessage("§b" + name + " §3is now toggled " + getToggledStateMessage());
    }

    /**
     * Current state label of the button
     * @return label
     */
    public String getCurrentStateLabel() {
        return "§b" + name + "§7: " + getToggledStateMessage();
    }

    /**
     * Returns a message corresponding to the module is enabled state.
     * Will return a red "off" or a green "on"
     * @return message
     */
    public String getToggledStateMessage() {
        return enabled ? "§aon" : "§coff";
    }

    /**
     * Gets the module represented by the module class
     * @param moduleClass class
     * @return module
     */
    public static Module get(Class<? extends Module> moduleClass) {
        return system.modules().get(moduleClass);
    }
}
