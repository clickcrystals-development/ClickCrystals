package io.github.itzispyder.clickcrystals.util.misc;

import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;

public enum Dimensions {
    OVERWORLD,
    THE_END,
    THE_NETHER;

    public static boolean isOverworld() {
        return check(BuiltinDimensionTypes.OVERWORLD, BuiltinDimensionTypes.OVERWORLD_CAVES);
    }

    public static boolean isNether() {
        return check(BuiltinDimensionTypes.NETHER);
    }

    public static boolean isEnd() {
        return check(BuiltinDimensionTypes.END);
    }

    @SafeVarargs
    private static boolean check(ResourceKey<DimensionType>... dimKeys) {
        if (PlayerUtils.invalid()) {
            return false;
        }
        var entry = PlayerUtils.getWorld().dimensionTypeRegistration().unwrapKey();
        if (entry.isEmpty())
            return false;

        var target = entry.get();
        for (ResourceKey<DimensionType> key : dimKeys) {
            if (key == target) {
                return true;
            }
        }
        return false;
    }
}