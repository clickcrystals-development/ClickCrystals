package io.github.itzispyder.clickcrystals.modules;

import io.github.itzispyder.clickcrystals.client.ClickCrystalsSystem;
import io.github.itzispyder.clickcrystals.data.ConfigSection;
import io.github.itzispyder.clickcrystals.modules.settings.KeybindSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import net.minecraft.client.MinecraftClient;

import java.io.Serializable;

import static io.github.itzispyder.clickcrystals.ClickCrystals.config;
import static io.github.itzispyder.clickcrystals.ClickCrystals.starter;

public abstract class Module implements Toggleable, Serializable {

    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    protected static final ClickCrystalsSystem system = ClickCrystalsSystem.getInstance();
    public static int totalEnabled;
    private ModuleData data;
    private final String name, description, id;
    private final Category category;

    public Module(String name, Category category, String description) {
        this.data = new ModuleData(this);
        this.name = StringUtils.capitalizeWords(name);
        this.id = name;
        this.category = category;
        this.description = description;
    }

    protected abstract void onEnable();

    protected abstract void onDisable();

    protected SettingSection getGeneralSection() {
        return data.scGeneral;
    }

    protected SettingSection createSettingSection(String name) {
        SettingSection group = new SettingSection(name);
        this.data.addSettingSection(group);
        return group;
    }

    @Override
    public boolean isEnabled() {
        return data.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.setEnabled(enabled,true);
    }

    public void setEnabled(boolean enabled, boolean sendFeedback) {
        /*
        if (enabled) {
            this.onEnable();
        }
        else {
            this.onDisable();
        }
         */

        this.data.setEnabled(enabled);

        if (sendFeedback) {
            this.sendUpdateInfo();
        }
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public ModuleData getData() {
        return data;
    }

    public void setData(ModuleData data) {
        this.data = data;
    }

    public String getNameLimited() {
        if (name.length() <= 14) return name;
        return name.substring(0, 14) + "...";
    }

    public String getId() {
        return id;
    }

    public String getHelp() {
        return " \n" + starter + "§f" + name +
                "\n" + "§3Category: §b" + category.name() +
                "\n" + "§7" + description + "\n ";
    }

    public void sendUpdateInfo() {
        ChatUtils.sendPrefixMessage("§b" + name + " §3is now toggled " + getOnOrOff());
    }

    public String getOnOrOff() {
        return isEnabled() ? "§aon" : "§coff";
    }

    public static <T extends Module> T get(Class<T> moduleClass) {
        return (T)system.modules().get(moduleClass);
    }

    public static void loadConfigModules() {
        for (Module module : system.modules().values()) {
            String path = "modules." + module.getId();
            ModuleData data = config.getModuleData(path);

            data.forEach(setting -> module.data.forEach(current -> {
                if (setting.getId().equals(current.getId())) {
                    if (setting instanceof KeybindSetting kS && current instanceof KeybindSetting kC) {
                        kC.setKey(kS.getKey());
                    }
                    else {
                        current.setVal(setting.getVal());
                    }
                }
            }));
            module.setEnabled(module.data.isEnabled(), false);
        }
    }

    public static void saveConfigModules() {
        for (Module module : system.modules().values()) {
            saveModule(module, false);
        }
        config.save();
    }

    public static void saveModule(Module module, boolean saveImmediately) {
        String path = "modules." + module.getId();
        ConfigSection<ModuleData> dataSection = new ConfigSection<>(module.getData());
        config.set(path, dataSection);

        if (saveImmediately) config.save();
    }

    public String getSearchQuery() {
        return id + ":" + description.toLowerCase().replaceAll("[^a-z0-9]","_");
    }
}
