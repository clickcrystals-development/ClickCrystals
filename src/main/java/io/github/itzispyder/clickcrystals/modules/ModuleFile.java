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

        if (setting instanceof KeybindSetting v) {
            integerEntries.put(id, v.getKey());
        }
        else if (d instanceof Integer v) {
            integerEntries.put(id, v);
        }
        else if (d instanceof Double v) {
            doubleEntries.put(id, v);
        }
        else if (d instanceof Boolean v) {
            booleanEntries.put(id, v);
        }
        else if (d instanceof String v) {
            stringEntries.put(id, v);
        }
        else if (d instanceof Enum<?> v) {
            stringEntries.put(id, v.name());
        }
        else {
            objectEntries.put(id, d);
        }
    }

    public <T> void revert(ModuleSetting<T> setting) {
        String id = setting.getId();

        if (setting instanceof KeybindSetting v) {
            v.setKey(integerEntries.getOrDefault(id, v.getDefKey()));
        }
        else if (setting instanceof IntegerSetting v) {
            v.setVal(integerEntries.getOrDefault(id, v.getDef()));
        }
        else if (setting instanceof DoubleSetting v) {
            v.setVal(doubleEntries.getOrDefault(id, v.getDef()));
        }
        else if (setting instanceof BooleanSetting v) {
            v.setVal(booleanEntries.getOrDefault(id, v.getDef()));
        }
        else if (setting instanceof StringSetting v) {
            v.setVal(stringEntries.getOrDefault(id, v.getDef()));
        }
        else if (setting instanceof EnumSetting<?> v) {
            String val = stringEntries.getOrDefault(id, v.getDef().name());
            for (Enum cons : v.getDef().getClass().getEnumConstants())
                if (cons.name().endsWith(val))
                    v.setVal(cons);
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
            this.putAll(objectEntries);
            this.putAll(integerEntries);
            this.putAll(doubleEntries);
            this.putAll(booleanEntries);
            this.putAll(stringEntries);
        }};
    }

    public String getId() {
        return id;
    }

    private <T> T getOrDef(T val, T def) {
        return val != null ? val : def;
    }
}
