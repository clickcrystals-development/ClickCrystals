package io.github.itzispyder.clickcrystals.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.util.FileValidationUtils;

import java.io.*;

public interface JsonSerializable<T> extends Global {

    File getFile();

    default String serialize(boolean pretty) {
        Gson gson;
        if (pretty) {
            gson = new GsonBuilder().setPrettyPrinting().create();
        }
        else {
            gson = new Gson();
        }

        try {
            String json = gson.toJson(this);
            if (json == null) {
                throw new IllegalStateException("json parse failed for " + this.getClass().getSimpleName());
            }
            return json;
        }
        catch (Exception ex) {
            return "{}";
        }
    }

    @SuppressWarnings("unchecked")
    default T deserialize(String json) {
        try {
            JsonSerializable<?> v = gson.fromJson(json, this.getClass());
            if (v == null) {
                throw new IllegalStateException("json parse failed");
            }
            return (T)v;
        }
        catch (Exception ex) {
            return null;
        }
    }

    default void save() {
        String json = serialize(true);
        File f = getFile();

        if (FileValidationUtils.validate(f)) {
            try {
                FileWriter fw = new FileWriter(f);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(json);
                bw.close();
            }
            catch (Exception ex) {
                system.printErr("JSON failed to save for " + f.getName());
            }
        }
    }

    default <O> O getOrDef(O val, O def) {
        return val != null ? val : def;
    }

    static <T extends JsonSerializable<?>> T load(File file, Class<T> jsonSerializable, T fallback) {
        if (FileValidationUtils.validate(file)) {
            try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                T t = gson.fromJson(br, jsonSerializable);

                if (t == null) {
                    throw new IllegalStateException("json parse failed!");
                }

                return t;
            }
            catch (Exception ex) {
                system.printErr("JSON failed to parse/load for " + file.getName());
            }
        }
        system.println("'%s' has failed to save, returning fallback! (If this is your first launch, you can ignore this message)".formatted(file.getPath()));
        return fallback;
    }

    static <T extends JsonSerializable<?>> T load(String path, Class<T> jsonSerializable, T fallback) {
        return load(new File(path), jsonSerializable, fallback);
    }
}
