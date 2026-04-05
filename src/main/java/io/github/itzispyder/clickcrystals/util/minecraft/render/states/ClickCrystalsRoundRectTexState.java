package io.github.itzispyder.clickcrystals.util.minecraft.render.states;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.ClickCrystalsRenderPipelines;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public class ClickCrystalsRoundRectTexState implements GuiElementRenderState {

    private final RenderPipeline pipeline;
    private final TextureSetup texture;
    private final Matrix3x2f pose;
    public float x, y, w, h, r;
    private final ScreenRectangle scissor, bounds;

    public ClickCrystalsRoundRectTexState(RenderPipeline pipeline, TextureSetup texture, Matrix3x2f pose, float x, float y, float w, float h, float r, ScreenRectangle scissor, ScreenRectangle bounds) {
        this.pipeline = pipeline;
        this.texture = texture;
        this.pose = pose;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.r = r;
        this.scissor = scissor;
        this.bounds = bounds;
    }

    public ClickCrystalsRoundRectTexState(Matrix3x2f pose, Identifier texture, float x, float y, float w, float h, float r, ScreenRectangle scissor) {
        this(ClickCrystalsRenderPipelines.PIPELINE_TEX_QUADS,
                TextureSetup.singleTexture(Minecraft.getInstance()
                                .getTextureManager()
                                .getTexture(texture)
                                .getTextureView(),
                        RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST)),
                pose,
                x, y, w, h, (float) MathUtils.clamp(r, 0, Math.min(w, h) / 2),
                scissor,
                createBounds(
                        pose, scissor,
                        x, y, w, h
                ));
    }

    public ClickCrystalsRoundRectTexState(GuiGraphicsExtractor context, Identifier texture, float x, float y, float w, float h, float r) {
        this(new Matrix3x2f(context.pose()), texture, x, y, w, h, r, context.scissorStack.peek());
    }

    @Override
    public void buildVertices(VertexConsumer buf) {
        float[][] corners = {
                { x + w - r,  y + h - r },
                { x + r,      y + h - r },
                { x + r,      y + r     },
                { x + w - r,  y + r     }
        };

        float x1 = x + w / 2F;
        float y1 = y + h / 2F;
        float u1 = 0.5F;
        float v1 = 0.5F;

        for (int i = 0; i < 360; i += 10) {
            int corner = i / 90;
            float angle;

            angle = (float)Math.toRadians(i);
            float x2 = corners[corner][0] + (Mth.cos(angle) * r);
            float y2 = corners[corner][1] + (Mth.sin(angle) * r);
            float u2 = (x2 - x) / w;
            float v2 = (y2 - y) / h;

            angle = (float)Math.toRadians(i + 10);
            float x3 = corners[corner][0] + (Mth.cos(angle) * r);
            float y3 = corners[corner][1] + (Mth.sin(angle) * r);
            float u3 = (x3 - x) / w;
            float v3 = (y3 - y) / h;

            buf.addVertexWith2DPose(pose, x1, y1).setUv(u1, v1).setColor(-1);
            buf.addVertexWith2DPose(pose, x1, y1).setUv(u1, v1).setColor(-1);
            buf.addVertexWith2DPose(pose, x2, y2).setUv(u2, v2).setColor(-1);
            buf.addVertexWith2DPose(pose, x3, y3).setUv(u3, v3).setColor(-1);
        }

        buf.addVertexWith2DPose(pose, x1, y1).setUv(u1, v1).setColor(-1);
        buf.addVertexWith2DPose(pose, x1, y1).setUv(u1, v1).setColor(-1);
        buf.addVertexWith2DPose(pose, x + w - r, y + h).setUv((w - r) / w, 1).setColor(-1);
        buf.addVertexWith2DPose(pose, x + r, y + h).setUv(r / w, 1).setColor(-1);

        buf.addVertexWith2DPose(pose, x1, y1).setUv(u1, v1).setColor(-1);
        buf.addVertexWith2DPose(pose, x1, y1).setUv(u1, v1).setColor(-1);
        buf.addVertexWith2DPose(pose, x, y + h - r).setUv(0, (h - r) / h).setColor(-1);
        buf.addVertexWith2DPose(pose, x, y + r).setUv(0, r / h).setColor(-1);

        buf.addVertexWith2DPose(pose, x1, y1).setUv(u1, v1).setColor(-1);
        buf.addVertexWith2DPose(pose, x1, y1).setUv(u1, v1).setColor(-1);
        buf.addVertexWith2DPose(pose, x + r, y).setUv(r / w, 0).setColor(-1);
        buf.addVertexWith2DPose(pose, x + w - r, y).setUv((w - r) / w, 0).setColor(-1);

        buf.addVertexWith2DPose(pose, x1, y1).setUv(u1, v1).setColor(-1);
        buf.addVertexWith2DPose(pose, x1, y1).setUv(u1, v1).setColor(-1);
        buf.addVertexWith2DPose(pose, x + w, y + r).setUv(1, r / h).setColor(-1);
        buf.addVertexWith2DPose(pose, x + w, y + h - r).setUv(1, (h - r) / h).setColor(-1);
    }

    @Override
    public RenderPipeline pipeline() {
        return pipeline;
    }

    @Override
    public TextureSetup textureSetup() {
        return texture;
    }

    @Nullable
    @Override
    public ScreenRectangle scissorArea() {
        return scissor;
    }

    @Nullable
    @Override
    public ScreenRectangle bounds() {
        return bounds;
    }

    private static ScreenRectangle createBounds(Matrix3x2f pose, ScreenRectangle scissor, float x, float y, float w, float h) {
        ScreenRectangle bounds = new ScreenRectangle((int) x, (int) y, (int) w, (int) h).transformMaxBounds(pose);
        return scissor == null ? bounds : scissor.intersection(bounds);
    }
}