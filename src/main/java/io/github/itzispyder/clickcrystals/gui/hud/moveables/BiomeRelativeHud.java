package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.TextHud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Optional;

public class BiomeRelativeHud extends TextHud {

    public BiomeRelativeHud() {
        super("biome-hud", 10, 135, 120, 12);
    }

    @Override
    public String getText() {
        String name = "UNKNOWN";

        if (PlayerUtils.playerNotNull()) {
            ClientPlayerEntity p = PlayerUtils.player();
            BlockPos pos = p.getBlockPos();
            World w = p.getWorld();
            Optional<RegistryKey<Biome>> bi = w.getBiome(pos).getKey();

            if (bi.isPresent()) {
                String[] secs = bi.get().getValue().toTranslationKey().split("\\.");
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
