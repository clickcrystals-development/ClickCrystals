package io.github.itzispyder.clickcrystals.modules;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.client.ClickCrystalsSystem;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import net.minecraft.client.MinecraftClient;

import static io.github.itzispyder.clickcrystals.ClickCrystals.starter;

/**
 * A toggleable module
 */
public abstract class Module implements Toggleable {

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

    public String getId() {
        return id;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) this.onEnable();
        else this.onDisable();
        this.enabled = enabled;
        this.sendUpdateInfo();
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
        if (enabled) ChatUtils.sendPrefixMessage("§b" + name + " §3is now toggled §aon");
        else ChatUtils.sendPrefixMessage("§b" + name + " §3is now toggled §coff");
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
