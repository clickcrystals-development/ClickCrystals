package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.world.RenderWorldEvent;
import io.github.itzispyder.clickcrystals.gui.misc.Color;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class ElytraShadow extends ListenerModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Color> color = scGeneral.add(createColorSetting()
            .name("shadow-color")
            .description("Color of the shadow")
            .def(0xFF00B7FF)
            .build());

    public ElytraShadow() {
        super("elytra-shadow", Categories.RENDER, "Renders a shadow directly below a gliding player on the ground.");
    }

    @EventHandler
    public void onWorldRender(RenderWorldEvent event) {
        Color color = this.color.getVal();
        PoseStack matrices = event.getPoseStack();
        SubmitNodeCollector submitNodeCollector = event.getSubmitNodeCollector();
        float tickDelta = event.getDeltaTracker().getGameTimeDeltaPartialTick(true);

        for (AbstractClientPlayer player : PlayerUtils.getClientWorld().players())
            if (player.isFallFlying())
                renderPlayerShadow(matrices, submitNodeCollector, event.getCamera(), MathUtils.lerpEntityPosVec(player, tickDelta), color);
    }

    private void renderPlayerShadow(PoseStack matrices, SubmitNodeCollector submitNodeCollector, Vec3 cameraPosition, Vec3 playerPosition, Color color) {
        submitNodeCollector.submitCustomGeometry(matrices, RenderLayers.QUADS, (pose, buf) -> {
            Matrix4f mat = pose.pose();
            float rad = 1F;
            float dTheta = Mth.PI / 16; // i chose this because mc pixels are 1/16 of a block, if ur computer lags womp womp
            float dRad = rad / 16;

            for (float i = 0; i < Mth.TWO_PI; i += dTheta) {
                for (float r = dRad; r <= rad; r += dRad) {
                    Vec3 pnt1 = castShadowVertex(playerPosition.add(r * Mth.cos(i), 0, r * Mth.sin(i)));
                    Vec3 pnt2 = castShadowVertex(playerPosition.add((r + dRad) * Mth.cos(i), 0, (r + dRad) * Mth.sin(i)));
                    Vec3 pnt3 = castShadowVertex(playerPosition.add((r + dRad) * Mth.cos(i + dTheta), 0, (r + dRad) * Mth.sin(i + dTheta)));
                    Vec3 pnt4 = castShadowVertex(playerPosition.add(r * Mth.cos(i + dTheta), 0, r * Mth.sin(i + dTheta)));

                    if (pnt1 == null || pnt2 == null || pnt3 == null || pnt4 == null)
                        continue;

                    pnt1 = pnt1.subtract(cameraPosition);
                    pnt2 = pnt2.subtract(cameraPosition);
                    pnt3 = pnt3.subtract(cameraPosition);
                    pnt4 = pnt4.subtract(cameraPosition);
                    buf.addVertex(mat, (float) pnt1.x, (float) pnt1.y, (float) pnt1.z).setColor(color.getHexCustomAlpha(r / rad));
                    buf.addVertex(mat, (float) pnt2.x, (float) pnt2.y, (float) pnt2.z).setColor(color.getHexCustomAlpha((r + dRad) / rad));
                    buf.addVertex(mat, (float) pnt3.x, (float) pnt3.y, (float) pnt3.z).setColor(color.getHexCustomAlpha((r + dRad) / rad));
                    buf.addVertex(mat, (float) pnt4.x, (float) pnt4.y, (float) pnt4.z).setColor(color.getHexCustomAlpha(r / rad));
                }
            }
        });
    }

    private Vec3 castShadowVertex(Vec3 pnt) {
        Level world = PlayerUtils.getWorld();
        double y = pnt.y + 0.001;

        if (!world.getBlockState(BlockPos.containing(pnt.x, y, pnt.z)).isAir())
            return null; // already in a block (no shadow calculations)

        while (y > -64 && world.getBlockState(BlockPos.containing(pnt.x, y, pnt.z)).isAir())
            y--;

        if (world.getBlockState(BlockPos.containing(pnt.x, y, pnt.z)).isAir())
            return null; // no blocks to cast shadow on
        else
            return new Vec3(pnt.x, Math.ceil(y) + 0.001, pnt.z); // adding 0.001 to prevent z-fighting
    }
}
