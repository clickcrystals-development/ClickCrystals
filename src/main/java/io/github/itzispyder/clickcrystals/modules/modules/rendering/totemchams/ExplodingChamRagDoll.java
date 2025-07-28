package io.github.itzispyder.clickcrystals.modules.modules.rendering.totemchams;

import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils3d;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Quaternionf;

public class ExplodingChamRagDoll extends BaseChamRagDoll<ExplodingChamRagDoll.Cuboid> {

    public ExplodingChamRagDoll(PlayerEntity player, int maxAge) {
        super(player, maxAge);
    }

    @Override
    protected void initializeParts(PlayerEntity player) {
        Cuboid head = new Cuboid(-BodyPartDimensions.B4, BodyPartDimensions.B12 + BodyPartDimensions.B12, -BodyPartDimensions.B4, BodyPartDimensions.B4, BodyPartDimensions.B12 + BodyPartDimensions.B12 + BodyPartDimensions.B8, BodyPartDimensions.B4);
        head.pitch = player.getPitch();

        parts.put("head", head);
        parts.put("bod", new Cuboid(-BodyPartDimensions.B4, BodyPartDimensions.B12, -BodyPartDimensions.B2, BodyPartDimensions.B4, BodyPartDimensions.B12 + BodyPartDimensions.B12, BodyPartDimensions.B2));
        parts.put("leftArm", new Cuboid(-BodyPartDimensions.B8, BodyPartDimensions.B12, -BodyPartDimensions.B2, -BodyPartDimensions.B4, BodyPartDimensions.B12 + BodyPartDimensions.B12, BodyPartDimensions.B2));
        parts.put("rightArm", new Cuboid(BodyPartDimensions.B8, BodyPartDimensions.B12, -BodyPartDimensions.B2, BodyPartDimensions.B4, BodyPartDimensions.B12 + BodyPartDimensions.B12, BodyPartDimensions.B2));
        parts.put("leftLeg", new Cuboid(-BodyPartDimensions.B4, BodyPartDimensions.B0, -BodyPartDimensions.B2, BodyPartDimensions.B0, BodyPartDimensions.B12, BodyPartDimensions.B2));
        parts.put("rightLeg", new Cuboid(BodyPartDimensions.B4, BodyPartDimensions.B0, -BodyPartDimensions.B2, BodyPartDimensions.B0, BodyPartDimensions.B12, BodyPartDimensions.B2));
    }

    @Override
    public void tick(float... params) {
        float maxVelocity = params.length > 0 ? params[0] : 0.5f;
        float gravity = params.length > 1 ? params[1] : 0.03f;

        for (Cuboid part : parts.values()) {
            if (age == 5) { // explode
                part.velX += (float) (0.1 + Math.random() * maxVelocity) * randomSign();
                part.velY += (float) (0.1 + Math.random() * maxVelocity);
                part.velZ += (float) (0.1 + Math.random() * maxVelocity) * randomSign();

                float[] dir = MathUtils.toPolar(part.velX, part.velY, part.velZ);
                part.velPitch = 0.2F;
                part.yaw = -dir[1];
            }
            part.tick(gravity);
        }
        age++;
    }

    @Override
    protected void renderPart(Cuboid part, MatrixStack matrices, int color, float tickDelta) {
        part.render(matrices, color, tickDelta);
    }

    private int randomSign() {
        return Math.random() > 0.5 ? 1 : -1;
    }

    public static class Cuboid {
        public float minX, minY, minZ, maxX, maxY, maxZ;
        public float x, y, z, prevX, prevY, prevZ, velX, velY, velZ;
        public float pitch, yaw, prevPitch, prevYaw, velPitch, velYaw;

        public Cuboid(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
            this.minX = minX;
            this.minY = minY;
            this.minZ = minZ;
            this.maxX = maxX;
            this.maxY = maxY;
            this.maxZ = maxZ;
        }

        public void tick(float gravity) {
            float airDrag = 0.99F;

            prevX = x;
            prevY = y;
            prevZ = z;

            x += velX;
            y += velY;
            z += velZ;

            velY -= gravity;

            velX *= airDrag;
            velY *= airDrag;
            velZ *= airDrag;

            // rotation
            prevPitch = pitch;
            prevYaw = yaw;

            pitch += velPitch * 90F;
            yaw += velYaw * 90F;

            velPitch *= 0.99F;
            velYaw *= 0.99F;
        }

        public boolean isMoving() {
            return Math.abs(velX) > 0.01 || Math.abs(velZ) > 0.01;
        }

        public Quaternionf getRotation(float tickDelta) {
            float pitch = (float) MathUtils.lerp(prevPitch, this.pitch, tickDelta);
            float yaw = (float) MathUtils.lerp(prevYaw, this.yaw, tickDelta);

            Quaternionf qPitch = new Quaternionf().rotationX((float) Math.toRadians(pitch));
            Quaternionf qYaw = new Quaternionf().rotationY((float) Math.toRadians(yaw));
            return qYaw.mul(qPitch);
        }

        public void render(MatrixStack matrices, int color, float tickDelta) {
            float x = (float) MathUtils.lerp(prevX, this.x, tickDelta);
            float y = (float) MathUtils.lerp(prevY, this.y, tickDelta);
            float z = (float) MathUtils.lerp(prevZ, this.z, tickDelta);

            float cx = minX + x + (maxX - minX) / 2;
            float cy = minY + y + (maxY - minY) / 2;
            float cz = minZ + z + (maxZ - minZ) / 2;

            matrices.push();
            matrices.multiply(getRotation(tickDelta), cx, cy, cz);
            matrices.translate(x, y, z);

            RenderUtils3d.fillRectPrism(matrices, minX, minY, minZ, maxX, maxY, maxZ, color, true);

            int alpha = Math.min((color >> 24 & 0xFF) * 5, 0xFF);
            color = (alpha << 24) | (color & 0x00FFFFFF);
            RenderUtils3d.drawRectPrism(matrices, minX, minY, minZ, maxX, maxY, maxZ, color, true);

            matrices.pop();
        }
    }
}