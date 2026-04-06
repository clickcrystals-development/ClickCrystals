package io.github.itzispyder.clickcrystals.util.minecraft.render.states;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.ClickCrystalsRenderPipelines;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public class ClickCrystalsRoundRectWireframeState implements GuiElementRenderState {

    private final RenderPipeline pipeline;
    private final TextureSetup texture;
    private final Matrix3x2f pose;
    public float x, y, w, h, r, thickness;
    public int colorInner1, colorOuter1, colorInner2, colorOuter2, colorInner3, colorOuter3, colorInner4, colorOuter4;
    private final ScreenRectangle scissor, bounds;

    public ClickCrystalsRoundRectWireframeState(RenderPipeline pipeline, TextureSetup texture, Matrix3x2f pose, float x, float y, float w, float h, float r, float thickness, int colorInner1, int colorOuter1, int colorInner2, int colorOuter2, int colorInner3, int colorOuter3, int colorInner4, int colorOuter4, ScreenRectangle scissor, ScreenRectangle bounds) {
        this.pipeline = pipeline;
        this.texture = texture;
        this.pose = pose;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.r = r;
        this.thickness = thickness;
        this.colorInner1 = colorInner1;
        this.colorOuter1 = colorOuter1;
        this.colorInner2 = colorInner2;
        this.colorOuter2 = colorOuter2;
        this.colorInner3 = colorInner3;
        this.colorOuter3 = colorOuter3;
        this.colorInner4 = colorInner4;
        this.colorOuter4 = colorOuter4;
        this.scissor = scissor;
        this.bounds = bounds;
    }

    public ClickCrystalsRoundRectWireframeState(Matrix3x2f pose, float x, float y, float w, float h, float r, float thickness, int colorInner1, int colorOuter1, int colorInner2, int colorOuter2, int colorInner3, int colorOuter3, int colorInner4, int colorOuter4, ScreenRectangle scissor) {
        this(ClickCrystalsRenderPipelines.PIPELINE_QUADS, TextureSetup.noTexture(), pose,
                x, y, w, h, (float) MathUtils.clamp(r, 0, Math.min(w, h) / 2), thickness,
                colorInner1, colorOuter1, colorInner2, colorOuter2, colorInner3, colorOuter3, colorInner4, colorOuter4,
                scissor,
                createBounds(
                        pose, scissor,
                        x, y, w, h, thickness
                ));
    }

    public ClickCrystalsRoundRectWireframeState(GuiGraphics context, float x, float y, float w, float h, float r, float thickness, int colorInner1, int colorOuter1, int colorInner2, int colorOuter2, int colorInner3, int colorOuter3, int colorInner4, int colorOuter4) {
        this(new Matrix3x2f(context.pose()), x, y, w, h, r, thickness, colorInner1, colorOuter1, colorInner2, colorOuter2, colorInner3, colorOuter3, colorInner4, colorOuter4, context.scissorStack.peek());
    }

    public ClickCrystalsRoundRectWireframeState(GuiGraphics context, float x, float y, float w, float h, float r, float thickness, int colorInner, int colorOuter) {
        this(new Matrix3x2f(context.pose()), x, y, w, h, r, thickness, colorInner, colorOuter, colorInner, colorOuter, colorInner, colorOuter, colorInner, colorOuter, context.scissorStack.peek());
    }

    public ClickCrystalsRoundRectWireframeState(GuiGraphics context, float x, float y, float w, float h, float r, float thickness, int color) {
        this(context, x, y, w, h, r, thickness, color, color);
    }

    // squeezes entire quads into triangle fans for rounded rectangle
    // ...yeah i know ...blame vibrant visuals
    @Override
    public void buildVertices(VertexConsumer buf) {
        float[][] corners = {
                { x + w - r,  y + h - r },
                { x + r,      y + h - r },
                { x + r,      y + r     },
                { x + w - r,  y + r     }
        };
        int[][] colors = {
                { colorInner3, colorOuter3 },
                { colorInner4, colorOuter4 },
                { colorInner1, colorOuter1 },
                { colorInner2, colorOuter2 }
        };

        for (int i = 0; i < 360; i += 10) {
            int corner = i / 90;
            float angle;

            angle = (float)Math.toRadians(i);
            float x1 = corners[corner][0] + (Mth.cos(angle) * r);
            float y1 = corners[corner][1] + (Mth.sin(angle) * r);
            float x4 = corners[corner][0] + Mth.cos(angle) * (r + thickness);
            float y4 = corners[corner][1] + Mth.sin(angle) * (r + thickness);

            angle = (float)Math.toRadians(i + 10);
            float x2 = corners[corner][0] + (Mth.cos(angle) * r);
            float y2 = corners[corner][1] + (Mth.sin(angle) * r);
            float x3 = corners[corner][0] + Mth.cos(angle) * (r + thickness);
            float y3 = corners[corner][1] + Mth.sin(angle) * (r + thickness);

            buf.addVertexWith2DPose(pose, x1, y1).setColor(colors[corner][0]);
            buf.addVertexWith2DPose(pose, x2, y2).setColor(colors[corner][0]);
            buf.addVertexWith2DPose(pose, x3, y3).setColor(colors[corner][1]);
            buf.addVertexWith2DPose(pose, x4, y4).setColor(colors[corner][1]);
        }

        buf.addVertexWith2DPose(pose, x + w - r, y + h).setColor(colors[0][0]);
        buf.addVertexWith2DPose(pose, x + r, y + h).setColor(colors[0][0]);
        buf.addVertexWith2DPose(pose, x + r, y + h + thickness).setColor(colors[0][1]);
        buf.addVertexWith2DPose(pose, x + w - r, y + h + thickness).setColor(colors[0][1]);

        buf.addVertexWith2DPose(pose, x, y + h - r).setColor(colors[1][0]);
        buf.addVertexWith2DPose(pose, x, y + r).setColor(colors[1][0]);
        buf.addVertexWith2DPose(pose, x - thickness, y + r).setColor(colors[1][1]);
        buf.addVertexWith2DPose(pose, x - thickness, y + h - r).setColor(colors[1][1]);

        buf.addVertexWith2DPose(pose, x + r, y).setColor(colors[2][0]);
        buf.addVertexWith2DPose(pose, x + w - r, y).setColor(colors[2][0]);
        buf.addVertexWith2DPose(pose, x + w - r, y - thickness).setColor(colors[2][1]);
        buf.addVertexWith2DPose(pose, x + r, y - thickness).setColor(colors[2][1]);

        buf.addVertexWith2DPose(pose, x + w, y + r).setColor(colors[3][0]);
        buf.addVertexWith2DPose(pose, x + w, y + h - r).setColor(colors[3][0]);
        buf.addVertexWith2DPose(pose, x + w + thickness, y + h - r).setColor(colors[3][1]);
        buf.addVertexWith2DPose(pose, x + w + thickness, y + r).setColor(colors[3][1]);
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

    private static ScreenRectangle createBounds(Matrix3x2f pose, ScreenRectangle scissor, float x, float y, float w, float h, float thickness) {
        int a = (int) (x - thickness);
        int b = (int) (y - thickness);
        int c = (int) (w + thickness * 2);
        int d = (int) (h + thickness * 2);
        ScreenRectangle bounds = new ScreenRectangle(a, b, c, d).transformMaxBounds(pose);
        return scissor == null ? bounds : scissor.intersection(bounds);
    }
}
