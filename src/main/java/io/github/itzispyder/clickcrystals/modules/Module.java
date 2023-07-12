package io.github.itzispyder.clickcrystals.modules;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.client.ClickCrystalsSystem;
import io.github.itzispyder.clickcrystals.data.ConfigSection;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import net.minecraft.client.MinecraftClient;

import java.io.Serializable;

import static io.github.itzispyder.clickcrystals.ClickCrystals.config;
import static io.github.itzispyder.clickcrystals.ClickCrystals.starter;

/**
 * Represents a toggleable module
 */
public abstract class Module implements Toggleable, Serializable {

    protected static final MinecraftClient mc = ClickCrystals.mc;
    protected static final ClickCrystalsSystem system = ClickCrystals.system;
    public static int totalEnabled;
    protected boolean enabled;
    private final String name, description, id;
    private final Category category;


    public Module(String name, Category category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.id = name.toLowerCase().replaceAll("[^a-z0-9]","_");
    }

    protected abstract void onEnable();

    protected abstract void onDisable();

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getNameLimited() {
        if (name.length() <= 14) return name;
        return name.substring(0, 14) + "...";
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
        this.setEnabled(enabled,true);
    }

    public void setEnabled(boolean enabled, boolean sendFeedback) {
        if (enabled) this.onEnable();
        else this.onDisable();
        this.enabled = enabled;
        if (sendFeedback) this.sendUpdateInfo();
        saveModule(this,true);
        totalEnabled += enabled ? 1 : -1;
    }

    public String getHelp() {
        return " \n" + starter + "§f" + name +
                "\n" + "§3Category: §b" + category.name() +
                "\n" + "§7" + description + "\n ";
    }

    public void sendUpdateInfo() {
        if (enabled) ChatUtils.sendPrefixMessage("§b" + name + " §3is now toggled " + getToggledStateMessage());
        else ChatUtils.sendPrefixMessage("§b" + name + " §3is now toggled " + getToggledStateMessage());
    }

    public String getCurrentStateLabel() {
        return (enabled ? "§b" : "§7") + getNameLimited();
    }

    public String getToggledStateMessage() {
        return enabled ? "§aon" : "§coff";
    }

    public static Module get(Class<? extends Module> moduleClass) {
        return system.modules().get(moduleClass);
    }

    public static void loadConfigModules() {
        for (Module module : system.modules().values()) {
            final String path = "modules." + module.getId();

            boolean enabled = config.getBoolean(path + ".toggled");
            module.setEnabled(enabled,false);
        }
    }

    public static void saveConfigModules() {
        for (Module module : system.modules().values()) saveModule(module, false);
        config.save();
    }

    public static void saveModule(Module module, boolean saveImmediately) {
        final String path = "modules." + module.getId();

        ConfigSection<Boolean> toggleSection = new ConfigSection<>(module.isEnabled());
        config.set(path + ".toggled", toggleSection);

        if (saveImmediately) config.save();
    }

    @Override
    public String toString() {
        return id + ":" + description.toLowerCase().replaceAll("[^a-z0-9]","_");
    }
}
