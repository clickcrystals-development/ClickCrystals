import io.github.itzispyder.clickcrystals.client.system.VersionStrings;

/**
 * Regression for a startup crash caused by stripping the entire
 * "minecraft-version-mod-version-" prefix from prerelease JARs.
 */
public final class VersionStringsSmokeTest {
    private static void expect(String actual, String wanted) {
        if (!wanted.equals(actual))
            throw new AssertionError("Expected [" + wanted + "], got [" + actual + "]");
    }

    private static void rejects(String version) {
        try {
            VersionStrings.numericCore(version);
        }
        catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError("Expected a rejected invalid version: " + version);
    }

    public static void main(String[] args) {
        expect(VersionStrings.stripMinecraftPrefix("1.21.11-1.4.4"), "1.4.4");
        expect(VersionStrings.stripMinecraftPrefix("1.21.11-1.4.5-dev.1"), "1.4.5-dev.1");
        expect(VersionStrings.stripMinecraftPrefix("1.21.11-1.4.5-dev.2"), "1.4.5-dev.2");
        expect(VersionStrings.stripMinecraftPrefix("26.1.2-1.4.5-dev.2"), "1.4.5-dev.2");
        expect(VersionStrings.stripMinecraftPrefix("26.2-1.4.5-dev.2"), "1.4.5-dev.2");
        expect(VersionStrings.stripMinecraftPrefix("1.4.5-dev.2"), "1.4.5-dev.2");

        expect(VersionStrings.numericCore("1.21.11-1.4.5-dev.1"), "1.4.5");
        expect(VersionStrings.numericCore("26.1.2-1.4.5-dev.2"), "1.4.5");
        expect(VersionStrings.numericCore("26.2-1.4.5-dev.2"), "1.4.5");
        expect(VersionStrings.numericCore("1.4.5-dev.2"), "1.4.5");
        expect(VersionStrings.numericCore("1.4.4"), "1.4.4");
        expect(VersionStrings.numericCore("1.4.5+build.8"), "1.4.5");

        rejects("dev.1");
        rejects("");
        rejects("broken");
        System.out.println("VersionStringsSmokeTest: PASS");
    }
}
