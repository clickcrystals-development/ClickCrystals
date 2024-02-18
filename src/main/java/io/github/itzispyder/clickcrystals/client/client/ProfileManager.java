package io.github.itzispyder.clickcrystals.client.client;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.data.Config;
import io.github.itzispyder.clickcrystals.data.JsonSerializable;
import io.github.itzispyder.clickcrystals.util.StringUtils;

import java.io.File;
import java.util.Arrays;

public class ProfileManager  {

    public final ProfileConfig profileConfig = JsonSerializable.load(Config.PATH_CONFIG_PROFILE, ProfileConfig.class, new ProfileConfig());

    public void init() {
        switchProfile(profileConfig.currentConfig);
    }

    public void switchProfile(String name) {
        boolean setDefault = name == null || name.isEmpty();
        name = validateName(name);

        ClickCrystals.config.saveKeybinds();
        ClickCrystals.config.saveModules();
        ClickCrystals.config.saveHuds();
        ClickCrystals.config.save();

        Global.system.modules().values().forEach(m -> m.setEnabled(false, false));
        File file = new File(name);
        Config config = JsonSerializable.load(file, Config.class, new Config(name));
        config.loadEntireConfig();
        profileConfig.currentConfig = setDefault ? null : file.getName().replace(".json", "");
        profileConfig.save();

        ClickCrystals.config = config;
        ClickCrystals.config.saveKeybinds();
        ClickCrystals.config.saveModules();
        ClickCrystals.config.saveHuds();
        ClickCrystals.config.save();
    }

    public void switchDefaultProfile() {
        switchProfile(null);
    }

    public void deleteProfile(String name) {
        name = validateName(name);

        if (name.equals(Config.PATH_CONFIG)) {
            return;
        }

        switchDefaultProfile();

        File file = new File(name);
        if (file.exists()) {
            file.delete();
        }
    }

    public boolean hasProfile(String name) {
        for (String p : getCustomProfiles()) {
            if (p.equals(name)) {
                return true;
            }
        }
        return false;
    }

    private String validateName(String name) {
        return name == null || name.isEmpty() ? Config.PATH_CONFIG : Config.PATH_PROFILES + name.toLowerCase().replaceAll("[^a-z_-]", "") + ".json";
    }

    public String[] getCustomProfiles() {
        File folder = new File(Config.PATH_PROFILES);
        File[] files = folder.listFiles(f -> f.getName().matches("^([a-z_-]*)(\\.json)$"));

        if (files == null) {
            return new String[]{};
        }
        return Arrays.stream(files).map(f -> f.getName().replace(".json", "")).toArray(String[]::new);
    }

    public static void renameOldFolder() {
        File oldFolder = new File("ClickCrystalsClient/");
        File folder = new File(Config.PATH);
        if (oldFolder.exists()) {
            oldFolder.renameTo(folder);
        }
    }

    public static class ProfileConfig implements JsonSerializable<ProfileConfig> {
        private String currentConfig;

        @Override
        public File getFile() {
            return new File(Config.PATH_CONFIG_PROFILE);
        }

        public String getCurrentProfileName() {
            return currentConfig == null || currentConfig.isEmpty() ? "Main Config" : StringUtils.capitalizeWords(currentConfig);
        }

        public String getCurrentProfileId() {
            return currentConfig == null || currentConfig.isEmpty() ? "main-config" : currentConfig;
        }
    }
}
