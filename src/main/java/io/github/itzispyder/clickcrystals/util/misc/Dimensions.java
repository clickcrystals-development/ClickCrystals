package io.github.itzispyder.clickcrystals.util.misc;

import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

public enum Dimensions {
    OVERWORLD,
    THE_END,
    THE_NETHER;

    public static boolean isOverworld() {
        return check(DimensionTypes.OVERWORLD, DimensionTypes.OVERWORLD_CAVES);
    }

    public static boolean isNether() {
        return check(DimensionTypes.THE_NETHER);
    }

    public static boolean isEnd() {
        return check(DimensionTypes.THE_END);
    }

    @SafeVarargs
    private static boolean check(RegistryKey<DimensionType>... dimKeys) {
        if (PlayerUtils.invalid()) {
            return false;
        }
        var entry = PlayerUtils.getWorld().getDimensionEntry().getKey();
        if (entry.isEmpty())
            return false;

        var target = entry.get();
        for (RegistryKey<DimensionType> key : dimKeys) {
            if (key == target) {
                return true;
            }
        }
        return false;
    }
}