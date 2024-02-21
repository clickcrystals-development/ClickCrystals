package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.TextHud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

public class VersionRelativeHud extends TextHud {

    public VersionRelativeHud() {
        super("version-hud", 10, 195, 150, 12);
    }

    private String getModVersionNumber() {
        String versionNumber = "Unknown";

        ModContainer modContainer = FabricLoader.getInstance().getModContainer("clickcrystals").orElse(null);

        if (modContainer != null) {
            versionNumber = modContainer.getMetadata().getVersion().getFriendlyString();
        }

        return versionNumber;
    }

    @Override
    public String getText() {
        String modVersion = getModVersionNumber();
        return "ClickCrystals:" + (modVersion.equals("Unknown") ? "" : " " + modVersion);
    }

    @Override
    public boolean canRender() {
        return super.canRender() && Boolean.TRUE.equals(Module.getFrom(InGameHuds.class, m -> m.hudVersion.getVal()));
    }
}
