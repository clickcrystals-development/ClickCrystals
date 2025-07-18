package io.github.itzispyder.clickcrystals.util.minecraft.render.states;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.ClickCrystalsRenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.TextureSetup;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public class ClickCrystalsRoundRectWireframeState implements SimpleGuiElementRenderState {

    private final RenderPipeline pipeline;
    private final TextureSetup texture;
    private final Matrix3x2f pose;
    public float x, y, w, h, r, thickness;
    public int colorInner1, colorOuter1, colorInner2, colorOuter2, colorInner3, colorOuter3, colorInner4, colorOuter4;
    private final ScreenRect scissor, bounds;

    public ClickCrystalsRoundRectWireframeState(RenderPipeline pipeline, TextureSetup texture, Matrix3x2f pose, float x, float y, float w, float h, float r, float thickness, int colorInner1, int colorOuter1, int colorInner2, int colorOuter2, int colorInner3, int colorOuter3, int colorInner4, int colorOuter4, ScreenRect scissor, ScreenRect bounds) {
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

    public ClickCrystalsRoundRectWireframeState(Matrix3x2f pose, float x, float y, float w, float h, float r, float thickness, int colorInner1, int colorOuter1, int colorInner2, int colorOuter2, int colorInner3, int colorOuter3, int colorInner4, int colorOuter4, ScreenRect scissor) {
        this(ClickCrystalsRenderPipelines.PIPELINE_QUADS, TextureSetup.empty(), pose,
                x, y, w, h, (float) MathUtils.clamp(r, 0, Math.min(w, h) / 2), thickness,
                colorInner1, colorOuter1, colorInner2, colorOuter2, colorInner3, colorOuter3, colorInner4, colorOuter4,
                scissor,
                createBounds(
                        pose, scissor,
                        x, y, w, h, thickness
                ));
    }

    public ClickCrystalsRoundRectWireframeState(DrawContext context, float x, float y, float w, float h, float r, float thickness, int colorInner1, int colorOuter1, int colorInner2, int colorOuter2, int colorInner3, int colorOuter3, int colorInner4, int colorOuter4) {
        this(new Matrix3x2f(context.getMatrices()), x, y, w, h, r, thickness, colorInner1, colorOuter1, colorInner2, colorOuter2, colorInner3, colorOuter3, colorInner4, colorOuter4, context.scissorStack.peekLast());
    }

    public ClickCrystalsRoundRectWireframeState(DrawContext context, float x, float y, float w, float h, float r, float thickness, int colorInner, int colorOuter) {
        this(new Matrix3x2f(context.getMatrices()), x, y, w, h, r, thickness, colorInner, colorOuter, colorInner, colorOuter, colorInner, colorOuter, colorInner, colorOuter, context.scissorStack.peekLast());
    }

    public ClickCrystalsRoundRectWireframeState(DrawContext context, float x, float y, float w, float h, float r, float thickness, int color) {
        this(context, x, y, w, h, r, thickness, color, color);
    }

    // squeezes entire quads into triangle fans for rounded rectangle
    // ...yeah i know ...blame vibrant visuals
    @Override
    public void setupVertices(VertexConsumer buf, float depth) {
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
            float x1 = corners[corner][0] + (float)(Math.cos(angle) * r);
            float y1 = corners[corner][1] + (float)(Math.sin(angle) * r);
            float x4 = corners[corner][0] + (float)(Math.cos(angle) * (r + thickness));
            float y4 = corners[corner][1] + (float)(Math.sin(angle) * (r + thickness));

            angle = (float)Math.toRadians(i + 10);
            float x2 = corners[corner][0] + (float)(Math.cos(angle) * r);
            float y2 = corners[corner][1] + (float)(Math.sin(angle) * r);
            float x3 = corners[corner][0] + (float)(Math.cos(angle) * (r + thickness));
            float y3 = corners[corner][1] + (float)(Math.sin(angle) * (r + thickness));

            buf.vertex(pose, x1, y1, depth).color(colors[corner][0]);
            buf.vertex(pose, x2, y2, depth).color(colors[corner][0]);
            buf.vertex(pose, x3, y3, depth).color(colors[corner][1]);
            buf.vertex(pose, x4, y4, depth).color(colors[corner][1]);
        }

        buf.vertex(pose, x + w - r, y + h, depth).color(colors[0][0]);
        buf.vertex(pose, x + r, y + h, depth).color(colors[0][0]);
        buf.vertex(pose, x + r, y + h + thickness, depth).color(colors[0][1]);
        buf.vertex(pose, x + w - r, y + h + thickness, depth).color(colors[0][1]);

        buf.vertex(pose, x, y + h - r, depth).color(colors[1][0]);
        buf.vertex(pose, x, y + r, depth).color(colors[1][0]);
        buf.vertex(pose, x - thickness, y + r, depth).color(colors[1][1]);
        buf.vertex(pose, x - thickness, y + h - r, depth).color(colors[1][1]);

        buf.vertex(pose, x + r, y, depth).color(colors[2][0]);
        buf.vertex(pose, x + w - r, y, depth).color(colors[2][0]);
        buf.vertex(pose, x + w - r, y - thickness, depth).color(colors[2][1]);
        buf.vertex(pose, x + r, y - thickness, depth).color(colors[2][1]);

        buf.vertex(pose, x + w, y + r, depth).color(colors[3][0]);
        buf.vertex(pose, x + w, y + h - r, depth).color(colors[3][0]);
        buf.vertex(pose, x + w + thickness, y + h - r, depth).color(colors[3][1]);
        buf.vertex(pose, x + w + thickness, y + r, depth).color(colors[3][1]);
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
    public ScreenRect scissorArea() {
        return scissor;
    }

    @Nullable
    @Override
    public ScreenRect bounds() {
        return bounds;
    }

    private static ScreenRect createBounds(Matrix3x2f pose, ScreenRect scissor, float x, float y, float w, float h, float thickness) {
        int a = (int) (x - thickness);
        int b = (int) (y - thickness);
        int c = (int) (w + thickness * 2);
        int d = (int) (h + thickness * 2);
        ScreenRect bounds = new ScreenRect(a, b, c, d).transformEachVertex(pose);
        return scissor == null ? bounds : scissor.intersection(bounds);
    }
}
