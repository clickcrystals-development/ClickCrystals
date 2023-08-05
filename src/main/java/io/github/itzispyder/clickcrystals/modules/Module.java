package io.github.itzispyder.clickcrystals.modules;

import io.github.itzispyder.clickcrystals.client.system.ClickCrystalsSystem;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import net.minecraft.client.MinecraftClient;

import static io.github.itzispyder.clickcrystals.ClickCrystals.starter;

public abstract class Module implements Toggleable {

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

    public static <T extends Module> boolean isEnabled(Class<T> moduleClass) {
        Module module = get(moduleClass);
        return module != null && module.isEnabled();
    }

    public static void loadConfigModules() {
        for (Module module : system.modules().values()) {
            ModuleFile.load(module).save();
        }
    }

    public static void saveConfigModules() {
        for (Module module : system.modules().values()) {
            saveModule(module);
        }
    }

    public static void saveModule(Module module) {
        ModuleFile file = new ModuleFile(module);
        file.save();
    }

    public String getSearchQuery() {
        return id + ":" + name + ":" + description.toLowerCase().replaceAll("[^a-z0-9]","_");
    }
}
