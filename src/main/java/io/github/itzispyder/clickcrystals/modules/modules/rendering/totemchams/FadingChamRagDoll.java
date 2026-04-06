package io.github.itzispyder.clickcrystals.modules.modules.rendering.totemchams;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.totemchams.parts.ChamPart;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.totemchams.parts.FadingChamPart;
import net.minecraft.world.entity.player.Player;

public class FadingChamRagDoll extends ChamRagDoll<FadingChamPart> {

    public FadingChamRagDoll(Player player, int maxAge) {
        super(player, maxAge);
    }

    @Override
    protected void initializeParts(Player player) {
        FadingChamPart head = new FadingChamPart(-ChamPart.B4, ChamPart.B12 + ChamPart.B12, -ChamPart.B4, ChamPart.B4, ChamPart.B12 + ChamPart.B12 + ChamPart.B8, ChamPart.B4, 0.05f, 0.8f);
        head.pitch = player.getXRot();

        parts.put("head", head);
        parts.put("bod", new FadingChamPart(-ChamPart.B4, ChamPart.B12, -ChamPart.B2, ChamPart.B4, ChamPart.B12 + ChamPart.B12, ChamPart.B2, 0.1f, 0.7f));
        parts.put("leftArm", new FadingChamPart(-ChamPart.B8, ChamPart.B12, -ChamPart.B2, -ChamPart.B4, ChamPart.B12 + ChamPart.B12, ChamPart.B2, 0.15f, 0.6f));
        parts.put("rightArm", new FadingChamPart(ChamPart.B8, ChamPart.B12, -ChamPart.B2, ChamPart.B4, ChamPart.B12 + ChamPart.B12, ChamPart.B2, 0.15f, 0.6f));
        parts.put("leftLeg", new FadingChamPart(-ChamPart.B4, ChamPart.B0, -ChamPart.B2, ChamPart.B0, ChamPart.B12, ChamPart.B2, 0.2f, 0.5f));
        parts.put("rightLeg", new FadingChamPart(ChamPart.B4, ChamPart.B0, -ChamPart.B2, ChamPart.B0, ChamPart.B12, ChamPart.B2, 0.2f, 0.5f));
    }

    @Override
    public void tick(float gravity, float maxVelocity) {
        float ageDelta = getAgeDelta();
        for (FadingChamPart part : parts.values())
            part.tick(gravity, ageDelta);
        age++;
    }

    @Override
    protected void renderPart(FadingChamPart part, PoseStack matrices, int color, float tickDelta) {
        part.render(matrices, color, tickDelta, age);
    }
}