package io.github.itzispyder.clickcrystals.modules;

import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModuleData implements Serializable {

    public final SettingSection scGeneral = new SettingSection("general-settings");
    public final SettingSection scDefault = new SettingSection("module-defaults");
    private final ModuleSetting<Boolean> enabled = scDefault.add(BooleanSetting.create()
            .name("module-enabled")
            .description("Module enabled or disabled.")
            .def(false)
            .val(false)
            .build()
    );
    private final List<SettingSection> settingSections;

    public ModuleData() {
        this.settingSections = new ArrayList<>();
        this.settingSections.add(scGeneral);
        this.settingSections.add(scDefault);
    }

    public boolean isEnabled() {
        return enabled.getVal();
    }

    public void setEnabled(boolean enabled) {
        this.enabled.setVal(enabled);
    }

    public void addSettingSection(SettingSection section) {
        this.settingSections.add(settingSections.size() - 1, section);
    }

    public void removeSettingSection(SettingSection section) {
        this.settingSections.remove(section);
    }

    public List<SettingSection> getSettingSections() {
        return settingSections;
    }

    public void forEach(Consumer<ModuleSetting<?>> action) {
        this.settingSections.forEach(group -> group.forEach(action));
    }
}
