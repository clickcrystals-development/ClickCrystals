package io.github.itzispyder.clickcrystals.util.minecraft.render;

import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;

public class RenderPipelines {
    
    public static final ColorTargetState WITH_BLEND = new ColorTargetState(BlendFunction.TRANSLUCENT);
    public static final DepthStencilState DEPTH_NONE = new DepthStencilState(CompareOp.ALWAYS_PASS, false);
    public static final DepthStencilState DEPTH_LEQUAL = new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, true);

    public static final RenderPipeline PIPELINE_LINES = RenderPipeline.builder(net.minecraft.client.renderer.RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_lines_pipeline")
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.DEBUG_LINES)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_NONE)
            .build();

    public static final RenderPipeline PIPELINE_LINES_STRIP = RenderPipeline.builder(net.minecraft.client.renderer.RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_lines_pipeline")
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.DEBUG_LINE_STRIP)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_NONE)
            .build();

    public static final RenderPipeline PIPELINE_QUADS = RenderPipeline.builder(net.minecraft.client.renderer.RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_fill_pipeline")
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_NONE)
            .build();

    public static final RenderPipeline PIPELINE_TRI_FAN = RenderPipeline.builder(net.minecraft.client.renderer.RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_fill_pipeline")
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.TRIANGLE_FAN)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_NONE)
            .build();

    public static final RenderPipeline PIPELINE_TRI_STRIP = RenderPipeline.builder(net.minecraft.client.renderer.RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_fill_pipeline")
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.TRIANGLE_STRIP)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_NONE)
            .build();

    public static final RenderPipeline PIPELINE_TRI = RenderPipeline.builder(net.minecraft.client.renderer.RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_fill_pipeline")
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_NONE)
            .build();

    public static final RenderPipeline PIPELINE_TEX_QUADS = RenderPipeline.builder(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED_SNIPPET)
            .withLocation("pipeline/gui_textured")
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_NONE)
            .build();

    public static final RenderPipeline PIPELINE_TEX_TRI_FAN = RenderPipeline.builder(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED_SNIPPET)
            .withLocation("pipeline/gui_textured")
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.TRIANGLE_FAN)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_NONE)
            .build();

    public static final RenderPipeline PIPELINE_TRI_STRIP_CULL = RenderPipeline.builder(net.minecraft.client.renderer.RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_fill_pipeline")
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.TRIANGLE_STRIP)
            .withColorTargetState(WITH_BLEND)
            .withCull(true)
            .withDepthStencilState(DEPTH_LEQUAL)
            .build();

    public static final RenderPipeline PIPELINE_TRI_FAN_CULL = RenderPipeline.builder(net.minecraft.client.renderer.RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_fill_pipeline")
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.TRIANGLE_FAN)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_LEQUAL)
            .build();

    public static final RenderPipeline PIPELINE_QUADS_CULL = RenderPipeline.builder(net.minecraft.client.renderer.RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_fill_pipeline")
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_LEQUAL)
            .build();

    public static final RenderPipeline PIPELINE_LINES_CULL = RenderPipeline.builder(net.minecraft.client.renderer.RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation("pipeline/global_lines_pipeline")
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.DEBUG_LINES)
            .withColorTargetState(WITH_BLEND)
            .withCull(false)
            .withDepthStencilState(DEPTH_LEQUAL)
            .build();
}
