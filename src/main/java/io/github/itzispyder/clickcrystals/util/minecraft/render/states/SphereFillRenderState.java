package io.github.itzispyder.clickcrystals.util.minecraft.render.states;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.ClickCrystalsRenderPipelines;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

public class SphereFillRenderState implements GuiElementRenderState {

    private final TextureSetup texture = TextureSetup.noTexture();
    private final SphereState ss;
    private final int color;

    public SphereFillRenderState(SphereState ss) {
        this.ss = ss;
        this.color = ss.color;
    }

    @Override
    public void buildVertices(VertexConsumer buf) {
        for (int pitch = 0; pitch < 360; pitch += ss.deltaTheta) {
            for (int yaw = 0; yaw < 180; yaw += ss.deltaTheta) {
                Vector3d vec1 = MathUtils.toVector(pitch, yaw, ss.radius);
                Vector3d vec2 = MathUtils.toVector(pitch + ss.deltaTheta, yaw, ss.radius);
                Vector3d vec3 = MathUtils.toVector(pitch + ss.deltaTheta, yaw + ss.deltaTheta, ss.radius);
                Vector3d vec4 = MathUtils.toVector(pitch, yaw + ss.deltaTheta, ss.radius);

                float[] vtx1 = MathUtils.projectVertex(vec1, ss.rotation, ss.focalLen);
                float[] vtx2 = MathUtils.projectVertex(vec2, ss.rotation, ss.focalLen);
                float[] vtx3 = MathUtils.projectVertex(vec3, ss.rotation, ss.focalLen);
                float[] vtx4 = MathUtils.projectVertex(vec4, ss.rotation, ss.focalLen);

                buf.addVertexWith2DPose(ss.pose, ss.x + vtx1[0], ss.y + vtx1[1]).setColor(color);
                buf.addVertexWith2DPose(ss.pose, ss.x + vtx2[0], ss.y + vtx2[1]).setColor(color);
                buf.addVertexWith2DPose(ss.pose, ss.x + vtx3[0], ss.y + vtx3[1]).setColor(color);
                buf.addVertexWith2DPose(ss.pose, ss.x + vtx4[0], ss.y + vtx4[1]).setColor(color);
            }
        }
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
    public ScreenRectangle scissorArea() {
        return ss.scissor;
    }

    @Nullable
    @Override
    public ScreenRectangle bounds() {
        return ss.bounds;
    }
}