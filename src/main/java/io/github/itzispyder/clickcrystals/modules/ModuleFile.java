package io.github.itzispyder.clickcrystals.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.itzispyder.clickcrystals.modules.settings.EnumSetting;
import io.github.itzispyder.clickcrystals.modules.settings.KeybindSetting;
import io.github.itzispyder.clickcrystals.modules.settings.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.FileValidationUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ModuleFile {

    private final String id;
    private final Map<String, Object> entries;

    public ModuleFile(Module module) {
        this.id = module.getId();
        this.entries = new HashMap<>();

        module.getData().forEach(this::add);
    }

    public <T> void add(ModuleSetting<T> setting) {
        if (setting instanceof KeybindSetting bindSetting) {
            entries.put(bindSetting.getId(), bindSetting.getKey());
        }
        else {
            entries.put(setting.getId(), setting.getVal());
        }
    }

    public <T> void revert(ModuleSetting<T> setting) {
        if (setting instanceof KeybindSetting bindSetting) {
            double key = (Double)entries.getOrDefault(bindSetting.getId(), bindSetting.getDefKey());
            bindSetting.setKey((int)key);
        }
        else if (setting instanceof EnumSetting<?> enumSetting) {
            String key = (String)entries.getOrDefault(enumSetting.getId(), enumSetting.getDef().name());
            enumSetting.setVal(Enum.valueOf(enumSetting.getDef().getClass(), key));
        }
        else {
            setting.setVal(entries.getOrDefault(setting.getId(), setting.getDef()));
        }
    }

    public void add(SettingSection section) {
        section.forEach(this::add);
    }

    public Map<String, Object> getEntries() {
        return entries;
    }

    public String getId() {
        return id;
    }

    public void save() {
        save(this);
    }

    public static void save(ModuleFile module) {
        String path = "ClickCrystalsClient/modules/" + module.id + ".json";
        File file = new File(path);

        if (FileValidationUtils.validate(file)) {
            try {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(module);

                if (json == null) {
                    throw new IllegalStateException();
                }

                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);

                bw.write(json);
                bw.close();
            }
            catch (Exception ex) {
                System.out.println(file.getName() + " has failed to save!");
                ex.printStackTrace();
            }
        }
    }

    public static ModuleFile load(Module module) {
        String path = "ClickCrystalsClient/modules/" + module.getId() + ".json";
        File file = new File(path);

        if (FileValidationUtils.validate(file)) {
            try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                Gson gson = new Gson();
                ModuleFile moduleFile = gson.fromJson(br, ModuleFile.class);

                br.close();

                if (moduleFile == null) {
                    throw new IllegalStateException();
                }
                else {
                    ModuleData data = module.getData();
                    for (SettingSection section : data.getSettingSections()) {
                        for (ModuleSetting<?> setting : section.getSettings()) {
                            moduleFile.revert(setting);
                        }
                    }
                    return moduleFile;
                }
            }
            catch (Exception ignore) {}
        }
        return new ModuleFile(module);
    }
}
