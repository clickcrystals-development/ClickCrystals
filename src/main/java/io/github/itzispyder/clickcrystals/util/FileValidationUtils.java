package io.github.itzispyder.clickcrystals.util;

import java.io.*;

public final class FileValidationUtils {

    public static boolean validate(File file) {
        try {
            if (!file.getParentFile().exists())
                if (!file.getParentFile().mkdirs())
                    return false;
            if (!file.exists())
                if (!file.createNewFile())
                    return false;
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public static boolean quickWrite(File file, String string) {
        if (file == null || !validate(file)) {
            return false;
        }

        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(string);
            bw.flush();
            bw.close();
            fw.close();
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public static String quickRead(File file) {
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String read = String.join(" ", br.lines().toArray(String[]::new));
            br.close();
            fr.close();
            return read;
        }
        catch (Exception ex) {
            return "";
        }
    }
}
