package io.github.itzispyder.clickcrystals.gui_beta.hud.moveables;

import io.github.itzispyder.clickcrystals.gui_beta.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.clickcrystals.InGameHuds;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import io.github.itzispyder.clickcrystals.util.misc.CameraRotator;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;

public class RotationRelativeHud extends Hud {

    public RotationRelativeHud() {
        super("rotation-hud", 10, 180, 120, 12);
    }

    @Override
    public void render(DrawContext context) {
        renderBackdrop(context);

        ClientPlayerEntity p = PlayerUtils.player();
        int pitch = (int)p.getPitch();
        int yaw = (int)p.getYaw();
        CameraRotator.Goal g = new CameraRotator.Goal(pitch, yaw);

        String text = "Pitch: %s, Yaw: %s".formatted(g.getPitch(), g.getYaw());
        setWidth(mc.textRenderer.getWidth(text) + 6);

        int x = getX() + 3;
        int y = getY() + 2;
        RenderUtils.drawText(context, text, x, y, 1.0F, true);
    }

    public boolean canRender() {
        return super.canRender() && Module.getFrom(InGameHuds.class, m -> m.hudRotation.getVal());
    }

    @Override
    public void revertDimensions() {
        setX(getDefaultDimension().x);
        setY(getDefaultDimension().y);
    }

    @Override
    public int getArgb() {
        return Module.getFrom(InGameHuds.class, m -> m.getArgb());
    }

    @Override
    public boolean canRenderBorder() {
        return Module.getFrom(InGameHuds.class, m -> m.renderHudBorders.getVal());
    }
}
