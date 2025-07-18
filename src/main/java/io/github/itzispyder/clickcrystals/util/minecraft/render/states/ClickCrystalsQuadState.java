package io.github.itzispyder.clickcrystals.util.minecraft.render.states;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import io.github.itzispyder.clickcrystals.util.minecraft.render.ClickCrystalsRenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.TextureSetup;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public class ClickCrystalsQuadState implements SimpleGuiElementRenderState {

    private final RenderPipeline pipeline;
    private final TextureSetup texture;
    private final Matrix3x2f pose;
    public float x1, x2, x3, x4, y1, y2, y3, y4;
    public int color1, color2, color3, color4;
    private final ScreenRect scissor, bounds;

    public ClickCrystalsQuadState(RenderPipeline pipeline, TextureSetup texture, Matrix3x2f pose, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, int color1, int color2, int color3, int color4, ScreenRect scissor, ScreenRect bounds) {
        this.pipeline = pipeline;
        this.texture = texture;
        this.pose = pose;
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.x4 = x4;
        this.y1 = y1;
        this.y2 = y2;
        this.y3 = y3;
        this.y4 = y4;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
        this.scissor = scissor;
        this.bounds = bounds;
    }

    public ClickCrystalsQuadState(Matrix3x2f pose, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, int color1, int color2, int color3, int color4, ScreenRect scissor) {
        this(ClickCrystalsRenderPipelines.PIPELINE_QUADS, TextureSetup.empty(), pose,
                x1, y1,
                x2, y2,
                x3, y3,
                x4, y4,
                color1, color2, color3, color4,
                scissor,
                createBounds(
                        pose, scissor,
                        x1, y1,
                        x2, y2,
                        x3, y3,
                        x4, y4
                ));
    }

    public ClickCrystalsQuadState(DrawContext context, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, int color1, int color2, int color3, int color4) {
        this(new Matrix3x2f(context.getMatrices()), x1, y1, x2, y2, x3, y3, x4, y4, color1, color2, color3, color4, context.scissorStack.peekLast());
    }

    public ClickCrystalsQuadState(DrawContext context, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, int color) {
        this(new Matrix3x2f(context.getMatrices()), x1, y1, x2, y2, x3, y3, x4, y4, color, color, color, color, context.scissorStack.peekLast());
    }

    public ClickCrystalsQuadState(DrawContext context, float x, float y, float w, float h, int color1, int color2, int color3, int color4) {
        this(context,
                x, y,
                x + w, y,
                x + w, y + h,
                x, y + h,
                color1, color2, color3, color4);
    }

    public ClickCrystalsQuadState(DrawContext context, float x, float y, float w, float h, int color) {
        this(context,
                x, y,
                x + w, y,
                x + w, y + h,
                x, y + h,
                color);
    }

    @Override
    public void setupVertices(VertexConsumer buf, float depth) {
        buf.vertex(pose, x1, y1, depth).color(color1);
        buf.vertex(pose, x2, y2, depth).color(color2);
        buf.vertex(pose, x3, y3, depth).color(color3);
        buf.vertex(pose, x4, y4, depth).color(color4);
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

    private static ScreenRect createBounds(Matrix3x2f pose, ScreenRect scissor, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        float minX = Math.min(x1, Math.min(x2, Math.min(x3, x4)));
        float maxX = Math.max(x1, Math.max(x2, Math.max(x3, x4)));
        float minY = Math.min(y1, Math.min(y2, Math.min(y3, y4)));
        float maxY = Math.max(y1, Math.max(y2, Math.max(y3, y4)));
        int x = (int) minX;
        int y = (int) minY;
        int w = (int) (maxX - minX);
        int h = (int) (maxY - minY);

        ScreenRect bounds = new ScreenRect(x, y, w, h).transformEachVertex(pose);
        return scissor == null ? bounds : scissor.intersection(bounds);
    }
}
