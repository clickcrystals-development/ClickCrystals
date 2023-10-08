package io.github.itzispyder.clickcrystals.data;

import io.github.itzispyder.clickcrystals.gui.Positionable;
import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleData;
import io.github.itzispyder.clickcrystals.modules.ModuleFile;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Config implements JsonSerializable<Config> {

    public static final String PATH = "ClickCrystalsClient/config.json";
    private final Map<String, Integer> keybindEntries;
    private final Map<String, Positionable.Dimension> positionEntries;
    private final Map<String, ModuleFile> moduleEntries;

    public Config() {
        this.keybindEntries = new HashMap<>();
        this.positionEntries = new HashMap<>();
        this.moduleEntries = new HashMap<>();
    }

    @Override
    public File getFile() {
        return new File(PATH);
    }

    public void saveKeybind(Keybind bind) {
        keybindEntries.put(bind.getId(), bind.getKey());
    }

    public void saveHud(Hud hud) {
        positionEntries.put(hud.getId(), hud.getDimensions());
    }

    public void saveModule(Module module) {
        moduleEntries.put(module.getId(), new ModuleFile(module));
    }

    public void saveModules() {
        system.modules().values().forEach(this::saveModule);
    }

    public void saveHuds() {
        system.huds().values().forEach(this::saveHud);
    }

    public void saveKeybinds() {
        system.keybinds().forEach(this::saveKeybind);
    }

    public void loadHuds() {
        for (Hud h : system.huds().values()) {
            Positionable.Dimension d = positionEntries.getOrDefault(h.getId(), h.getDefaultDimension());
            h.setX(d.x);
            h.setY(d.y);
        }
    }

    public void loadKeybinds() {
        for (Keybind b : system.keybinds()) {
            b.setKey(keybindEntries.getOrDefault(b.getId(), b.getDefaultKey()));
        }
    }

    public void loadModules() {
        for (Module m : system.modules().values()) {
            ModuleFile f = moduleEntries.getOrDefault(m.getId(), new ModuleFile(m));

            if (f == null) {
                throw new IllegalStateException("Failed to load module '%s'".formatted(m.getId()));
            }
            else {
                ModuleData data = m.getData();
                for (SettingSection section : data.getSettingSections()) {
                    for (ModuleSetting<?> setting : section.getSettings()) {
                        f.revert(setting);
                    }
                }
            }
        }
    }

    public void loadEntireConfig() {
        this.loadKeybinds();
        this.loadHuds();
        this.loadModules();
    }

    public Map<String, Integer> getKeybindEntries() {
        return keybindEntries;
    }

    public Map<String, Positionable.Dimension> getPositionEntries() {
        return positionEntries;
    }

    public Map<String, ModuleFile> getModuleEntries() {
        return moduleEntries;
    }
}
