package io.github.itzispyder.clickcrystals.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.itzispyder.clickcrystals.gui.Positionable;
import io.github.itzispyder.clickcrystals.util.FileValidationUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ConfigFile {

    private final String path;
    private Map<String, Object> entries;
    private Map<String, Positionable.Dimension> positionableEntries;

    public ConfigFile(String path) {
        this.path = path;
        this.entries = new HashMap<>();
        this.positionableEntries = new HashMap<>();
    }

    public void checkEntries() {
        if (entries == null) {
            entries = new HashMap<>();
        }

        if (positionableEntries == null) {
            positionableEntries = new HashMap<>();
        }
    }

    public void set(String key, Object val) {
        checkEntries();
        if (val == null || val.getClass().isPrimitive()) {
            entries.remove(key);
        }
        else {
            entries.put(key, val);
        }
    }

    public <T> T get(String key, Class<T> type) {
        checkEntries();
        try {
            return (T)entries.get(key);
        }
        catch (Exception ex) {
            return null;
        }
    }

    public void setPositionable(String key, Positionable.Dimension dim) {
        checkEntries();
        positionableEntries.put(key, dim);
    }

    public Positionable.Dimension getPositionable(String key) {
        checkEntries();
        return positionableEntries.get(key);
    }

    public Positionable.Dimension getPositionable(String key, Positionable.Dimension def) {
        checkEntries();
        return positionableEntries.getOrDefault(key, def);
    }

    public <T> T get(String key, Class<T> type, T def) {
        return getOrDef(get(key, type), def);
    }

    public <T> T getOrDef(T val, T def) {
        return val == null ? def : val;
    }

    public String getPath() {
        return path;
    }

    public void save() {
        save(this);
    }

    public static void save(ConfigFile module) {
        String path = module.path;
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

    public static ConfigFile load(String path) {
        File file = new File(path);

        if (FileValidationUtils.validate(file)) {
            try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                Gson gson = new Gson();
                ConfigFile configFile = gson.fromJson(br, ConfigFile.class);

                br.close();

                if (configFile == null) {
                    throw new IllegalStateException();
                }
                else {
                    return configFile;
                }
            }
            catch (Exception ignore) {}
        }
        return new ConfigFile(path);
    }
}
