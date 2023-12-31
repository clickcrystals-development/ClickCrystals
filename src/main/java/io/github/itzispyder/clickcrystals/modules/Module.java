package io.github.itzispyder.clickcrystals.modules;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.settings.SettingContainer;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Module implements Toggleable, Global, SettingContainer {

    private ModuleData data;
    private final String name, id;
    private String description;
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

    public String getDescriptionLimited() {
        if (description.length() <= 35) return description;
        return description.substring(0, 35) + "...";
    }

    public void setDescription(String description) {
        this.description = description;
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
        return isEnabled() ? "§bon" : "§7off";
    }

    public String getSearchQuery() {
        String norm = id.toLowerCase() + ";" + name.toLowerCase() + ";" + description.toLowerCase().replaceAll("[^a-z0-9 ]"," ");
        return norm + ";" + norm.replaceAll(" ", "").trim();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Module> T get(Class<T> moduleClass) {
        return (T)system.modules().get(moduleClass);
    }

    public static <T extends Module> void acceptFor(Class<T> moduleClass, Consumer<T> action) {
        T m = get(moduleClass);
        if (m != null) {
            action.accept(m);
        }
    }

    public static <T extends Module> boolean isEnabled(Class<T> moduleClass) {
        T module = get(moduleClass);
        return module != null && module.isEnabled();
    }

    public static <T extends Module, R> R getFrom(Class<T> moduleClass, Function<T, R> action) {
        T module = get(moduleClass);

        if (module == null) {
            return null;
        }
        else {
            return action.apply(module);
        }
    }

    public static void disableAllGameJoinDisabled() {
        system.collectModules().stream().filter(m -> {
            return m.getData().isGameJoinDisable();
        }).forEach(m -> {
            m.setEnabled(false, true);
        });
    }
}
