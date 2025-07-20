package io.github.itzispyder.clickcrystals.util.minecraft.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
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


    private static RenderLayer.MultiPhaseParameters emptyParams() {
        return RenderLayer.MultiPhaseParameters.builder()
                .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
                .target(RenderPhase.ITEM_ENTITY_TARGET)
                .build(false);
    }

    private static RenderLayer.MultiPhaseParameters textureParams(Identifier id) {
        return RenderLayer.MultiPhaseParameters.builder()
                .texture(new RenderPhase.Texture(id, false))
                .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
                .target(RenderPhase.ITEM_ENTITY_TARGET)
                .build(false);
    }

    
    static {
        LINES = RenderLayer.of("cc_layer_lines", 256, ClickCrystalsRenderPipelines.PIPELINE_LINES, emptyParams());
        LINES_STRIP = RenderLayer.of("cc_layer_lines_strip", 256, ClickCrystalsRenderPipelines.PIPELINE_LINES_STRIP, emptyParams());
        QUADS = RenderLayer.of("cc_layer_quads", 256, ClickCrystalsRenderPipelines.PIPELINE_QUADS, emptyParams());
        TRI_FAN = RenderLayer.of("cc_layer_tri_fan", 256, ClickCrystalsRenderPipelines.PIPELINE_TRI_FAN, emptyParams());
        TRI_STRIP = RenderLayer.of("cc_layer_tri_strip", 256, ClickCrystalsRenderPipelines.PIPELINE_TRI_STRIP, emptyParams());
        TEX_QUADS = id -> RenderLayer.of("cc_layer_tex_quad", 256, ClickCrystalsRenderPipelines.PIPELINE_TEX_QUADS, textureParams(id));
        TEX_TRI_FAN = id -> RenderLayer.of("cc_layer_tex_tri_fan", 256, ClickCrystalsRenderPipelines.PIPELINE_TEX_TRI_FAN, textureParams(id));
        TRI_STRIP_CULL = RenderLayer.of("cc_layer_tri_strip_cull", 256, ClickCrystalsRenderPipelines.PIPELINE_TRI_STRIP_CULL, emptyParams());
        QUADS_CULL = RenderLayer.of("cc_layer_quads_cull", 256, ClickCrystalsRenderPipelines.PIPELINE_QUADS_CULL, emptyParams());
        LINES_CULL = RenderLayer.of("cc_layer_lines_cull", 256, ClickCrystalsRenderPipelines.PIPELINE_LINES_CULL, emptyParams());
    }
}
