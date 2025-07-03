package io.github.itzispyder.clickcrystals.modules.modules.rendering.entityindicators;

import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animations;
import io.github.itzispyder.clickcrystals.gui.misc.animators.Animator;
import io.github.itzispyder.clickcrystals.gui.misc.animators.PollingAnimator;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderConstants;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.joml.*;

import java.lang.Math;

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

    public void render(Matrix3x2fStack matrices, int x, int y, int hudSize, Quaternionf rotation, int color) {
        float radius = (int)(hudSize * transitionAnimation.getAnimation());
        if (radius <= 0)
            return;

        BufferBuilder buf = RenderUtils.getBuffer(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Matrix3x2f mat = matrices.pushMatrix();

        for (int pitch = 0; pitch < 360; pitch += deltaTheta) {
            for (int yaw = 0; yaw < 180; yaw += deltaTheta) {
                Vector3d vec1 = this.polar2vector(pitch, yaw, radius);
                Vector3d vec2 = this.polar2vector(pitch + deltaTheta, yaw, radius);
                Vector3d vec3 = this.polar2vector(pitch + deltaTheta, yaw + deltaTheta, radius);
                Vector3d vec4 = this.polar2vector(pitch, yaw + deltaTheta, radius);

                float[] vtx1 = this.projectVertex(vec1, rotation);
                float[] vtx2 = this.projectVertex(vec2, rotation);
                float[] vtx3 = this.projectVertex(vec3, rotation);
                float[] vtx4 = this.projectVertex(vec4, rotation);

                buf.vertex(mat, x + vtx1[0], y + vtx1[1], 0).color(color);
                buf.vertex(mat, x + vtx2[0], y + vtx2[1], 0).color(color);
                buf.vertex(mat, x + vtx3[0], y + vtx3[1], 0).color(color);
                buf.vertex(mat, x + vtx4[0], y + vtx4[1], 0).color(color);
            }
        }

        RenderUtils.drawBuffer(buf, RenderConstants.QUADS);
		matrices.popMatrix();

        buf = RenderUtils.getBuffer(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);
        mat = matrices.pushMatrix();
        color = (0x20000000) | (0x00FFFFFF & color);

        for (int pitch = 0; pitch < 360; pitch += deltaTheta) {
            for (int yaw = 0; yaw < 180; yaw += deltaTheta) {
                Vector3d vec1 = this.polar2vector(pitch, yaw, radius);
                Vector3d vec2 = this.polar2vector(pitch, yaw + deltaTheta, radius);

                float[] vtx1 = this.projectVertex(vec1, rotation);
                float[] vtx2 = this.projectVertex(vec2, rotation);

                buf.vertex(mat, x + vtx1[0], y + vtx1[1], 0).color(color);
                buf.vertex(mat, x + vtx2[0], y + vtx2[1], 0).color(color);
            }
        }
        for (int yaw = 0; yaw < 180; yaw += deltaTheta) {
            for (int pitch = 0; pitch < 360; pitch += deltaTheta) {
                Vector3d vec1 = this.polar2vector(pitch, yaw, radius);
                Vector3d vec2 = this.polar2vector(pitch + deltaTheta, yaw, radius);

                float[] vtx1 = this.projectVertex(vec1, rotation);
                float[] vtx2 = this.projectVertex(vec2, rotation);

                buf.vertex(mat, x + vtx1[0], y + vtx1[1], 0).color(color);
                buf.vertex(mat, x + vtx2[0], y + vtx2[1], 0).color(color);
            }
        }

        RenderUtils.drawBuffer(buf, RenderConstants.LINES);
		matrices.popMatrix();

        this.renderEntities(matrices, x, y, radius, rotation);
    }

    private void renderEntities(Matrix3x2fStack matrices, int cx, int cy, float radius, Quaternionf rotation) {
        float spriteSize = 8.0F;
        for (SimulationEntry entity : simulation.getEntities()) {
            Vector3f vecDifference = entity.getVecDifference().multiply(radius).toVector3f();
            float[] vertex = this.projectVertex(vecDifference, rotation);

            entity.getTexture().accept(texture -> {
                drawSprite(matrices, texture, cx + vertex[0], cy + vertex[1], spriteSize, false);
            });
        }
        simulation.getNearestEntity().accept(entity -> {
            Vector3f vecDifference = entity.getVecDifference().multiply(radius).toVector3f();
            float[] vertex = this.projectVertex(vecDifference, rotation);

            entity.getTexture().accept(texture -> {
                drawLine(matrices, cx, cy, cx + vertex[0], cy + vertex[1], 0xFFFFFFFF);
                drawSprite(matrices, texture, cx + vertex[0], cy + vertex[1], spriteSize + 2, true);
            });
        });
    }

    private void drawLine(Matrix3x2fStack matrices, float x1, float y1, float x2, float y2, int color) {
        BufferBuilder buf = RenderUtils.getBuffer(VertexFormat.DrawMode.LINES, VertexFormats.POSITION_COLOR);
        buf.vertex(matrices, x1, y1, 0).color(color);
        buf.vertex(matrices, x2, y2, 0).color(color);
    }

    private void drawSprite(Matrix3x2fStack matrices, Identifier texture, float cx, float cy, float size, boolean highlight) {
        BufferBuilder buf;
        Matrix3x2f mat;

        float x = cx - size / 2;
        float y = cy - size / 2;

        if (highlight) {
            buf = RenderUtils.getBuffer(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            mat = matrices.pushMatrix();

            x--;
            y--;
            size += 2;

            buf.vertex(mat, x, y, 0).color(-1);
            buf.vertex(mat, x, y + size, 0).color(-1);
            buf.vertex(mat, x + size, y + size, 0).color(-1);
            buf.vertex(mat, x + size, y, 0).color(-1);

            x++;
            y++;
            size -= 2;

            RenderUtils.drawBuffer(buf, RenderConstants.QUADS);
		    matrices.popMatrix();
        }

        buf = RenderUtils.getBuffer(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        mat = matrices.pushMatrix();

        buf.vertex(mat, x, y, 0).texture(0, 0).color(-1);
        buf.vertex(mat, x, y + size, 0).texture(0, 1).color(-1);
        buf.vertex(mat, x + size, y + size, 0).texture(1, 1).color(-1);
        buf.vertex(mat, x + size, y, 0).texture(1, 0).color(-1);

        RenderUtils.drawBuffer(buf, RenderConstants.TEX_QUADS.apply(texture));
		matrices.popMatrix();
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
