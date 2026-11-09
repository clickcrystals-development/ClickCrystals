package io.github.itzispyder.clickcrystals.client.system;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Fabric metadata contains "<minecraft-version>-<mod-version>".
 * Remove ONLY the Minecraft prefix: development suffixes (e.g. -dev.2)
 * belong to the actual mod version and must not be discarded.
 */
public final class VersionStrings {
    private static final Pattern GAME_PREFIX =
            Pattern.compile("^\\d+(?:\\.\\d+)*-(?=\\d)");
    private static final Pattern MOD_VERSION =
            Pattern.compile("^(\\d+(?:\\.\\d+)*)(?:[-+][0-9A-Za-z][0-9A-Za-z.+-]*)?$");

    private VersionStrings() { }

    public static String stripMinecraftPrefix(String fullVersion) {
        if (fullVersion == null || fullVersion.isBlank())
            throw new IllegalArgumentException("Mod version is missing");
        return GAME_PREFIX.matcher(fullVersion.trim()).replaceFirst("");
    }

    /** Parses the numeric core of release and prerelease versions. */
    public static String numericCore(String version) {
        String normalized = stripMinecraftPrefix(version);
        Matcher matcher = MOD_VERSION.matcher(normalized);
        if (!matcher.matches())
            throw new IllegalArgumentException("Invalid mod version: " + version);
        return matcher.group(1);
    }
}
