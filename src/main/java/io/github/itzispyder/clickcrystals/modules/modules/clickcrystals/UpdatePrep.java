package io.github.itzispyder.clickcrystals.modules.modules.clickcrystals;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;

import java.io.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class UpdatePrep extends Module {

    public static final Predicate<String> CC_FILTER = line -> {
        String s = StringUtils.decolor(line.toLowerCase()).replaceAll("[^0-9a-z]", "");
        return !s.contains("clickcrystal");
    };

    public UpdatePrep() {
        super("update-prep", Categories.CLICKCRYSTALS, "Deletes CC jar file, CC config files, and clears all CC from your latest.log to prepare for CC update!");
    }

    @Override
    protected void onEnable() {
        setEnabled(false, false);

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            clearScreen();
            clearJar();
            clearConfig();
            clearLogs();
        });

        future.thenRun(() -> {
            ChatUtils.sendChatMessage("e");
        });
    }

    @Override
    protected void onDisable() {

    }

    public static void clearScreen() {
        if (mc.currentScreen != null) {
            mc.currentScreen.close();
        }
        if (mc.inGameHud != null && mc.inGameHud.getChatHud() != null) {
            mc.inGameHud.getChatHud().getMessageHistory().removeIf(s -> !CC_FILTER.test(s));
        }
    }

    public static void clearJar() {
        File modsFolder = new File("mods");
        if (!modsFolder.isDirectory()) return;

        File[] mods = modsFolder.listFiles();
        if (mods == null) return;

        for (File mod : mods) {
            if (mod.getName().toLowerCase().contains("clickcrystals")) {
                mod.delete();
            }
        }
    }

    public static void clearConfig() {
        new File("ClickCrystalsClient").delete();
    }

    public static void clearLogs() {
        File file = new File("logs/latest.log");

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            List<String> lines = br.lines().filter(CC_FILTER).toList();
            br.close();

            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.flush();
            for (String line : lines) {
                bw.append(line);
                bw.newLine();
            }
            bw.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
