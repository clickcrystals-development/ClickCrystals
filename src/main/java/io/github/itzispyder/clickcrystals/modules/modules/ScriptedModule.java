package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.util.misc.Timer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScriptedModule extends ListenerModule {

    public static final String PATH = "ClickCrystalsClient/scripts";
    public final List<ClickListener> clickListeners = new ArrayList<>();

    public ScriptedModule(String name, String description) {
        super(name, Categories.SCRIPTED, description);
    }

    @EventHandler
    public void onMouseClick(MouseClickEvent e) {
        if (e.isScreenNull()) {
            for (ClickListener l : clickListeners) {
                l.pass(e);
            }
        }
    }

    @FunctionalInterface
    public interface ClickListener {
        void pass(MouseClickEvent e);
    }

    public static void runModuleScripts() {
        File parentFolder = new File(PATH);
        if (!parentFolder.exists() || !parentFolder.isDirectory()) {
            parentFolder.mkdirs();
            return;
        }

        File[] files = parentFolder.listFiles(f -> f.isFile() && f.getPath().endsWith(".ccs"));
        if (files == null || files.length == 0) {
            return;
        }

        Timer timer = Timer.start();
        int total = files.length;

        system.printf("-> executing scripts ({x})...", total);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            new ClickScript(file).execute();
            system.printf("<- [{x}/{x}] '{x}'", i + 1, total, file.getName());
        }
        system.printf("<- [done] executed ({x}) scripts in {x}", total, timer.end().getStampPrecise());
    }
}
