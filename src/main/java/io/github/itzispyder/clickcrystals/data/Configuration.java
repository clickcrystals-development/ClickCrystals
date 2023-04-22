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
     */
    public ConfigSection<?> get(String key) {
        return sections.get(key);
    }

    public Object getObject(String key) {
        return get(key).get();
    }

    public <T extends Serializable> T get(String key, Class<T> klass) {
        return (T) getObject(key);
    }

    public <T extends Serializable> T getOrDefault(String key, Class<T> klass, T def) {
        return get(key) != null ? getObject(key) != null ? get(key,klass) : def : def;
    }

    public int getInt(String key) {
        return getOrDefault(key, Integer.class,0);
    }

    public boolean getBoolean(String key) {
        return getOrDefault(key, Boolean.class,false);
    }

    public float getFloat(String key) {
        return getOrDefault(key, Float.class,0.0F);
    }

    public double getDouble(String key) {
        return getOrDefault(key, Double.class,0.0d);
    }

    public byte getByte(String key) {
        return getOrDefault(key, Byte.class, (byte) 0);
    }

    public short getShort(String key) {
        return getOrDefault(key, Short.class, (short) 0);
    }

    public long getLong(String key) {
        return getOrDefault(key, Long.class,0L);
    }

    public Module getModule(String key) {
        return get(key, Module.class);
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
