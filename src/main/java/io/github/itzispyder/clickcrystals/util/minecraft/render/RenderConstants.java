package io.github.itzispyder.clickcrystals.util.minecraft.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.TriState;

import java.util.function.Function;

public class RenderConstants {

    public static final RenderPipeline PIPELINE_LINES = RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
            .withLocation("pipeline/global_lines_pipeline")
            .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.DEBUG_LINES)
            .withBlend(BlendFunction.TRANSLUCENT)
            .withCull(false)
            .withDepthWrite(false)
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .build();

    public static final RenderPipeline PIPELINE_LINES_STRIP = RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
            .withLocation("pipeline/global_lines_pipeline")
            .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.DEBUG_LINE_STRIP)
            .withBlend(BlendFunction.TRANSLUCENT)
            .withCull(false)
            .withDepthWrite(false)
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .build();

    public static final RenderPipeline PIPELINE_QUADS = RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
            .withLocation("pipeline/global_fill_pipeline")
            .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS)
            .withBlend(BlendFunction.TRANSLUCENT)
            .withCull(false)
            .withDepthWrite(false)
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .build();

    public static final RenderPipeline PIPELINE_TRI_FAN = RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
            .withLocation("pipeline/global_fill_pipeline")
            .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.TRIANGLE_FAN)
            .withBlend(BlendFunction.TRANSLUCENT)
            .withCull(false)
            .withDepthWrite(false)
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .build();

    public static final RenderPipeline PIPELINE_TRI_STRIP = RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
            .withLocation("pipeline/global_fill_pipeline")
            .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.TRIANGLE_STRIP)
            .withBlend(BlendFunction.TRANSLUCENT)
            .withCull(false)
            .withDepthWrite(false)
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .build();

    public static final RenderPipeline PIPELINE_TEX_QUADS = RenderPipeline.builder(RenderPipelines.POSITION_TEX_COLOR_SNIPPET)
            .withLocation("pipeline/gui_textured")
            .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, VertexFormat.DrawMode.QUADS)
            .withBlend(BlendFunction.TRANSLUCENT)
            .withCull(false)
            .withDepthWrite(false)
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .build();

    public static final RenderPipeline PIPELINE_TEX_TRI_FAN = RenderPipeline.builder(RenderPipelines.POSITION_TEX_COLOR_SNIPPET)
            .withLocation("pipeline/gui_textured")
            .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, VertexFormat.DrawMode.TRIANGLE_FAN)
            .withBlend(BlendFunction.TRANSLUCENT)
            .withCull(false)
            .withDepthWrite(false)
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .build();

    public static final RenderPipeline PIPELINE_TRI_STRIP_CULL = RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
            .withLocation("pipeline/global_fill_pipeline")
            .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.TRIANGLE_STRIP)
            .withBlend(BlendFunction.TRANSLUCENT)
            .withCull(true)
            .withDepthWrite(true)
            .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
            .build();


    private static RenderLayer.MultiPhaseParameters emptyParams() {
        return RenderLayer.MultiPhaseParameters.builder().build(false);
    }

    private static RenderLayer.MultiPhaseParameters textureParams(Identifier id) {
        return RenderLayer.MultiPhaseParameters.builder()
                .texture(new RenderPhase.Texture(id, TriState.FALSE, false))
                .build(false);
    }

    public static final RenderLayer LINES = RenderLayer.of("cc_layer_lines", 256, PIPELINE_LINES, emptyParams());
    public static final RenderLayer LINES_STRIP = RenderLayer.of("cc_layer_lines_strip", 256, PIPELINE_LINES_STRIP, emptyParams());
    public static final RenderLayer QUADS = RenderLayer.of("cc_layer_quads", 256, PIPELINE_QUADS, emptyParams());
    public static final RenderLayer TRI_FAN = RenderLayer.of("cc_layer_tri_fan", 256, PIPELINE_TRI_FAN, emptyParams());
    public static final RenderLayer TRI_STRIP = RenderLayer.of("cc_layer_tri_strip", 256, PIPELINE_TRI_STRIP, emptyParams());
    public static final Function<Identifier, RenderLayer> TEX_QUADS = id -> RenderLayer.of("cc_layer_tex_quad", 256, PIPELINE_TEX_QUADS, textureParams(id));
    public static final Function<Identifier, RenderLayer> TEX_TRI_FAN = id -> RenderLayer.of("cc_layer_tex_tri_fan", 256, PIPELINE_TEX_TRI_FAN, textureParams(id));

    public static final RenderLayer TRI_STRIP_CULL = RenderLayer.of("cc_layer_tri_strip_cull", 256, PIPELINE_TRI_STRIP_CULL, emptyParams());

}
