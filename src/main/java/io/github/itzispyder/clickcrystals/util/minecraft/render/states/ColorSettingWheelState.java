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

import java.awt.*;

public class ColorSettingWheelState implements GuiElementRenderState {

    private final RenderPipeline pipeline;
    private final TextureSetup texture;
    private final Matrix3x2f pose;
    public float x, y, w, h, r;
    private final ScreenRectangle scissor, bounds;

    private ColorSettingWheelState(RenderPipeline pipeline, TextureSetup texture, Matrix3x2f pose, float x, float y, float w, float h, float r, ScreenRectangle scissor, ScreenRectangle bounds) {
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

    private ColorSettingWheelState(Matrix3x2f pose, float x, float y, float w, float h, float r, ScreenRectangle scissor) {
        this(RenderPipelines.PIPELINE_QUADS, TextureSetup.noTexture(), pose,
                x, y, w, h, (float) MathUtils.clamp(r, 0, Math.min(w, h) / 2),
                scissor,
                createBounds(
                        pose, scissor,
                        x, y, w, h
                ));
    }

    public ColorSettingWheelState(GuiGraphicsExtractor context, float x, float y, float r) {
        this(new Matrix3x2f(context.pose()), x, y, r * 2, r * 2, r, context.scissorStack.peek());
    }

    // squeezes entire quads into triangle fans for rounded rectangle
    // ...yeah i know ...blame vibrant visuals
    @Override
    public void buildVertices(VertexConsumer buf) {
        float cx = x + r;
        float cy = y + r;
        float rx, ry;

        float dTheta = Mth.PI / 180F;
        float dRad = this.r / 100;
        int hsbRgb;

        for (float i = 0; i <= Mth.TWO_PI; i += dTheta) { // hsb.h
            for (float r = 0; r <= this.r; r += dRad) { // hsb.s
                hsbRgb = Color.HSBtoRGB(i / Mth.TWO_PI, r / this.r, 1F);

                rx = cx + r * Mth.cos(i);
                ry = cy + r * Mth.sin(i);
                buf.addVertexWith2DPose(pose, rx, ry).setColor(hsbRgb);

                rx = cx + r * Mth.cos(i + dTheta);
                ry = cy + r * Mth.sin(i + dTheta);
                buf.addVertexWith2DPose(pose, rx, ry).setColor(hsbRgb);

                rx = cx + (r + dRad) * Mth.cos(i + dTheta);
                ry = cy + (r + dRad) * Mth.sin(i + dTheta);
                buf.addVertexWith2DPose(pose, rx, ry).setColor(hsbRgb);

                rx = cx + (r + dRad) * Mth.cos(i);
                ry = cy + (r + dRad) * Mth.sin(i);
                buf.addVertexWith2DPose(pose, rx, ry).setColor(hsbRgb);
            }
        }
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
