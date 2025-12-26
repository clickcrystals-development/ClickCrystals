package io.github.itzispyder.clickcrystals.util.minecraft.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import net.minecraft.client.render.LayeringTransform;
import net.minecraft.client.render.OutputTarget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ClickCrystalsRenderLayers {

    public static final RenderLayer LINES;
    public static final RenderLayer LINES_STRIP;
    public static final RenderLayer QUADS;
    public static final RenderLayer TRI_FAN;
    public static final RenderLayer TRI_STRIP;
    public static final Function<Identifier, RenderLayer> TEX_QUADS;
    public static final Function<Identifier, RenderLayer> TEX_TRI_FAN;
    public static final RenderLayer TRI_STRIP_CULL;
    public static final RenderLayer QUADS_CULL;
    public static final RenderLayer LINES_CULL;

    private static RenderSetup emptyParams(RenderPipeline pipeline) {
        return RenderSetup.builder(pipeline)
                .layeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                .outputTarget(OutputTarget.ITEM_ENTITY_TARGET)
                .build();
    }

    private static RenderSetup textureParams(RenderPipeline pipeline, Identifier id) {
        return RenderSetup.builder(pipeline)
                .texture("Sampler0", id, () -> RenderSystem.getSamplerCache().get(FilterMode.NEAREST))
                .layeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                .outputTarget(OutputTarget.ITEM_ENTITY_TARGET)
                .build();
    }

    static {
        LINES = RenderLayer.of("cc_layer_lines", emptyParams(ClickCrystalsRenderPipelines.PIPELINE_LINES));
        LINES_STRIP = RenderLayer.of("cc_layer_lines_strip", emptyParams(ClickCrystalsRenderPipelines.PIPELINE_LINES_STRIP));
        QUADS = RenderLayer.of("cc_layer_quads", emptyParams(ClickCrystalsRenderPipelines.PIPELINE_QUADS));
        TRI_FAN = RenderLayer.of("cc_layer_tri_fan", emptyParams(ClickCrystalsRenderPipelines.PIPELINE_TRI_FAN));
        TRI_STRIP = RenderLayer.of("cc_layer_tri_strip", emptyParams(ClickCrystalsRenderPipelines.PIPELINE_TRI_STRIP));
        TEX_QUADS = id -> RenderLayer.of("cc_layer_tex_quad", textureParams(ClickCrystalsRenderPipelines.PIPELINE_TEX_QUADS, id));
        TEX_TRI_FAN = id -> RenderLayer.of("cc_layer_tex_tri_fan", textureParams(ClickCrystalsRenderPipelines.PIPELINE_TEX_TRI_FAN, id));
        TRI_STRIP_CULL = RenderLayer.of("cc_layer_tri_strip_cull", emptyParams(ClickCrystalsRenderPipelines.PIPELINE_TRI_STRIP_CULL));
        QUADS_CULL = RenderLayer.of("cc_layer_quads_cull", emptyParams(ClickCrystalsRenderPipelines.PIPELINE_QUADS_CULL));
        LINES_CULL = RenderLayer.of("cc_layer_lines_cull", emptyParams(ClickCrystalsRenderPipelines.PIPELINE_LINES_CULL));
    }
}