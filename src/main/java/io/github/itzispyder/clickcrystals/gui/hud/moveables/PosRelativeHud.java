package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.TextHud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.misc.Dimensions;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

public class PosRelativeHud extends TextHud {

    public PosRelativeHud() {
        super("pos-hud", 10, 120, 120, 16);
    }

    @Override
    public String getText() {
        if (Module.get(InGameHuds.class).showNetherCoords.getVal() && !Dimensions.isNether() && Dimensions.isOverworld()) {
            BlockPos blockPos = PlayerUtils.player().getBlockPos();
            int x = (blockPos.getX() / 8);
            int z = (blockPos.getZ() / 8);
    return blockPos.toShortString() + " / " + Formatting.RED +  x + ", " + blockPos.getY() + ", " + z;
        }
        return PlayerUtils.player().getBlockPos().toShortString();
    }

    @Override
    public boolean canRender() {
        return super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudPos.getVal());
    }
}
