package io.github.itzispyder.clickcrystals.gui.screens.scripts;

import com.google.gson.Gson;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.data.Config;
import io.github.itzispyder.clickcrystals.util.FileValidationUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public record ScriptInfo(Info... scripts) implements Global {

    private static final AtomicReference<ScriptInfo> current = new AtomicReference<>(null);
    public static final String URL = "https://clickcrystals.xyz/scripts/scripts.json";

    public static ScriptInfo getCurrent() {
        return current.get();
    }

    public static boolean hasCurrent() {
        return getCurrent() != null;
    }

    public static ScriptInfo createNull() {
        return new ScriptInfo();
    }

    public static synchronized CompletableFuture<Void> request() {
        return CompletableFuture.runAsync(ScriptInfo::get);
    }

    private static synchronized void get() {
        try {
            java.net.URL u = new URL(URL);
            InputStream is = u.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            Gson gson = new Gson();
            ScriptInfo info = gson.fromJson(isr, ScriptInfo.class);

            if (info == null) {
                throw new IllegalStateException("json parse failed!");
            }

            isr.close();
            current.set(info);
        }
        catch (Exception ex) {
            system.printErr("Error requesting online scripts");
            system.printErr(ex.getMessage());
        }
    }

    public void download(Info info) {
        try {
            URL url = new URL(info.toURL());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("User-Agent", "ClickCrystals Script Downloader");
            conn.setDoInput(true);

            InputStream is = conn.getInputStream();

            File file = new File(info.toLocalPath());
            FileValidationUtils.validate(file);
            FileOutputStream fos = new FileOutputStream(file);

            fos.write(is.readAllBytes());
            fos.flush();
            fos.close();

            is.close();
            conn.disconnect();
        }
        catch (Exception ex) {
            system.logger.error("Failed to download script: " + ex.getMessage());
        }
    }

    public boolean alreadyOwns(Info info) {
        return new File(info.toLocalPath()).exists();
    }

    public record Info(String name, String author, String desc, String contents) {
        public String toURL() {
            return "https://clickcrystals.xyz/scripts/" + contents;
        }

        public String toLocalPath() {
            return Config.PATH_SCRIPTS + "downloads/" + toFileName() + ".ccs";
        }

        public String toFileName() {
            return contents.replaceAll("(.*/)|(\\..+)", "");
        }
    }
}