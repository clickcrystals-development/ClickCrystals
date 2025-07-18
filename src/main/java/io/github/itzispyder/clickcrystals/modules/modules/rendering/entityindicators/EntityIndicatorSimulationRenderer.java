package io.github.itzispyder.clickcrystals.modules.modules.rendering.entityindicators;

import io.github.itzispyder.clickcrystals.gui.misc.animators.Animations;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animator;
import io.github.itzispyder.clickcrystals.gui.misc.animators.PollingAnimator;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.states.SphereFillRenderState;
import io.github.itzispyder.clickcrystals.util.minecraft.render.states.SphereState;
import io.github.itzispyder.clickcrystals.util.minecraft.render.states.SphereWireframeRenderState;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class EntityIndicatorSimulationRenderer {

    private static final int deltaTheta = 15;

    private final EntityIndicatorSimulation simulation;
    private final Animator transitionAnimation;
    private int focalLen;

    public EntityIndicatorSimulationRenderer(EntityIndicatorSimulation simulation, int focalLen) {
        this.focalLen = focalLen;
        this.simulation = simulation;
        this.transitionAnimation = new PollingAnimator(300, this.simulation::notEmpty, Animations.FADE_IN_AND_OUT);
    }

    public void render(DrawContext context, int x, int y, int hudSize, Quaternionf rotation, int color) {
        float radius = (int)(hudSize * transitionAnimation.getAnimation());
        if (radius <= 0)
            return;

        SphereState sphere = new SphereState(context, rotation, x, y, radius, focalLen, deltaTheta, color, simulation);
        context.state.addSimpleElement(new SphereFillRenderState(sphere));
        sphere.color = 0x20000000 | (sphere.color & 0x00FFFFFF); // change color opacity to make lines stand out
        context.state.addSimpleElement(new SphereWireframeRenderState(sphere));

        this.renderEntities(context, x, y, radius, rotation);
    }

    private void renderEntities(DrawContext context, int cx, int cy, float radius, Quaternionf rotation) {
        int spriteSize = 8;
        for (SimulationEntry entity : simulation.getEntities()) {
            Vector3f vecDifference = entity.getVecDifference().multiply(radius).toVector3f();
            float[] vertex = MathUtils.projectVertex(vecDifference, rotation, focalLen);

            entity.getTexture().accept(texture -> {
                int x = (int) (cx + vertex[0]);
                int y = (int) (cy + vertex[1]);
                drawSprite(context, texture, x, y, spriteSize, false);
            });
        }
        simulation.getNearestEntity().accept(entity -> {
            Vector3f vecDifference = entity.getVecDifference().multiply(radius).toVector3f();
            float[] vertex = MathUtils.projectVertex(vecDifference, rotation, focalLen);

            entity.getTexture().accept(texture -> {
                int x = (int) (cx + vertex[0]);
                int y = (int) (cy + vertex[1]);
                drawSprite(context, texture, x, y, spriteSize + 2, true);
            });
        });
    }

    private void drawSprite(DrawContext context, Identifier texture, int cx, int cy, int size, boolean highlight) {
        int x = cx - size / 2;
        int y = cy - size / 2;

        if (highlight) {
            x--;
            y--;
            size += 2;

            context.fill(x, y, x + size, y + size, -1);

            x++;
            y++;
            size -= 2;
        }

        context.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, 0, 0, size, size, size, size);
    }

    private Vector3d polar2vector(float pitch, float yaw, float radius) {
        pitch = (float) Math.toRadians(pitch);
        yaw = (float) Math.toRadians(yaw);
        double x = radius * Math.cos(yaw) * Math.cos(pitch);
        double y = radius * Math.sin(pitch);
        double z = radius * Math.sin(yaw) * Math.cos(pitch);
        return new Vector3d(x, y, z);
    }

    private float[] projectVertex(double x, double y, double z) {
        double depth = -(focalLen + z);
        if (depth >= 0)
            depth = -0.00000000001;
        float pixelX = (float) ((focalLen * x) / depth);
        float pixelY = (float) ((focalLen * y) / depth);
        return new float[] { pixelX, pixelY };
    }

    private float[] projectVertex(Vector3d vector, Quaternionf rotation) {
        vector = rotation.transform(vector);
        return projectVertex(vector.x, vector.y, vector.z);
    }

    private float[] projectVertex(Vector3f vector, Quaternionf rotation) {
        vector = rotation.transform(vector);
        return projectVertex(vector.x, vector.y, vector.z);
    }

    public int getFocalLen() {
        return focalLen;
    }

    public void setFocalLen(int focalLen) {
        this.focalLen = focalLen;
    }

    public Animator getTransitionAnimation() {
        return transitionAnimation;
    }
}
