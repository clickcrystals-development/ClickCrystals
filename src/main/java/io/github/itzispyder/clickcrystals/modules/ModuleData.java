package io.github.itzispyder.clickcrystals.modules;

import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.Setting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModuleData implements Serializable {

    public final SettingGroup sgGeneral = new SettingGroup("general-settings");
    public final SettingGroup sgDefault = new SettingGroup("module-defaults");
    private final Setting<Boolean> enabled = sgDefault.add(BooleanSetting.create()
            .name("module-enabled")
            .description("Module enabled")
            .def(false)
            .val(false)
            .build()
    );
    private final List<SettingGroup> settingGroups;

    public ModuleData() {
        this.settingGroups = new ArrayList<>();
        this.settingGroups.add(sgGeneral);
        this.settingGroups.add(sgDefault);
    }

    public boolean isEnabled() {
        return enabled.getVal();
    }

    public void setEnabled(boolean enabled) {
        this.enabled.setVal(enabled);
    }

    public List<SettingGroup> getSettingGroups() {
        return settingGroups;
    }

    public void forEach(Consumer<Setting<?>> action) {
        this.settingGroups.forEach(group -> group.forEach(action));
    }
}
