package io.github.itzispyder.clickcrystals.util.minecraft.render.states;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.ClickCrystalsRenderPipelines;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.TextureSetup;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public class ClickCrystalsRoundRectTexState implements SimpleGuiElementRenderState {

    private final RenderPipeline pipeline;
    private final TextureSetup texture;
    private final Matrix3x2f pose;
    public float x, y, w, h, r;
    private final ScreenRect scissor, bounds;

    public ClickCrystalsRoundRectTexState(RenderPipeline pipeline, TextureSetup texture, Matrix3x2f pose, float x, float y, float w, float h, float r, ScreenRect scissor, ScreenRect bounds) {
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

    public ClickCrystalsRoundRectTexState(Matrix3x2f pose, Identifier texture, float x, float y, float w, float h, float r, ScreenRect scissor) {
        this(ClickCrystalsRenderPipelines.PIPELINE_TEX_QUADS,
                TextureSetup.withoutGlTexture(MinecraftClient.getInstance()
                        .getTextureManager()
                        .getTexture(texture)
                        .getGlTextureView()),
                pose,
                x, y, w, h, (float) MathUtils.clamp(r, 0, Math.min(w, h) / 2),
                scissor,
                createBounds(
                        pose, scissor,
                        x, y, w, h
                ));
    }

    public ClickCrystalsRoundRectTexState(DrawContext context, Identifier texture, float x, float y, float w, float h, float r) {
        this(new Matrix3x2f(context.getMatrices()), texture, x, y, w, h, r, context.scissorStack.peekLast());
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

        float x1 = x + w / 2F;
        float y1 = y + h / 2F;
        float u1 = 0.5F;
        float v1 = 0.5F;

        for (int i = 0; i < 360; i += 10) {
            int corner = i / 90;
            float angle;

            angle = (float)Math.toRadians(i);
            float x2 = corners[corner][0] + (float)(Math.cos(angle) * r);
            float y2 = corners[corner][1] + (float)(Math.sin(angle) * r);
            float u2 = (x2 - x) / w;
            float v2 = (y2 - y) / h;

            angle = (float)Math.toRadians(i + 10);
            float x3 = corners[corner][0] + (float)(Math.cos(angle) * r);
            float y3 = corners[corner][1] + (float)(Math.sin(angle) * r);
            float u3 = (x3 - x) / w;
            float v3 = (y3 - y) / h;

            buf.vertex(pose, x1, y1, depth).texture(u1, v1).color(-1);
            buf.vertex(pose, x1, y1, depth).texture(u1, v1).color(-1);
            buf.vertex(pose, x2, y2, depth).texture(u2, v2).color(-1);
            buf.vertex(pose, x3, y3, depth).texture(u3, v3).color(-1);
        }

        buf.vertex(pose, x1, y1, depth).texture(u1, v1).color(-1);
        buf.vertex(pose, x1, y1, depth).texture(u1, v1).color(-1);
        buf.vertex(pose, x + w - r, y + h, depth).texture((w - r) / w, 1).color(-1);
        buf.vertex(pose, x + r, y + h, depth).texture(r / w, 1).color(-1);

        buf.vertex(pose, x1, y1, depth).texture(u1, v1).color(-1);
        buf.vertex(pose, x1, y1, depth).texture(u1, v1).color(-1);
        buf.vertex(pose, x, y + h - r, depth).texture(0, (h - r) / h).color(-1);
        buf.vertex(pose, x, y + r, depth).texture(0, r / h).color(-1);

        buf.vertex(pose, x1, y1, depth).texture(u1, v1).color(-1);
        buf.vertex(pose, x1, y1, depth).texture(u1, v1).color(-1);
        buf.vertex(pose, x + r, y, depth).texture(r / w, 0).color(-1);
        buf.vertex(pose, x + w - r, y, depth).texture((w - r) / w, 0).color(-1);

        buf.vertex(pose, x1, y1, depth).texture(u1, v1).color(-1);
        buf.vertex(pose, x1, y1, depth).texture(u1, v1).color(-1);
        buf.vertex(pose, x + w, y + r, depth).texture(1, r / h).color(-1);
        buf.vertex(pose, x + w, y + h - r, depth).texture(1, (h - r) / h).color(-1);
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
