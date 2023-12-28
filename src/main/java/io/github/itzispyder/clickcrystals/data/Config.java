package io.github.itzispyder.clickcrystals.data;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.Positionable;
import io.github.itzispyder.clickcrystals.gui.elements.overviewmode.CategoryElement;
import io.github.itzispyder.clickcrystals.gui.elements.overviewmode.SearchCategoryElement;
import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.OverviewScreen;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleData;
import io.github.itzispyder.clickcrystals.modules.ModuleFile;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.misc.Pair;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Config implements JsonSerializable<Config> {

    public static final String PATH = "ClickCrystalsClient/";
    public static final String PATH_CONFIG = PATH + "config.json";
    public static final String PATH_LOG = PATH + "current.log";
    public static final String PATH_SCRIPT_HISTORY_LOG = PATH + "script_history.log";
    public static final String PATH_SCRIPTS = PATH + "scripts";
    private boolean hasPlayedBefore, overviewMode;
    private final Map<String, Integer> keybindEntries;
    private final Map<String, Positionable.Dimension> positionEntries;
    private final Map<String, Pair<Positionable.Dimension, Boolean>> overviewScreenEntries;
    private final Map<String, ModuleFile> moduleEntries;

    public Config() {
        this.keybindEntries = new HashMap<>();
        this.positionEntries = new HashMap<>();
        this.overviewScreenEntries = new HashMap<>();
        this.moduleEntries = new HashMap<>();
    }

    @Override
    public File getFile() {
        return new File(PATH_CONFIG);
    }

    public void saveKeybind(Keybind bind) {
        keybindEntries.put(bind.getId(), bind.getKey());
    }

    public void saveHud(Hud hud) {
        positionEntries.put(hud.getId(), hud.getDimensions());
    }

    public void saveOverviewScreen(OverviewScreen screen) {
        for (GuiElement child : screen.getChildren()) {
            if (child instanceof CategoryElement e) {
                overviewScreenEntries.put(e.getCategory().name(), Pair.of(e.getDimensions(), e.isCollapsed()));
            }
            else if (child instanceof SearchCategoryElement e) {
                overviewScreenEntries.put("search-category-element", Pair.of(e.getDimensions(), false));
            }
        }
    }

    public void saveModule(Module module) {
        moduleEntries.put(module.getId(), new ModuleFile(module));
    }

    public void saveKeybinds() {
        this.keybindEntries.clear();
        system.keybinds().forEach(this::saveKeybind);
    }

    public void saveHuds() {
        this.positionEntries.clear();
        system.huds().values().forEach(this::saveHud);
    }

    public void saveModules() {
        this.moduleEntries.clear();
        system.collectModules().forEach(this::saveModule);
    }

    public void loadKeybinds() {
        for (Keybind b : system.keybinds()) {
            b.setKey(keybindEntries.getOrDefault(b.getId(), b.getDefaultKey()));
        }
    }

    public void loadHuds() {
        for (Hud h : system.huds().values()) {
            Positionable.Dimension d = positionEntries.getOrDefault(h.getId(), h.getDefaultDimension());
            h.setX(d.x);
            h.setY(d.y);
        }
    }

    public void loadModules() {
        system.collectModules().forEach(this::loadModule);
    }

    public void loadOverviewScreen(OverviewScreen screen) {
        for (GuiElement child : screen.getChildren()) {
            if (child instanceof CategoryElement e) {
                var d = overviewScreenEntries.get(e.getCategory().name());
                if (d != null) {
                    e.moveTo(d.left.x, d.left.y);
                    e.setCollapsed(d.right);
                }
            }
            else if (child instanceof SearchCategoryElement e) {
                var d = overviewScreenEntries.get("search-category-element");
                if (d != null) {
                    e.moveTo(d.left.x, d.left.y);
                }
            }
        }
    }

    public void loadModule(Module module) {
        ModuleFile f = moduleEntries.getOrDefault(module.getId(), new ModuleFile(module));

        if (f == null) {
            throw new IllegalStateException("Failed to load module '%s'".formatted(module.getId()));
        }
        else {
            ModuleData data = module.getData();
            for (SettingSection section : data.getSettingSections()) {
                for (ModuleSetting<?> setting : section.getSettings()) {
                    f.revert(setting);
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

    public Map<String, Pair<Positionable.Dimension, Boolean>> getOverviewScreenEntries() {
        return overviewScreenEntries;
    }

    public Map<String, ModuleFile> getModuleEntries() {
        return moduleEntries;
    }

    public boolean hasPlayedBefore() {
        return hasPlayedBefore;
    }

    public void markPlayedBefore() {
        if (!hasPlayedBefore) {
            hasPlayedBefore = true;
            save();
        }
    }

    public boolean isOverviewMode() {
        return overviewMode;
    }

    public void setOverviewMode(boolean overviewMode) {
        this.overviewMode = overviewMode;
    }
}
