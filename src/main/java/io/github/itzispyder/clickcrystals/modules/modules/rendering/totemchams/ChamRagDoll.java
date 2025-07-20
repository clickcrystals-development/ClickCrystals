package io.github.itzispyder.clickcrystals.modules.modules.rendering.totemchams;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils3d;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class ChamRagDoll implements Global {

    private final Map<String, Cuboid> parts;
    private final double x, y, z;
    private final float yaw, pitch;
    private float glidingProgress;
    private final int maxAge;
    private int age;

    public ChamRagDoll(PlayerEntity player, int maxAge) {
        this.maxAge = maxAge;
        this.parts = new HashMap<>();

        PlayerEntityRenderState state = (PlayerEntityRenderState) mc.getEntityRenderDispatcher().getRenderer(player).getAndUpdateRenderState(player, 1F);
        this.x = state.x;
        this.y = state.y;
        this.z = state.z;
        this.yaw = player.getYaw();
        this.pitch = player.getPitch();
        this.glidingProgress = state.getGlidingProgress();

        if (state.isSwimming)
            glidingProgress = 1F;

        float pixelsToBlocks = 0.05625F; // I used a calculator for this (1.8 / 32) or (player height blocks / player height pixels)
        float b12 = 12 * pixelsToBlocks;
        float b2 = 2 * pixelsToBlocks;
        float b4 = 4 * pixelsToBlocks;
        float b8 = 8 * pixelsToBlocks;
        float b0 = 0 * pixelsToBlocks;

        Cuboid head = new Cuboid(-b4, b12 + b12, -b4, b4, b12 + b12 + b8, b4);
        head.pitch = player.getPitch();

        parts.put("head", head);
        parts.put("bod", new Cuboid(-b4, b12, -b2, b4, b12 + b12, b2));
        parts.put("leftArm", new Cuboid(-b8, b12, -b2, -b4, b12 + b12, b2));
        parts.put("rightArm", new Cuboid(b8, b12, -b2, b4, b12 + b12, b2));
        parts.put("leftLeg", new Cuboid(-b4, b0, -b2, b0, b12, b2));
        parts.put("rightLeg", new Cuboid(b4, b0, -b2, b0, b12, b2));
    }

    public void tick(float maxVelocity, float gravity) {
        for (Cuboid part: parts.values()) {
            if (age == 5) { // explode
                part.velX += (float)(0.1 + Math.random() * maxVelocity) * randomSign();
                part.velY += (float)(0.1 + Math.random() * maxVelocity);
                part.velZ += (float)(0.1 + Math.random() * maxVelocity) * randomSign();

                float[] dir = MathUtils.toPolar(part.velX, part.velY, part.velZ);
                part.velPitch = 0.2F;
                part.yaw = -dir[1];
            }
            part.tick(gravity);
        }
        age++;
    }

    private int randomSign() {
        return Math.random() > 0.5 ? 1 : -1;
    }

    public void render(MatrixStack matrices, int color, float tickDelta) {
        Vector3f c = new Vec3d(x, y, z).subtract(mc.gameRenderer.getCamera().getCameraPos()).toVector3f();
        Quaternionf pitch = new Quaternionf()
                .rotationX((float)Math.toRadians(glidingProgress == 1 ? this.pitch + 90 : glidingProgress * 90));
        Quaternionf yaw = new Quaternionf()
                .rotationY((float)Math.toRadians(-this.yaw));

        matrices.push();
        matrices.multiply(yaw.mul(pitch), c.x, c.y, c.z);
        matrices.translate(c.x, c.y, c.z);

        double life = 1 - (age / (double) maxAge);
        int alpha = (int) ((color >> 24 & 0xFF) * life);
        color = (alpha << 24) | (color & 0x00FFFFFF);

        for (Cuboid part : parts.values())
            part.render(matrices, color, tickDelta);

        matrices.pop();
    }

    public boolean isAlive() {
        return age < maxAge;
    }

    private static class Cuboid {
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
