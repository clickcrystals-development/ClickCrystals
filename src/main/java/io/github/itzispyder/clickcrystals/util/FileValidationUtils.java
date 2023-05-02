package io.github.itzispyder.clickcrystals.util;

import java.io.File;

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
}
