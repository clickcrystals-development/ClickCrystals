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

public class ClickCrystalsRoundRectState implements SimpleGuiElementRenderState {

    private final RenderPipeline pipeline;
    private final TextureSetup texture;
    private final Matrix3x2f pose;
    public float x, y, w, h, r;
    public int color1, color2, color3, color4, colorCenter;
    private final ScreenRect scissor, bounds;

    public ClickCrystalsRoundRectState(RenderPipeline pipeline, TextureSetup texture, Matrix3x2f pose, float x, float y, float w, float h, float r, int color1, int color2, int color3, int color4, int colorCenter, ScreenRect scissor, ScreenRect bounds) {
        this.pipeline = pipeline;
        this.texture = texture;
        this.pose = pose;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.r = r;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
        this.colorCenter = colorCenter;
        this.scissor = scissor;
        this.bounds = bounds;
    }

    public ClickCrystalsRoundRectState(Matrix3x2f pose, float x, float y, float w, float h, float r, int color1, int color2, int color3, int color4, int colorCenter, ScreenRect scissor) {
        this(ClickCrystalsRenderPipelines.PIPELINE_QUADS, TextureSetup.empty(), pose,
                x, y, w, h, (float) MathUtils.clamp(r, 0, Math.min(w, h) / 2),
                color1, color2, color3, color4, colorCenter,
                scissor,
                createBounds(
                        pose, scissor,
                        x, y, w, h
                ));
    }

    public ClickCrystalsRoundRectState(DrawContext context, float x, float y, float w, float h, float r, int color1, int color2, int color3, int color4, int colorCenter) {
        this(new Matrix3x2f(context.getMatrices()), x, y, w, h, r, color1, color2, color3, color4, colorCenter, context.scissorStack.peekLast());
    }

    public ClickCrystalsRoundRectState(DrawContext context, float x, float y, float w, float h, float r, int color) {
        this(context, x, y, w, h, r, color, color, color, color, color);
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
        int[] colors = { color3, color4, color1, color2 };

        float x1 = x + w / 2F;
        float y1 = y + h / 2F;

        for (int i = 0; i < 360; i += 10) {
            int corner = i / 90;
            float angle;

            angle = (float)Math.toRadians(i);
            float x2 = corners[corner][0] + (float)(Math.cos(angle) * r);
            float y2 = corners[corner][1] + (float)(Math.sin(angle) * r);

            angle = (float)Math.toRadians(i + 10);
            float x3 = corners[corner][0] + (float)(Math.cos(angle) * r);
            float y3 = corners[corner][1] + (float)(Math.sin(angle) * r);

            buf.vertex(pose, x1, y1, depth).color(colorCenter);
            buf.vertex(pose, x1, y1, depth).color(colorCenter);
            buf.vertex(pose, x2, y2, depth).color(colors[corner]);
            buf.vertex(pose, x3, y3, depth).color(colors[corner]);
        }

        buf.vertex(pose, x1, y1, depth).color(colorCenter);
        buf.vertex(pose, x1, y1, depth).color(colorCenter);
        buf.vertex(pose, x + w - r, y + h, depth).color(colors[0]);
        buf.vertex(pose, x + r, y + h, depth).color(colors[1]);

        buf.vertex(pose, x1, y1, depth).color(colorCenter);
        buf.vertex(pose, x1, y1, depth).color(colorCenter);
        buf.vertex(pose, x, y + h - r, depth).color(colors[1]);
        buf.vertex(pose, x, y + r, depth).color(colors[2]);

        buf.vertex(pose, x1, y1, depth).color(colorCenter);
        buf.vertex(pose, x1, y1, depth).color(colorCenter);
        buf.vertex(pose, x + r, y, depth).color(colors[2]);
        buf.vertex(pose, x + w - r, y, depth).color(colors[3]);

        buf.vertex(pose, x1, y1, depth).color(colorCenter);
        buf.vertex(pose, x1, y1, depth).color(colorCenter);
        buf.vertex(pose, x + w, y + r, depth).color(colors[3]);
        buf.vertex(pose, x + w, y + h - r, depth).color(colors[0]);
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

    private static ScreenRect createBounds(Matrix3x2f pose, ScreenRect scissor, float x, float y, float w, float h) {
        ScreenRect bounds = new ScreenRect((int) x, (int) y, (int) w, (int) h).transformEachVertex(pose);
        return scissor == null ? bounds : scissor.intersection(bounds);
    }
}
