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
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

public class EntityIndicatorHud extends Hud {

    public EntityIndicatorHud() {
        super("entity-indicator-hud");
        this.setFixed(true);
    }

    @Override
    public void render(GuiGraphicsExtractor context, float tickDelta) {
        EntityIndicator m = Module.get(EntityIndicator.class);
        if (!m.isEnabled() || PlayerUtils.invalid())
            return;
        if (m.updatePerRender.getVal())
            m.update();

        int cx = context.guiWidth() / 2;
        int cy = context.guiHeight() / 2;
        int radius = m.hudSize.getVal();

        EntityIndicatorSimulation sim = m.getSimulation();
        Voidable<SimulationEntry> nearest = sim.getNearestEntity();

        if (m.isRendering2D())
            this.render2D(context, m, cx, cy, radius, nearest); // render 2d
        else
            this.render3D(context, m, cx, cy, radius, tickDelta); // render 3d
    }

    private void render3D(GuiGraphicsExtractor context, EntityIndicator module, int cx, int cy, int radius, float tickDelta) {
        EntityIndicatorSimulationRenderer renderer = module.getSimulation().getRenderer();
        LocalPlayer client = PlayerUtils.player();

        float pitch = (float) Math.toRadians(client.getViewXRot(tickDelta));
        float yaw = (float) Math.toRadians(client.getViewYRot(tickDelta));

        Quaternionf qPitch = new Quaternionf().rotationX(-pitch);
        Quaternionf qYaw = new Quaternionf().rotationY(yaw);
        Quaternionf rotation = qPitch.mul(qYaw);

        renderer.render(context, cx, cy, radius, rotation, 0x06FFFFFF);
        renderer.setFocalLen(100);
    }

    private void render2D(GuiGraphicsExtractor context, EntityIndicator module, int cx, int cy, int radius, Voidable<SimulationEntry> nearest) {
        int size = module.spriteSize.getVal();

        nearest.accept(display -> {
            context.pose().pushMatrix();
            context.pose().rotateAbout((float)Math.toRadians(display.getYawDifference()), cx, cy);
            RenderUtils.drawTexture(context, Tex.Overlays.DIRECTION, cx - radius, cy - radius, radius * 2, radius * 2);
            context.pose().popMatrix();
        });

        for (SimulationEntry display : module.getSimulation().getEntities()) {
            float θ = display.getYawDifference(); // TheTrouper gave me this Unicode LOL (the real theta)
            int x = (int)(cx + Mth.cos(Math.toRadians(θ - 90)) * radius);
            int y = (int)(cy + Mth.sin(Math.toRadians(θ - 90)) * radius);
            MobHeadBrush.drawHead(context, display.getEntityType(), x - size / 2, y - size / 2, size);
        }

        nearest.accept(display -> {
            float θ = display.getYawDifference(); // TheTrouper gave me this Unicode LOL (the real theta)
            int x = (int)(cx + Mth.cos(Math.toRadians(θ - 90)) * radius);
            int y = (int)(cy + Mth.sin(Math.toRadians(θ - 90)) * radius);
            int bigger = size + 2;

            RenderUtils.fillRect(context, x - bigger / 2 - 1, y - bigger / 2 - 1, bigger + 2, bigger + 2, 0xFFFFFFFF);
            MobHeadBrush.drawHead(context, display.getEntityType(), x - bigger / 2, y - bigger / 2, bigger);
        });
    }
}