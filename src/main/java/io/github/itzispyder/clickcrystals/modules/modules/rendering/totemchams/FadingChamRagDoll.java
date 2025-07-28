package io.github.itzispyder.clickcrystals.modules.modules.rendering.totemchams;

import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils3d;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Quaternionf;

public class FadingChamRagDoll extends BaseChamRagDoll<FadingChamRagDoll.FadingCuboid> {

    public FadingChamRagDoll(PlayerEntity player, int maxAge) {
        super(player, maxAge);
    }

    @Override
    protected void initializeParts(PlayerEntity player) {
        FadingCuboid head = new FadingCuboid(-BodyPartDimensions.B4, BodyPartDimensions.B12 + BodyPartDimensions.B12, -BodyPartDimensions.B4, BodyPartDimensions.B4, BodyPartDimensions.B12 + BodyPartDimensions.B12 + BodyPartDimensions.B8, BodyPartDimensions.B4, 0.05f, 0.8f);
        head.pitch = player.getPitch();

        parts.put("head", head);
        parts.put("bod", new FadingCuboid(-BodyPartDimensions.B4, BodyPartDimensions.B12, -BodyPartDimensions.B2, BodyPartDimensions.B4, BodyPartDimensions.B12 + BodyPartDimensions.B12, BodyPartDimensions.B2, 0.1f, 0.7f));
        parts.put("leftArm", new FadingCuboid(-BodyPartDimensions.B8, BodyPartDimensions.B12, -BodyPartDimensions.B2, -BodyPartDimensions.B4, BodyPartDimensions.B12 + BodyPartDimensions.B12, BodyPartDimensions.B2, 0.15f, 0.6f));
        parts.put("rightArm", new FadingCuboid(BodyPartDimensions.B8, BodyPartDimensions.B12, -BodyPartDimensions.B2, BodyPartDimensions.B4, BodyPartDimensions.B12 + BodyPartDimensions.B12, BodyPartDimensions.B2, 0.15f, 0.6f));
        parts.put("leftLeg", new FadingCuboid(-BodyPartDimensions.B4, BodyPartDimensions.B0, -BodyPartDimensions.B2, BodyPartDimensions.B0, BodyPartDimensions.B12, BodyPartDimensions.B2, 0.2f, 0.5f));
        parts.put("rightLeg", new FadingCuboid(BodyPartDimensions.B4, BodyPartDimensions.B0, -BodyPartDimensions.B2, BodyPartDimensions.B0, BodyPartDimensions.B12, BodyPartDimensions.B2, 0.2f, 0.5f));
    }

    @Override
    public void tick(float... params) {
        float gravity = params.length > 0 ? params[0] : 0.03f;

        for (FadingCuboid part : parts.values()) {
            part.tick(gravity, age / (float) maxAge);
        }
        age++;
    }

    @Override
    protected void renderPart(FadingCuboid part, MatrixStack matrices, int color, float tickDelta) {
        part.render(matrices, color, tickDelta, age);
    }

    public static class FadingCuboid {
        public float minX, minY, minZ, maxX, maxY, maxZ;
        public float x, y, z, prevX, prevY, prevZ, velX, velY, velZ;
        public float pitch, yaw, prevPitch, prevYaw, velPitch, velYaw;
        public float fadeDelay, fadeSpeed;
        public boolean hasStartedFading = false;
        public boolean hasStartedMoving = false;

        public FadingCuboid(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float fadeDelay, float fadeSpeed) {
            this.minX = minX;
            this.minY = minY;
            this.minZ = minZ;
            this.maxX = maxX;
            this.maxY = maxY;
            this.maxZ = maxZ;
            this.fadeDelay = fadeDelay;
            this.fadeSpeed = fadeSpeed;
        }

