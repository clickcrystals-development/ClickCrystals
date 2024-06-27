package io.github.itzispyder.clickcrystals.client.system;

import io.github.itzispyder.clickcrystals.Global;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

public class Version implements Global {

    private final int[] digits;

    public Version(String versionString) {
        String[] i = versionString.replaceAll("[^0-9.]", "").split("\\.");
        this.digits = new int[i.length];
        for (int j = 0; j < i.length; j++) {
            digits[j] = Integer.parseInt(i[j]);
        }
    }

    public static Version ofString(String s) {
        return new Version(s);
    }

    public static Version ofInt(int... i) {
        StringBuilder b = new StringBuilder();
        for (int x : i) {
            b.append(x).append(".");
        }
        return ofString(b.substring(0, b.length() - 1));
    }

    public static Version ofAnother(Version v) {
        return new Version(v.getVersionString());
    }

    public String getVersionString() {
        StringBuilder b = new StringBuilder();
        for (int d : digits) {
            b.append(d).append(".");
        }
        return b.substring(0, b.length() - 1);
    }

    public int[] getDigits() {
        return digits;
    }

    public boolean isNewerThan(Version another) {
        int[] cur = getDigits();
        int[] oth = another.getDigits();

        for (int i = 0; i < cur.length; i++) {
            if (cur[i] > oth[i]) {
                return true;
            }
        }
        return false;
    }

    public boolean isSameAs(Version another) {
        int[] cur = getDigits();
        int[] oth = another.getDigits();

        for (int i = 0; i < cur.length; i++) {
            if (cur[i] != oth[i]) {
                return false;
            }
        }

        return true;
    }

    public boolean isOlderThan(Version another) {
        return !isNewerThan(another) && !isSameAs(another);
    }

    public boolean isUpToDate(Version latest) {
        return isSameAs(latest) || isNewerThan(latest);
    }

    @Override
    public String toString() {
        return getVersionString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Version v)) {
            return false;
        }
        return isSameAs(v);
    }

    public static String getModVersion() {
        ModContainer mod = FabricLoader.getInstance().getModContainer(modId)
                .orElseThrow(() -> new IllegalArgumentException("ClickCrystals has not been loaded"));
        String ver = mod.getMetadata().getVersion().getFriendlyString();
        return ver.replaceFirst("((\\d\\.?)+-)+", "");
    }
}