package io.github.itzispyder.clickcrystals.modules.modules.rendering.totemchams.parts;

import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils3d;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionf;

public class ExplodingChamPart extends ChamPart {

    public ExplodingChamPart(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        super(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public void tick(float gravity, float ageDelta) {
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

    @Override
    public void render(MatrixStack matrices, int color, float tickDelta, int age) {
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
