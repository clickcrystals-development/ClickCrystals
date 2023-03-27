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

    public Configuration() {
        this.keys = new HashMap<>();
    }

    public ConfigSegment<? extends Serializable> get(String key) {
        return keys.get(key);
    }

    public void write(String key, ConfigSegment<? extends Serializable> segment) {
        if (segment != null && segment.get() != null)
            keys.put(key, segment);
    }

    public void purge(String key) {
        keys.remove(key);
    }

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
