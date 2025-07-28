package io.github.itzispyder.clickcrystals.modules.modules.rendering.totemchams.parts;

import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils3d;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionf;

public class FadingChamPart extends ChamPart {

    public float fadeDelay, fadeSpeed;
    public boolean hasStartedFading;
    public boolean hasStartedMoving;

    public FadingChamPart(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float fadeDelay, float fadeSpeed) {
        super(minX, minY, minZ, maxX, maxY, maxZ);
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.fadeDelay = fadeDelay;
        this.fadeSpeed = fadeSpeed;
    }

    @Override
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

    @Override
    public void render(MatrixStack matrices, int color, float tickDelta, int age) {
        float globalProgress = age / 100.0f;
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
