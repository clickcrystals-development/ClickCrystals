package io.github.itzispyder.clickcrystals.modules.modules.rendering.totemchams;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils3d;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.Map;

public class RagDoll implements Global {

    private final Map<String, Cuboid> parts;
    private final double x, y, z;
    private final int maxAge;
    private int age;

    public RagDoll(PlayerEntity player, int maxAge) {
        this.maxAge = maxAge;
        this.parts = new HashMap<>();

        PlayerEntityRenderState state = (PlayerEntityRenderState) mc.getEntityRenderDispatcher().getRenderer(player).getAndUpdateRenderState(player, 1F);
        this.x = state.x;
        this.y = state.y;
        this.z = state.z;

        float pixelsToBlocks = 0.05625F; // I used a calculator for this (1.8 / 32) or (player height blocks / player height pixels)
        float b12 = 12 * pixelsToBlocks;
        float b2 = 2 * pixelsToBlocks;
        float b4 = 4 * pixelsToBlocks;
        float b8 = 8 * pixelsToBlocks;
        float b0 = 0 * pixelsToBlocks;

        parts.put("head", new Cuboid(-b4, b12 + b12, -b4, b4, b12 + b12 + b8, b4));
        parts.put("bod", new Cuboid(-b4, b12, -b2, b4, b12 + b12, b2));
        parts.put("leftArm", new Cuboid(-b8, b12, -b2, -b4, b12 + b12, b2));
        parts.put("rightArm", new Cuboid(b8, b12, -b2, b4, b12 + b12, b2));
        parts.put("leftLeg", new Cuboid(-b4, b0, -b2, b0, b12, b2));
        parts.put("rightLeg", new Cuboid(b4, b0, -b2, b0, b12, b2));
    }

    public void tick() {
        for (Cuboid part: parts.values()) {
            if (age == 1) { // shoot up
                part.velX = (float)(Math.random() * 0.1) * randomSign();
                part.velY = (float)(1 + Math.random() * 0.3);
                part.velZ = (float)(Math.random() * 0.1) * randomSign();
            }
            if (age == 5) { // explode
                part.velX += (float)(0.2 + Math.random() * 0.3) * randomSign();
                part.velZ += (float)(0.2 + Math.random() * 0.3) * randomSign();
                part.velPitch += (float)(Math.random() * 0.3) * randomSign();
                part.velYaw += (float)(Math.random() * 0.3) * randomSign();
            }
            part.tick();
        }
        age++;
    }

    private int randomSign() {
        return Math.random() > 0.5 ? 1 : -1;
    }

    public void render(MatrixStack matrices, int color, float tickDelta) {
        Vec3d c = mc.gameRenderer.getCamera().getCameraPos();
        double life = 1 - age / (double) maxAge;
        color = ((int)(0x40 * life) << 24) | (color & 0x00FFFFFF);

        matrices.push();
        matrices.translate(x - c.x, y - c.y, z - c.z);

        for (Cuboid part: parts.values())
            part.render(matrices, color, tickDelta);

        matrices.pop();
    }

    public boolean isAlive() {
        if (age < maxAge)
            return true;

        for (Cuboid part: parts.values())
            if (part.isMoving())
                return true;
        return false;
    }

    private static class Cuboid {
        public float x1, y1, z1, x2, y2, z2;
        public float x, y, z, prevX, prevY, prevZ, velX, velY, velZ;
        public float pitch, yaw, prevPitch, prevYaw, velPitch, velYaw;

        public Cuboid(float x1, float y1, float z1, float x2, float y2, float z2) {
            this.x1 = x1;
            this.y1 = y1;
            this.z1 = z1;
            this.x2 = x2;
            this.y2 = y2;
            this.z2 = z2;
        }

        public void tick() {
            float gravity = 0.05F;
            float airDrag = 0.8F;

            // position

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
            float pitch = (float)MathUtils.lerp(prevPitch, this.pitch, tickDelta);
            float yaw = (float)MathUtils.lerp(prevYaw, this.yaw, tickDelta);

            Quaternionf qPitch = new Quaternionf().rotationX((float)Math.toRadians(pitch));
            Quaternionf qYaw = new Quaternionf().rotationY((float)Math.toRadians(yaw));
            return qYaw.mul(qPitch);
        }

        public void render(MatrixStack matrices, int color, float tickDelta) {
            float x = (float)MathUtils.lerp(prevX, this.x, tickDelta);
            float y = (float)MathUtils.lerp(prevY, this.y, tickDelta);
            float z = (float)MathUtils.lerp(prevZ, this.z, tickDelta);

            float cx = x + (x2 - x1) / 2;
            float cy = y + (y2 - y1) / 2;
            float cz = z + (z2 - z1) / 2;

            matrices.push();
            matrices.multiply(getRotation(tickDelta), cx, cy, cz);
            matrices.translate(x, y, z);

            RenderUtils3d.fillRectPrism(matrices, x1, y1, z1, x2, y2, z2, color);
            color = 0xFF000000 | (0x00FFFFFF & color);
            RenderUtils3d.drawRectPrism(matrices, x1, y1, z1, x2, y2, z2, color);

            matrices.pop();
        }
    }
}
