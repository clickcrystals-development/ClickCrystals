package io.github.itzispyder.clickcrystals.modules;

import io.github.itzispyder.clickcrystals.modules.settings.*;

import java.util.HashMap;
import java.util.Map;

public class ModuleFile {

    private final String id;
    private final Map<String, Object> objectEntries;
    private final Map<String, Integer> integerEntries;
    private final Map<String, Double> doubleEntries;
    private final Map<String, Boolean> booleanEntries;
    private final Map<String, String> stringEntries;

    public ModuleFile(Module module) {
        this.id = module.getId();
        this.objectEntries = new HashMap<>();
        this.integerEntries = new HashMap<>();
        this.doubleEntries = new HashMap<>();
        this.booleanEntries = new HashMap<>();
        this.stringEntries = new HashMap<>();

        module.getData().forEach(this::add);
    }

    public <T> void add(ModuleSetting<T> setting) {
        T d = setting.getVal();
        String id = setting.getId();

        if (setting instanceof KeybindSetting) {
            integerEntries.put(id, ((KeybindSetting) setting).getKey());
        }
        else if (d instanceof Integer) {
            integerEntries.put(id, (Integer) d);
        }
        else if (d instanceof Double) {
            doubleEntries.put(id, (Double) d);
        }
        else if (d instanceof Boolean) {
            booleanEntries.put(id, (Boolean) d);
        }
        else if (d instanceof String) {
            stringEntries.put(id, (String) d);
        }
        else if (d instanceof Enum<?>) {
            stringEntries.put(id, ((Enum<?>) d).name());
        }
        else {
            objectEntries.put(id, d);
        }
    }

    public <T> void revert(ModuleSetting<T> setting) {
        String id = setting.getId();

        if (setting instanceof KeybindSetting) {
            ((KeybindSetting) setting).setKey(integerEntries.getOrDefault(id, ((KeybindSetting) setting).getDefKey()));
        }
        else if (setting instanceof IntegerSetting) {
            ((IntegerSetting) setting).setVal(integerEntries.getOrDefault(id, ((IntegerSetting) setting).getDef()));
        }
        else if (setting instanceof DoubleSetting) {
            ((DoubleSetting) setting).setVal(doubleEntries.getOrDefault(id, ((DoubleSetting) setting).getDef()));
        }
        else if (setting instanceof BooleanSetting) {
            ((BooleanSetting) setting).setVal(booleanEntries.getOrDefault(id, ((BooleanSetting) setting).getDef()));
        }
        else if (setting instanceof StringSetting) {
            ((StringSetting) setting).setVal(stringEntries.getOrDefault(id, ((StringSetting) setting).getDef()));
        }
        else if (setting instanceof EnumSetting<?>) {
            ((EnumSetting<?>) setting).setVal(Enum.valueOf(((EnumSetting<?>) setting).getDef().getClass(), stringEntries.getOrDefault(id, ((EnumSetting<?>) setting).getDef().name())));
        }
        else {
            setting.setVal(objectEntries.getOrDefault(id, setting.getDef()));
        }
    }

    public void add(SettingSection section) {
        section.forEach(this::add);
    }

    public Map<String, Object> getObjectEntries() {
        return objectEntries;
    }

    public Map<String, Integer> getIntegerEntries() {
        return integerEntries;
    }

    public Map<String, Double> getDoubleEntries() {
        return doubleEntries;
    }

    public Map<String, Boolean> getBooleanEntries() {
        return booleanEntries;
    }

    public Map<String, String> getStringEntries() {
        return stringEntries;
    }

    public Map<String, Object> getAllEntries() {
        return new HashMap<>() {{
            putAll(objectEntries);
            putAll(integerEntries);
            putAll(doubleEntries);
            putAll(booleanEntries);
            putAll(stringEntries);
        }};
    }

    public String getId() {
        return id;
    }

    private <T> T getOrDef(T val, T def) {
        return val != null ? val : def;
    }
}
