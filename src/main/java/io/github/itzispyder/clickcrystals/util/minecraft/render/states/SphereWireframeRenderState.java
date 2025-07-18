package io.github.itzispyder.clickcrystals.util.minecraft.render.states;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.ClickCrystalsRenderPipelines;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.TextureSetup;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class SphereWireframeRenderState implements SimpleGuiElementRenderState {

    private final TextureSetup texture = TextureSetup.empty();
    private final SphereState ss;
    private final int color;

    public SphereWireframeRenderState(SphereState ss) {
        this.ss = ss;
        this.color = ss.color;
    }

    @Override
    public void setupVertices(VertexConsumer buf, float depth) {
        for (int pitch = 0; pitch < 360; pitch += ss.deltaTheta) {
            for (int yaw = 0; yaw < 180; yaw += ss.deltaTheta) {
                Vector3d vec1 = MathUtils.toVector(pitch, yaw, ss.radius);
                Vector3d vec2 = MathUtils.toVector(pitch, yaw + ss.deltaTheta, ss.radius);

                float[] vtx1 = MathUtils.projectVertex(vec1, ss.rotation, ss.focalLen);
                float[] vtx2 = MathUtils.projectVertex(vec2, ss.rotation, ss.focalLen);

                line(buf, ss.x + vtx1[0], ss.y + vtx1[1], ss.x + vtx2[0], ss.y + vtx2[1], color);
            }
        }
        for (int yaw = 0; yaw < 180; yaw += ss.deltaTheta) {
            for (int pitch = 0; pitch < 360; pitch += ss.deltaTheta) {
                Vector3d vec1 = MathUtils.toVector(pitch, yaw, ss.radius);
                Vector3d vec2 = MathUtils.toVector(pitch + ss.deltaTheta, yaw, ss.radius);

                float[] vtx1 = MathUtils.projectVertex(vec1, ss.rotation, ss.focalLen);
                float[] vtx2 = MathUtils.projectVertex(vec2, ss.rotation, ss.focalLen);

                line(buf, ss.x + vtx1[0], ss.y + vtx1[1], ss.x + vtx2[0], ss.y + vtx2[1], color);
            }
        }

        ss.simulation.getNearestEntity().accept(entity -> {
            Vector3f vecDifference = entity.getVecDifference().multiply(ss.radius).toVector3f();
            float[] vertex = MathUtils.projectVertex(vecDifference, ss.rotation, ss.focalLen);

            entity.getTexture().accept(texture -> {
                int x = (int) (ss.x + vertex[0]);
                int y = (int) (ss.y + vertex[1]);
                line(buf, ss.x, ss.y, x, y, 0xFFFFFFFF);
            });
        });
    }

    private void line(VertexConsumer buf, float x1, float y1, float x2, float y2, int color) {
        ss.pose.pushMatrix();

        float dx = x2 - x1;
        float dy = y2 - y1;
        float angle = (float) Math.atan2(dy, dx);
        float thickness = 0.5F;
        float t = thickness / 2;
        float length = (float) Math.sqrt(dx * dx + dy * dy);

        ss.pose.rotateAbout(angle, x1, y1);
        buf.vertex(ss.pose, x1 - t, y1 - t, 0).color(color);
        buf.vertex(ss.pose, x1 + length + t, y1 - t, 0).color(color);
        buf.vertex(ss.pose, x1 + length + t, y1 + t, 0).color(color);
        buf.vertex(ss.pose, x1 - t, y1 + t, 0).color(color);

        ss.pose.popMatrix();
    }

    @Override
    public RenderPipeline pipeline() {
        return ClickCrystalsRenderPipelines.PIPELINE_QUADS;
    }

    @Override
    public TextureSetup textureSetup() {
        return texture;
    }

    @Nullable
    @Override
    public ScreenRect scissorArea() {
        return ss.scissor;
    }

    @Nullable
    @Override
    public ScreenRect bounds() {
        return ss.bounds;
    }
}