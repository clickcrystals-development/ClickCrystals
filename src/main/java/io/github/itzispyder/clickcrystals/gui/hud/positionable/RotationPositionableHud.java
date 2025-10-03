package io.github.itzispyder.clickcrystals.gui.hud.positionable;

import io.github.itzispyder.clickcrystals.gui.hud.TextHud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.network.ClientPlayerEntity;

public class RotationPositionableHud extends TextHud {

    public RotationPositionableHud() {
        super("rotation-hud", 10, 180, 120, 16);
    }

    @Override
    public String getText() {
        if (PlayerUtils.invalid())
            return "";

        ClientPlayerEntity p = PlayerUtils.player();
        int pitch = (int)p.getPitch();
        int yaw = (int)p.getYaw();

        return "Pitch: %s, Yaw: %s".formatted(
                MathUtils.wrapDegrees(pitch),
                MathUtils.wrapDegrees(yaw)
        );
    }

    public boolean canRender() {
        return super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudRotation.getVal());
    }
}
