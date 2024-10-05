package io.github.itzispyder.clickcrystals.gui.screens.profiles;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.data.Config;
import io.github.itzispyder.clickcrystals.util.FileValidationUtils;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public record ProfileInfo(Info... configs) implements Global {

    private static final AtomicReference<ProfileInfo> current = new AtomicReference<>(null);
    public static final String URL = "https://itzispyder.github.io/clickcrystals/configs/configs.json";

    public static ProfileInfo getCurrent() {
        return current.get();
    }

    public static boolean hasCurrent() {
        return getCurrent() != null;
    }

    public static ProfileInfo createNull() {
        return new ProfileInfo();
    }

    public static synchronized CompletableFuture<Void> request() {
        return CompletableFuture.runAsync(ProfileInfo::get);
    }

    private static synchronized void get() {
        try {
            java.net.URL u = new URL(URL);
            InputStream is = u.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            Gson gson = new Gson();
            ProfileInfo info = gson.fromJson(isr, ProfileInfo.class);

            if (info == null) {
                throw new IllegalStateException("json parse failed!");
            }

            isr.close();
            current.set(info);
        }
        catch (Exception ex) {
            system.printErr("Error requesting online configuration profiles");
            system.printErr(ex.getMessage());
        }
    }

    public void download(Info info) {
        try {
            URL url = new URL(info.toURL());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("User-Agent", "ClickCrystals Profile Downloader");
            conn.setDoInput(true);

            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            JsonObject json = JsonParser.parseReader(isr).getAsJsonObject();
            json.addProperty("customPath", info.toLocalPath());

            isr.close();
            is.close();
            conn.disconnect();

            File file = new File(info.toLocalPath());
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            FileValidationUtils.validate(file);
            FileValidationUtils.quickWrite(file, gson.toJson(json));
        }
        catch (Exception ex) {
            system.logger.error("Failed to download config: " + ex.getMessage());
        }
    }

    public boolean alreadyOwns(Info info) {
        return new File(info.toLocalPath()).exists();
    }

    public record Info(String name, String desc, String contents) {
        public String toURL() {
            return "https://itzispyder.github.io/clickcrystals/configs/" + contents;
        }

        public String toLocalPath() {
            return Config.PATH_PROFILES + toFileName() + ".json";
        }

        public String toFileName() {
            return contents.replaceAll("(.*/)|(\\..+)", "");
        }
    }
}