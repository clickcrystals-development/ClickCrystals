package io.github.itzispyder.clickcrystals.gui.hud.positionable;

import io.github.itzispyder.clickcrystals.gui.hud.TextHud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import java.util.Optional;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class BiomePositionableHud extends TextHud {

    public BiomePositionableHud() {
        super("biome-hud", 10, 135, 120, 16);
    }

    @Override
    public String getText() {
        String name = "UNKNOWN";

        if (PlayerUtils.valid()) {
            LocalPlayer p = PlayerUtils.player();
            BlockPos pos = p.blockPosition();
            Level w = p.level();
            Optional<ResourceKey<Biome>> bi = w.getBiome(pos).unwrapKey();

            if (bi.isPresent()) {
                String[] secs = bi.get().identifier().toLanguageKey().split("\\.");
                name = StringUtils.capitalizeWords(secs[secs.length - 1]);
            }
        }
        return "Biome: " + name;
    }

    @Override
    public boolean canRender() {
        return super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudBiome.getVal());
    }
}
