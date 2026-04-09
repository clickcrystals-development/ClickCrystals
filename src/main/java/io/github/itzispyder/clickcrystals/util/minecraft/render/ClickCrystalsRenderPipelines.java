package io.github.itzispyder.clickcrystals.util.minecraft.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderPipelines;

public class ClickCrystalsRenderPipelines {
    
    public static final ColorTargetState WITH_BLEND = new ColorTargetState(BlendFunction.TRANSLUCENT);
    public static final DepthStencilState DEPTH_NONE = new DepthStencilState(CompareOp.ALWAYS_PASS, false);
    public static final DepthStencilState DEPTH_LEQUAL = new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, true);

    public static final RenderPipeline PIPELINE_LINES = RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_lines_pipeline")
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.DEBUG_LINES)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_NONE)
            .build();

    public static final RenderPipeline PIPELINE_LINES_STRIP = RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_lines_pipeline")
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.DEBUG_LINE_STRIP)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_NONE)
            .build();

    public static final RenderPipeline PIPELINE_QUADS = RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_fill_pipeline")
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_NONE)
            .build();

    public static final RenderPipeline PIPELINE_TRI_FAN = RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_fill_pipeline")
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLE_FAN)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_NONE)
            .build();

    public static final RenderPipeline PIPELINE_TRI_STRIP = RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_fill_pipeline")
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLE_STRIP)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_NONE)
            .build();

    public static final RenderPipeline PIPELINE_TRI = RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_fill_pipeline")
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLES)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_NONE)
            .build();

    public static final RenderPipeline PIPELINE_TEX_QUADS = RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET)
            .withLocation("pipeline/gui_textured")
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_NONE)
            .build();

    public static final RenderPipeline PIPELINE_TEX_TRI_FAN = RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET)
            .withLocation("pipeline/gui_textured")
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.TRIANGLE_FAN)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_NONE)
            .build();

    public static final RenderPipeline PIPELINE_TRI_STRIP_CULL = RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_fill_pipeline")
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLE_STRIP)
            .withColorTargetState(WITH_BLEND)
            .withCull(true)
            .withDepthStencilState(DEPTH_LEQUAL)
            .build();

    public static final RenderPipeline PIPELINE_QUADS_CULL = RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_fill_pipeline")
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_LEQUAL)
            .build();

    public static final RenderPipeline PIPELINE_LINES_CULL = RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_lines_pipeline")
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.DEBUG_LINES)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_LEQUAL)
            .build();
}
