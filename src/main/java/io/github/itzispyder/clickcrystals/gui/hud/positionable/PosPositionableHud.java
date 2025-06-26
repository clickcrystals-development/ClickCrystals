package io.github.itzispyder.clickcrystals.gui.hud.positionable;

import io.github.itzispyder.clickcrystals.gui.hud.TextHud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.misc.Dimensions;
import net.minecraft.util.math.BlockPos;

public class PosPositionableHud extends TextHud {

    public PosPositionableHud() {
        super("pos-hud", 10, 120, 120, 16);
    }

    @Override
    public String getText() {
        BlockPos p = PlayerUtils.player().getBlockPos();
        if (Module.get(InGameHuds.class).showNetherCoords.getVal() && !Dimensions.isNether() && Dimensions.isOverworld())
            return "%s /§c %s, %s".formatted(p.toShortString(), p.getX() / 8, p.getZ() / 8);
        return p.toShortString();
    }

    @Override
    public boolean canRender() {
        return super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudPos.getVal());
    }
}
