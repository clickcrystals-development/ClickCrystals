package io.github.itzispyder.clickcrystals.gui.hud.moveables;

import io.github.itzispyder.clickcrystals.gui.hud.TextHud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.misc.CameraRotator;
import net.minecraft.client.network.ClientPlayerEntity;

public class RotationRelativeHud extends TextHud {

    public RotationRelativeHud() {
        super("rotation-hud", 10, 180, 120, 16);
    }

    @Override
    public String getText() {
        if (PlayerUtils.invalid())
            return "";

        ClientPlayerEntity p = PlayerUtils.player();
        int pitch = (int)p.getPitch();
        int yaw = (int)p.getYaw();
        CameraRotator.Goal g = new CameraRotator.Goal(pitch, yaw);

        return "Pitch: %s, Yaw: %s".formatted(g.getPitch(), g.getYaw());
    }

    public boolean canRender() {
        return super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudRotation.getVal());
    }
}
