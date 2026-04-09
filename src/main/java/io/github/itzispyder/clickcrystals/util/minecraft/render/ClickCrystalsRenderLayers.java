package io.github.itzispyder.clickcrystals.util.minecraft.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

import java.util.function.Function;

public class ClickCrystalsRenderLayers {

    public static final RenderType LINES;
    public static final RenderType LINES_STRIP;
    public static final RenderType QUADS;
    public static final RenderType TRI_FAN;
    public static final RenderType TRI_STRIP;
    public static final Function<Identifier, RenderType> TEX_QUADS;
    public static final Function<Identifier, RenderType> TEX_TRI_FAN;
    public static final RenderType TRI_STRIP_CULL;
    public static final RenderType QUADS_CULL;
    public static final RenderType LINES_CULL;

    private static RenderSetup emptyParams(RenderPipeline pipeline) {
        return RenderSetup.builder(pipeline)
                .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
                .createRenderSetup();
    }

    private static RenderSetup textureParams(RenderPipeline pipeline, Identifier id) {
        return RenderSetup.builder(pipeline)
                .withTexture("Sampler0", id, () -> RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST))
                .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
                .createRenderSetup();
    }

    static {
        LINES = RenderType.create("cc_layer_lines", emptyParams(ClickCrystalsRenderPipelines.PIPELINE_LINES));
        LINES_STRIP = RenderType.create("cc_layer_lines_strip", emptyParams(ClickCrystalsRenderPipelines.PIPELINE_LINES_STRIP));
        QUADS = RenderType.create("cc_layer_quads", emptyParams(ClickCrystalsRenderPipelines.PIPELINE_QUADS));
        TRI_FAN = RenderType.create("cc_layer_tri_fan", emptyParams(ClickCrystalsRenderPipelines.PIPELINE_TRI_FAN));
        TRI_STRIP = RenderType.create("cc_layer_tri_strip", emptyParams(ClickCrystalsRenderPipelines.PIPELINE_TRI_STRIP));
        TEX_QUADS = id -> RenderType.create("cc_layer_tex_quad", textureParams(ClickCrystalsRenderPipelines.PIPELINE_TEX_QUADS, id));
        TEX_TRI_FAN = id -> RenderType.create("cc_layer_tex_tri_fan", textureParams(ClickCrystalsRenderPipelines.PIPELINE_TEX_TRI_FAN, id));
        TRI_STRIP_CULL = RenderType.create("cc_layer_tri_strip_cull", emptyParams(ClickCrystalsRenderPipelines.PIPELINE_TRI_STRIP_CULL));
        QUADS_CULL = RenderType.create("cc_layer_quads_cull", emptyParams(ClickCrystalsRenderPipelines.PIPELINE_QUADS_CULL));
        LINES_CULL = RenderType.create("cc_layer_lines_cull", emptyParams(ClickCrystalsRenderPipelines.PIPELINE_LINES_CULL));
    }
}