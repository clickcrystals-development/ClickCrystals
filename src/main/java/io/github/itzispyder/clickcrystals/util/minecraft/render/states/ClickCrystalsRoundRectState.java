package io.github.itzispyder.clickcrystals.util.minecraft.render.states;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.ClickCrystalsRenderPipelines;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public class ClickCrystalsRoundRectState implements GuiElementRenderState {

    private final RenderPipeline pipeline;
    private final TextureSetup texture;
    private final Matrix3x2f pose;
    public float x, y, w, h, r;
    public int color1, color2, color3, color4, colorCenter;
    private final ScreenRectangle scissor, bounds;

    public ClickCrystalsRoundRectState(RenderPipeline pipeline, TextureSetup texture, Matrix3x2f pose, float x, float y, float w, float h, float r, int color1, int color2, int color3, int color4, int colorCenter, ScreenRectangle scissor, ScreenRectangle bounds) {
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

    public ClickCrystalsRoundRectState(Matrix3x2f pose, float x, float y, float w, float h, float r, int color1, int color2, int color3, int color4, int colorCenter, ScreenRectangle scissor) {
        this(ClickCrystalsRenderPipelines.PIPELINE_QUADS, TextureSetup.noTexture(), pose,
                x, y, w, h, (float) MathUtils.clamp(r, 0, Math.min(w, h) / 2),
                color1, color2, color3, color4, colorCenter,
                scissor,
                createBounds(
                        pose, scissor,
                        x, y, w, h
                ));
    }

    public ClickCrystalsRoundRectState(GuiGraphicsExtractor context, float x, float y, float w, float h, float r, int color1, int color2, int color3, int color4, int colorCenter) {
        this(new Matrix3x2f(context.pose()), x, y, w, h, r, color1, color2, color3, color4, colorCenter, context.scissorStack.peek());
    }

    public ClickCrystalsRoundRectState(GuiGraphicsExtractor context, float x, float y, float w, float h, float r, int color) {
        this(context, x, y, w, h, r, color, color, color, color, color);
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
        int[] colors = { color3, color4, color1, color2 };

        float x1 = x + w / 2F;
        float y1 = y + h / 2F;

        for (int i = 0; i < 360; i += 10) {
            int corner = i / 90;
            float angle;

            angle = (float)Math.toRadians(i);
            float x2 = corners[corner][0] + (Mth.cos(angle) * r);
            float y2 = corners[corner][1] + (Mth.sin(angle) * r);

            angle = (float)Math.toRadians(i + 10);
            float x3 = corners[corner][0] + (Mth.cos(angle) * r);
            float y3 = corners[corner][1] + (Mth.sin(angle) * r);

            buf.addVertexWith2DPose(pose, x1, y1).setColor(colorCenter);
            buf.addVertexWith2DPose(pose, x1, y1).setColor(colorCenter);
            buf.addVertexWith2DPose(pose, x2, y2).setColor(colors[corner]);
            buf.addVertexWith2DPose(pose, x3, y3).setColor(colors[corner]);
        }

        buf.addVertexWith2DPose(pose, x1, y1).setColor(colorCenter);
        buf.addVertexWith2DPose(pose, x1, y1).setColor(colorCenter);
        buf.addVertexWith2DPose(pose, x + w - r, y + h).setColor(colors[0]);
        buf.addVertexWith2DPose(pose, x + r, y + h).setColor(colors[1]);

        buf.addVertexWith2DPose(pose, x1, y1).setColor(colorCenter);
        buf.addVertexWith2DPose(pose, x1, y1).setColor(colorCenter);
        buf.addVertexWith2DPose(pose, x, y + h - r).setColor(colors[1]);
        buf.addVertexWith2DPose(pose, x, y + r).setColor(colors[2]);

        buf.addVertexWith2DPose(pose, x1, y1).setColor(colorCenter);
        buf.addVertexWith2DPose(pose, x1, y1).setColor(colorCenter);
        buf.addVertexWith2DPose(pose, x + r, y).setColor(colors[2]);
        buf.addVertexWith2DPose(pose, x + w - r, y).setColor(colors[3]);

        buf.addVertexWith2DPose(pose, x1, y1).setColor(colorCenter);
        buf.addVertexWith2DPose(pose, x1, y1).setColor(colorCenter);
        buf.addVertexWith2DPose(pose, x + w, y + r).setColor(colors[3]);
        buf.addVertexWith2DPose(pose, x + w, y + h - r).setColor(colors[0]);
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
