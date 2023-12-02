package io.github.itzispyder.clickcrystals.client.system;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.util.FileValidationUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClickCrystalsLogger {

    private final File log;
    private final boolean hasWriter;

    public ClickCrystalsLogger(File log) {
        this.log = log;
        log.delete();
        this.hasWriter = FileValidationUtils.validate(log);

        if (hasWriter) {
            clearLog();
        }
        else {
            System.err.println(ClickCrystals.prefix + "Log writer has failed to initialize!");
        }
    }

    public void info(String line) {
        log("CC/INFO", line);
    }

    public void error(String line) {
        log("CC/ERROR", line);
    }

    public void log(String prefix, String line) {
        if (!hasWriter) {
            return;
        }

        try (FileWriter writer = new FileWriter(log, true)) {
            line = "[%s] [%s] [%s]: %s%n".formatted(StringUtils.getCurrentTimeStamp(), Thread.currentThread().getName(), prefix, line);
            writer.append(line);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void clearLog() {
        if (!hasWriter) {
            return;
        }

        try (FileWriter writer = new FileWriter(log)) {
            writer.write("");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public File getLog() {
        return log;
    }
}
