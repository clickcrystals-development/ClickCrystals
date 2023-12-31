package io.github.itzispyder.clickcrystals.modules;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.KeybindSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModuleData implements Global {

    public final SettingSection scGeneral = new SettingSection("general-settings");
    public final SettingSection scDefault = new SettingSection("module-defaults");
    private final ModuleSetting<Boolean> enabled = scDefault.add(BooleanSetting.create()
            .name("module-enabled")
            .description("Module enabled or disabled.")
            .def(false)
            .val(false)
            .build()
    );
    private final ModuleSetting<Keybind> keybind = scDefault.add(KeybindSetting.create()
            .name("module-toggle-keybind")
            .description("The keybind for toggling modules quickly")
            .def(Keybind.NONE)
            .onPress(bind -> {})
            .condition((bind, screen) -> screen == null)
            .build()
    );
    private final ModuleSetting<Boolean> gameJoinDisable = scDefault.add(BooleanSetting.create()
            .name("module-game-join-disable")
            .description("Automatically disables the module upon joining a world.")
            .def(false)
            .val(false)
            .build()
    );
    private final List<SettingSection> settingSections;

    public ModuleData(Module module) {
        this.settingSections = new ArrayList<>();
        this.settingSections.add(scGeneral);
        this.settingSections.add(scDefault);

        this.enabled.setChangeAction(setting -> {
            boolean enabled = setting.getVal();
            if (module != null) {
                if (enabled) {
                    module.onEnable();
                }
                else {
                    module.onDisable();
                }

                ClickCrystals.config.saveModule(module);
            }
        });

        this.keybind.getVal().setKeyAction(bind -> {
            if (module != null) {
                module.setEnabled(!module.isEnabled(), true);
            }
        });
    }

    public boolean isEnabled() {
        return enabled.getVal();
    }

    public void setEnabled(boolean enabled) {
        this.enabled.setVal(enabled);
    }

    public boolean isGameJoinDisable() {
        return gameJoinDisable.getVal();
    }

    public void setGameJoinDisable(boolean gameJoinDisable) {
        this.gameJoinDisable.setVal(gameJoinDisable);
    }

    public void addSettingSection(SettingSection section) {
        this.settingSections.add(settingSections.size() - 1, section);
    }

    public Keybind getBind() {
        return keybind.getVal();
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