        public void tick(float gravity, float globalProgress) {
            float airDrag = 0.98F;
            float movementDelay = fadeDelay * 0.3f;

            if (globalProgress > movementDelay && !hasStartedMoving) {
                hasStartedMoving = true;

                float heightFactor = (maxY + minY) / 2;
                float horizontalBoost = Math.max(0.5f, heightFactor / 12f);

                velX = (float) (Math.random() - 0.5) * 0.025f * horizontalBoost;
                velZ = (float) (Math.random() - 0.5) * 0.025f * horizontalBoost;
                velY = (float) Math.random() * 0.015f + 0.005f;

                velPitch = (float) (Math.random() - 0.5) * 0.8f;
                velYaw = (float) (Math.random() - 0.5) * 0.8f;
            }

            if (globalProgress > fadeDelay && !hasStartedFading) {
                hasStartedFading = true;
            }

            if (hasStartedMoving) {
                prevX = x;
                prevY = y;
                prevZ = z;

                x += velX;
                y += velY;
                z += velZ;

                float gravityMultiplier = Math.min(2.0f, 1.0f + globalProgress);
                velY -= gravity * 0.3f * gravityMultiplier;

                velX *= airDrag;
                velY *= Math.max(0.95f, airDrag);
                velZ *= airDrag;

                prevPitch = pitch;
                prevYaw = yaw;

                pitch += velPitch * 25F;
                yaw += velYaw * 25F;

                velPitch *= 0.985F;
                velYaw *= 0.985F;

                if (Math.random() < 0.02) {
                    velPitch += (float) (Math.random() - 0.5) * 0.2f;
                    velYaw += (float) (Math.random() - 0.5) * 0.2f;
                }
            }
        }

        public boolean isVisible(float globalProgress) {
            if (!hasStartedFading) return true;

            float adjustedFadeSpeed = fadeSpeed;
            float fadeProgress = Math.min(1.0f, (globalProgress - fadeDelay) * adjustedFadeSpeed);
            return fadeProgress < 1.0f;
        }

        public float getFadeAlpha(float globalProgress) {
            if (!hasStartedFading) return 1.0f;

            float adjustedFadeSpeed = fadeSpeed;
            float fadeProgress = Math.min(1.0f, (globalProgress - fadeDelay) * adjustedFadeSpeed);

            fadeProgress = fadeProgress * fadeProgress * (3.0f - 2.0f * fadeProgress);

            return Math.max(0.0f, 1.0f - fadeProgress);
        }

        public Quaternionf getRotation(float tickDelta) {
            float pitch = (float) MathUtils.lerp(prevPitch, this.pitch, tickDelta);
            float yaw = (float) MathUtils.lerp(prevYaw, this.yaw, tickDelta);

            Quaternionf qPitch = new Quaternionf().rotationX((float) Math.toRadians(pitch));
            Quaternionf qYaw = new Quaternionf().rotationY((float) Math.toRadians(yaw));
            return qYaw.mul(qPitch);
        }

        public void render(MatrixStack matrices, int color, float tickDelta, int currentAge) {
            float globalProgress = currentAge / 100.0f;
            if (!isVisible(globalProgress)) return;

            float x = (float) MathUtils.lerp(prevX, this.x, tickDelta);
            float y = (float) MathUtils.lerp(prevY, this.y, tickDelta);
            float z = (float) MathUtils.lerp(prevZ, this.z, tickDelta);

            float cx = minX + x + (maxX - minX) / 2;
            float cy = minY + y + (maxY - minY) / 2;
            float cz = minZ + z + (maxZ - minZ) / 2;

            matrices.push();
            matrices.multiply(getRotation(tickDelta), cx, cy, cz);
            matrices.translate(x, y, z);

            float fadeAlpha = getFadeAlpha(globalProgress);
            int fadeColor = ((int) (((color >> 24) & 0xFF) * fadeAlpha) << 24) | (color & 0x00FFFFFF);

            RenderUtils3d.fillRectPrism(matrices, minX, minY, minZ, maxX, maxY, maxZ, fadeColor, true);

            int outlineAlpha = Math.min((int) (((color >> 24) & 0xFF) * fadeAlpha * 1.3f), 0xFF);
            int outlineColor = (outlineAlpha << 24) | (color & 0x00FFFFFF);
            RenderUtils3d.drawRectPrism(matrices, minX, minY, minZ, maxX, maxY, maxZ, outlineColor, true);

            matrices.pop();
        }
    }
}