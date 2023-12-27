package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;

public class ConfigCmd extends ScriptCommand implements Global {

    public ConfigCmd() {
        super("config");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        switch (args.get(0).toEnum(Type.class, null)) {
            case SAVE -> saveEntireConfig();
            case RELOAD -> {
                saveEntireConfig();
                ClickCrystals.config.loadEntireConfig();
            }
        }

        if (args.match(1, "then")) {
            args.executeAll(2);
        }
    }

    public static void saveEntireConfig() {
        system.println("-> saving data");
        ClickCrystals.config.saveModules();
        ClickCrystals.config.saveKeybinds();
        ClickCrystals.config.saveHuds();
        system.println("-> saving config");
        ClickCrystals.config.save();
    }

    public enum Type {
        SAVE,
        RELOAD
    }
}
