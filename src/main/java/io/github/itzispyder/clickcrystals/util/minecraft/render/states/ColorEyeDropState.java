package io.github.itzispyder.clickcrystals.util.minecraft.render.states;

import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderPipelines;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public class ColorEyeDropState implements GuiElementRenderState {

    private final RenderPipeline pipeline;
    private final TextureSetup texture;
    private final Matrix3x2f pose;
    public float x, y, w, h, r;
    public int color;
    private final ScreenRectangle scissor, bounds;

    private ColorEyeDropState(RenderPipeline pipeline, TextureSetup texture, Matrix3x2f pose, float x, float y, float w, float h, float r, int color, ScreenRectangle scissor, ScreenRectangle bounds) {
        this.pipeline = pipeline;
        this.texture = texture;
        this.pose = pose;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.r = r;
        this.color = color;
        this.scissor = scissor;
        this.bounds = bounds;
    }

    private ColorEyeDropState(Matrix3x2f pose, float x, float y, float w, float h, float r, int color, ScreenRectangle scissor) {
        this(RenderPipelines.PIPELINE_QUADS, TextureSetup.noTexture(), pose,
                x, y, w, h, (float) MathUtils.clamp(r, 0, Math.min(w, h) / 2),
                color,
                scissor,
                createBounds(
                        pose, scissor,
                        x, y, w, h
                ));
    }

    public ColorEyeDropState(GuiGraphicsExtractor context, float x, float y, float h, float r, int color) {
        this(new Matrix3x2f(context.pose()), x, y, r * 2, h, r, color, context.scissorStack.peek());
    }

    // squeezes entire quads into triangle fans for rounded rectangle
    // ...yeah i know ...blame vibrant visuals
    @Override
    public void buildVertices(VertexConsumer buf) {
        float dTheta = Mth.PI / 20;
        float thetaOffset = Mth.PI / 6;
        float cx = x;
        float cy = y - h + r;
        float rx, ry;

        for (float i = -thetaOffset; i < Mth.PI + thetaOffset; i += dTheta) {
            buf.addVertexWith2DPose(pose, cx, cy).setColor(color);
            buf.addVertexWith2DPose(pose, cx, cy).setColor(color);

            rx = cx + r * Mth.cos(i);
            ry = cy - r * Mth.sin(i);
            buf.addVertexWith2DPose(pose, rx, ry).setColor(color);

            rx = cx + r * Mth.cos(i + dTheta);
            ry = cy - r * Mth.sin(i + dTheta);
            buf.addVertexWith2DPose(pose, rx, ry).setColor(color);
        }

        float connectY = cy - r * Mth.sin(-thetaOffset);
        float connectX1 = cx + r * Mth.cos(-thetaOffset);
        float connectX2 = cx + r * Mth.cos(Mth.PI + thetaOffset);
        buf.addVertexWith2DPose(pose, cx, cy).setColor(color);
        buf.addVertexWith2DPose(pose, connectX1, connectY).setColor(color);
        buf.addVertexWith2DPose(pose, x, y).setColor(color);
        buf.addVertexWith2DPose(pose, connectX2, connectY).setColor(color);
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
