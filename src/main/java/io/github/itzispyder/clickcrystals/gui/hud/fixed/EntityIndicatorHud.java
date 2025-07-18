package io.github.itzispyder.clickcrystals.gui.hud.fixed;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.gui.misc.brushes.MobHeadBrush;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.EntityIndicator;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.entityindicators.EntityIndicatorSimulation;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.entityindicators.EntityIndicatorSimulationRenderer;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.entityindicators.SimulationEntry;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import io.github.itzispyder.clickcrystals.util.misc.Voidable;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import org.joml.Quaternionf;

public class EntityIndicatorHud extends Hud {

    public EntityIndicatorHud() {
        super("entity-indicator-hud");
        this.setFixed(true);
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        EntityIndicator m = Module.get(EntityIndicator.class);
        if (!m.isEnabled() || PlayerUtils.invalid())
            return;
        if (m.updatePerRender.getVal())
            m.update();

        int cx = context.getScaledWindowWidth() / 2;
        int cy = context.getScaledWindowHeight() / 2;
        int radius = m.hudSize.getVal();

        EntityIndicatorSimulation sim = m.getSimulation();
        Voidable<SimulationEntry> nearest = sim.getNearestEntity();

        if (m.isRendering2D())
            this.render2D(context, m, cx, cy, radius, nearest); // render 2d
        else
            this.render3D(context, m, cx, cy, radius, tickDelta); // render 3d
    }

    private void render3D(DrawContext context, EntityIndicator module, int cx, int cy, int radius, float tickDelta) {
        EntityIndicatorSimulationRenderer renderer = module.getSimulation().getRenderer();
        ClientPlayerEntity client = PlayerUtils.player();

        float pitch = (float) Math.toRadians(client.getPitch(tickDelta));
        float yaw = (float) Math.toRadians(client.getYaw(tickDelta));

        Quaternionf qPitch = new Quaternionf().rotationX(-pitch);
        Quaternionf qYaw = new Quaternionf().rotationY(yaw);
        Quaternionf rotation = qPitch.mul(qYaw);

        renderer.render(context, cx, cy, radius, rotation, 0x06FFFFFF);
        renderer.setFocalLen(100);
    }

    private void render2D(DrawContext context, EntityIndicator module, int cx, int cy, int radius, Voidable<SimulationEntry> nearest) {
        int size = module.spriteSize.getVal();

        nearest.accept(display -> {
            context.getMatrices().pushMatrix();
            context.getMatrices().rotateAbout((float)Math.toRadians(display.getYawDifference()), cx, cy);
            RenderUtils.drawTexture(context, Tex.Overlays.DIRECTION, cx - radius, cy - radius, radius * 2, radius * 2);
            context.getMatrices().popMatrix();
        });

        for (SimulationEntry display : module.getSimulation().getEntities()) {
            float θ = display.getYawDifference(); // TheTrouper gave me this Unicode LOL (the real theta)
            int x = (int)(cx + Math.cos(Math.toRadians(θ - 90)) * radius);
            int y = (int)(cy + Math.sin(Math.toRadians(θ - 90)) * radius);
            MobHeadBrush.drawHead(context, display.getEntityClass(), x - size / 2, y - size / 2, size);
        }

        nearest.accept(display -> {
            float θ = display.getYawDifference(); // TheTrouper gave me this Unicode LOL (the real theta)
            int x = (int)(cx + Math.cos(Math.toRadians(θ - 90)) * radius);
            int y = (int)(cy + Math.sin(Math.toRadians(θ - 90)) * radius);
            int bigger = size + 2;

            RenderUtils.fillRect(context, x - bigger / 2 - 1, y - bigger / 2 - 1, bigger + 2, bigger + 2, 0xFFFFFFFF);
            MobHeadBrush.drawHead(context, display.getEntityClass(), x - bigger / 2, y - bigger / 2, bigger);
        });
    }
}