package io.github.itzispyder.clickcrystals.data;

import com.google.gson.Gson;
import io.github.itzispyder.clickcrystals.util.FileValidationUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ConfigFile {

    private final String path;
    private final Map<String, Object> entries;

    public ConfigFile(String path) {
        this.path = path;
        this.entries = new HashMap<>();
    }

    public void set(String key, Object val) {
        if (val == null || val.getClass().isPrimitive()) {
            entries.remove(key);
        }
        else {
            entries.put(key, val);
        }
    }

    public <T> T get(String key, Class<T> type) {
        try {
            return (T)entries.get(key);
        }
        catch (Exception ex) {
            return null;
        }
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
                Gson gson = new Gson();
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
