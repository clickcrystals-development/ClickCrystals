package io.github.itzispyder.clickcrystals.modules.modules.rendering.totemchams.parts;

import net.minecraft.client.util.math.MatrixStack;

public abstract class ChamPart {

    public static final float PIXELS_TO_BLOCKS = 0.05625F; // 1.8 / 32
    public static final float B0 = 0 * PIXELS_TO_BLOCKS;
    public static final float B2 = 2 * PIXELS_TO_BLOCKS;
    public static final float B4 = 4 * PIXELS_TO_BLOCKS;
    public static final float B8 = 8 * PIXELS_TO_BLOCKS;
    public static final float B12 = 12 * PIXELS_TO_BLOCKS;

    public float minX, minY, minZ, maxX, maxY, maxZ;
    public float x, y, z, prevX, prevY, prevZ, velX, velY, velZ;
    public float pitch, yaw, prevPitch, prevYaw, velPitch, velYaw;

    protected ChamPart(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public abstract void tick(float gravity, float ageDelta);

    public abstract void render(MatrixStack matrices, int color, float tickDelta, int age);
}
