package io.github.itzispyder.clickcrystals.client.system;

import io.github.itzispyder.clickcrystals.Global;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Version implements Global {

    private static final Pattern PATTERN =
            Pattern.compile("(?<vmajor>\\d+)\\.(?<vminor>\\d+)(\\.(?<vpatch>\\d+))?");

    private final int vmajor, vminor, vpatch;

    public Version(int vmajor, int vminor, int vpatch) {
        this.vmajor = vmajor;
        this.vminor = vminor;
        this.vpatch = vpatch;
    }

    public Version(int vmajor, int vminor) {
        this(vmajor, vminor, 0);
    }

    public static Version ofString(String versionString) {
        Matcher matcher = PATTERN.matcher(versionString);
        if (!matcher.matches())
            return null;

        String major = matcher.group("vmajor");
        String minor = matcher.group("vminor");
        String patch = matcher.group("vpatch");

        if (patch == null)
            return new Version(
                    Integer.parseInt(major),
                    Integer.parseInt(minor)
            );

        return new Version(
                Integer.parseInt(major),
                Integer.parseInt(minor),
                Integer.parseInt(patch)
        );
    }

    public boolean isNewerThan(Version other) {
        return vComp(other, (us, them) -> us > them);
    }

    public boolean isNewerOrEqualTo(Version other) {
        return vComp(other, (us, them) -> us >= them);
    }

    public boolean isSameAs(Version other) {
        return vComp(other, (us, them) -> us == them);
    }

    public boolean isOlderThan(Version other) {
        return vComp(other, (us, them) -> us < them);
    }

    public boolean isOlderOrEqualTo(Version other) {
        return vComp(other, (us, them) -> us <= them);
    }

    private boolean vComp(Version other, VComp comp) {
        if (this.vmajor != other.vmajor)
            return comp.comp(this.vmajor, other.vmajor);
        if (this.vminor != other.vminor)
            return comp.comp(this.vminor, other.vminor);
        return comp.comp(this.vpatch, other.vpatch);
    }

    public String getVersionString() {
        return toString();
    }

    @Override
    public String toString() {
        return "%s.%s.%s".formatted(vmajor, vminor, vpatch);
    }

    @FunctionalInterface
    public interface VComp {
        boolean comp(int us, int them);
    }

    public static String fetchModVersion() {
        ModContainer mod = FabricLoader.getInstance().getModContainer(modId)
                .orElseThrow(() -> new IllegalArgumentException("ClickCrystals has not been loaded"));
        String ver = mod.getMetadata().getVersion().getFriendlyString();
        return ver.replaceFirst("((\\d\\.?)+-)+", "");
    }
}