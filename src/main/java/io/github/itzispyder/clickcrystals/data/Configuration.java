package io.github.itzispyder.clickcrystals.data;

import io.github.itzispyder.clickcrystals.modules.Module;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Configuration system
 */
public class Configuration implements Serializable {

    private final Map<String,ConfigSection<?>> sections;
    private final File file;

    /**
     * Constructs a new config
     * @param file file to save to
     */
    public Configuration(File file) {
        this.sections = new HashMap<>();
        this.file = file;
    }

    /**
     * Gets a map of configuration sections
     * @return sections
     */
    public Map<String, ConfigSection<?>> getSections() {
        return new HashMap<>(sections);
    }

    /**
     * Sets a configuration section and maps it to its String key
     * @param key key
     * @param section section
     * @param <T> section type
     */
    public <T extends Serializable> void set(String key, ConfigSection<T> section) {
        sections.put(key,section);
    }

    /**
     * Gets a section value
     * @param key key
     * @return section value
     * @param <T> value type
     */
    public <T extends Serializable> ConfigSection<T> get(String key) {
        return (ConfigSection<T>) sections.get(key);
    }

    /**
     * Gets a configuration section value
     * @param key section key
     * @return section value
     */
    public ConfigSection<String> getString(String key) {
        return get(key) != null ? get(key) : new ConfigSection<>("null");
    }

    /**
     * Gets a configuration section value
     * @param key section key
     * @return section value
     */
    public ConfigSection<Integer> getInt(String key) {
        return get(key) != null ? get(key) : new ConfigSection<>(0);
    }

    /**
     * Gets a configuration section value
     * @param key section key
     * @return section value
     */
    public ConfigSection<Float> getFloat(String key) {
        return get(key) != null ? get(key) : new ConfigSection<>(0.0F);
    }

    /**
     * Gets a configuration section value
     * @param key section key
     * @return section value
     */
    public ConfigSection<Boolean> getBoolean(String key) {
        return get(key) != null ? get(key) : new ConfigSection<>(false);
    }

    /**
     * Gets a configuration section value
     * @param key section key
     * @return section value
     */
    public ConfigSection<Double> getDouble(String key) {
        return get(key) != null ? get(key) : new ConfigSection<>(0.0d);
    }

    /**
     * Gets a configuration section value
     * @param key section key
     * @return section value
     */
    public ConfigSection<Module> getModule(String key) {
        return get(key);
    }

    /**
     * Gets a configuration section value
     * @param key section key
     * @return section value
     */
    public ConfigSection<Byte> getByte(String key) {
        return get(key) != null ? get(key) : new ConfigSection<>((byte) 0);
    }

    /**
     * Saves the configuration to its file
     */
    public void save() {
        save(this,file);
    }

    /**
     * Saves a configuration to a file
     * @param config configuration
     * @param file file to save to
     */
    public static void save(Configuration config, File file) {
        try {
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            GZIPOutputStream gos = new GZIPOutputStream(bos);
            ObjectOutputStream oos = new ObjectOutputStream(gos);
            oos.writeObject(config);
            oos.close();
        }
        catch (Exception ex) {
            System.out.println("An error occurred while trying to save configuration '" + file.getPath() + "'");
            ex.printStackTrace();
        }
    }

    /**
     * Loads a configuration from a file
     * @param file file to load from
     * @return loaded configuration
     */
    public static Configuration load(File file) {
        try {
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();

            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            GZIPInputStream gis = new GZIPInputStream(bis);
            ObjectInput ois = new ObjectInputStream(gis);
            Configuration config = (Configuration) ois.readObject();
            ois.close();
            return config;
        }
        catch (Exception ex) {
            System.out.println("An error occurred while trying to load configuration '" + file.getPath() + "'");
            ex.printStackTrace();
            return null;
        }
    }
}
