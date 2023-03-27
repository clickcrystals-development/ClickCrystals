package io.github.itzispyder.clickcrystals.data;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Saves the configuration to a file
 */
public class Configuration implements Serializable {

    private final Map<String,ConfigSegment<? extends Serializable>> keys;

    /**
     * Constructs a new configuration
     */
    public Configuration() {
        this.keys = new HashMap<>();
    }

    /**
     * Returns a configuration segment based on the provided string key
     * @param key key
     * @return configuration segment
     */
    public ConfigSegment<? extends Serializable> get(String key) {
        return keys.get(key);
    }

    /**
     * Write a configuration segment to its corresponding key
     * @param key string key
     * @param segment segment object
     */
    public void write(String key, ConfigSegment<? extends Serializable> segment) {
        if (segment != null && segment.get() != null)
            keys.put(key, segment);
    }

    /**
     * Removes a configuration segment from the configuration
     * @param key string key
     */
    public void purge(String key) {
        keys.remove(key);
    }

    /**
     * Saves this current configuration to its file
     * @param file file
     */
    public void save(File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            GZIPOutputStream zos = new GZIPOutputStream(bos);
            ObjectOutputStream oos = new ObjectOutputStream(zos);
            oos.writeObject(this);
            oos.close();
        }
        catch (Exception ex) {
            System.out.println("Fail to save config for '" + file.getPath() + "'");
        }
    }

    /**
     * Loads a configuration from a file
     * @param file file
     * @return loaded configuration
     */
    public static Configuration load(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            GZIPInputStream zis = new GZIPInputStream(bis);
            ObjectInputStream ois = new ObjectInputStream(zis);
            Configuration config = (Configuration) ois.readObject();
            ois.close();
            return config;
        }
        catch (Exception ex) {
            System.out.println("Failed to load config for '" + file.getPath() + "'");
            return null;
        }
    }
}
