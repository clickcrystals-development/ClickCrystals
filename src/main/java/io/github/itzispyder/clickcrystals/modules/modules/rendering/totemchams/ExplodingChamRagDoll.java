package io.github.itzispyder.clickcrystals.modules.modules.rendering.totemchams;

import io.github.itzispyder.clickcrystals.modules.modules.rendering.totemchams.parts.ChamPart;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.totemchams.parts.ExplodingChamPart;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;

public class ExplodingChamRagDoll extends ChamRagDoll<ExplodingChamPart> {

    public ExplodingChamRagDoll(PlayerEntity player, int maxAge) {
        super(player, maxAge);
    }

    @Override
    protected void initializeParts(PlayerEntity player) {
        ExplodingChamPart head = new ExplodingChamPart(-ChamPart.B4, ChamPart.B12 + ChamPart.B12, -ChamPart.B4, ChamPart.B4, ChamPart.B12 + ChamPart.B12 + ChamPart.B8, ChamPart.B4);
        head.pitch = player.getPitch();

        parts.put("head", head);
        parts.put("bod", new ExplodingChamPart(-ChamPart.B4, ChamPart.B12, -ChamPart.B2, ChamPart.B4, ChamPart.B12 + ChamPart.B12, ChamPart.B2));
        parts.put("leftArm", new ExplodingChamPart(-ChamPart.B8, ChamPart.B12, -ChamPart.B2, -ChamPart.B4, ChamPart.B12 + ChamPart.B12, ChamPart.B2));
        parts.put("rightArm", new ExplodingChamPart(ChamPart.B8, ChamPart.B12, -ChamPart.B2, ChamPart.B4, ChamPart.B12 + ChamPart.B12, ChamPart.B2));
        parts.put("leftLeg", new ExplodingChamPart(-ChamPart.B4, ChamPart.B0, -ChamPart.B2, ChamPart.B0, ChamPart.B12, ChamPart.B2));
        parts.put("rightLeg", new ExplodingChamPart(ChamPart.B4, ChamPart.B0, -ChamPart.B2, ChamPart.B0, ChamPart.B12, ChamPart.B2));
    }

    @Override
    public void tick(float gravity, float maxVelocity) {
        float ageDelta = getAgeDelta();

        for (ExplodingChamPart part : parts.values()) {
            if (age == 5) { // explode
                part.velX += (float) (0.1 + Math.random() * maxVelocity) * randomSign();
                part.velY += (float) (0.1 + Math.random() * maxVelocity);
                part.velZ += (float) (0.1 + Math.random() * maxVelocity) * randomSign();

                float[] dir = MathUtils.toPolar(part.velX, part.velY, part.velZ);
                part.velPitch = 0.2F;
                part.yaw = -dir[1];
            }
            part.tick(gravity, ageDelta);
        }
        age++;
    }

    @Override
    protected void renderPart(ExplodingChamPart part, MatrixStack matrices, int color, float tickDelta) {
        part.render(matrices, color, tickDelta, age);
    }

    private int randomSign() {
        return Math.random() > 0.5 ? 1 : -1;
    }
}