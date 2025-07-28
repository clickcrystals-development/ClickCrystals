package io.github.itzispyder.clickcrystals.modules.modules.rendering.totemchams;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.totemchams.parts.ChamPart;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public abstract class ChamRagDoll<P extends ChamPart> implements Global {

    protected final Map<String, P> parts;
    protected final double x, y, z;
    protected final float yaw, pitch;
    protected float glidingProgress;
    protected final int maxAge;
    protected int age;

    public ChamRagDoll(PlayerEntity player, int maxAge) {
        this.maxAge = maxAge;
        this.parts = new HashMap<>();

        PlayerEntityRenderState state = (PlayerEntityRenderState) mc.getEntityRenderDispatcher().getRenderer(player).getAndUpdateRenderState(player, 1F);
        this.x = state.x;
        this.y = state.y;
        this.z = state.z;
        this.yaw = player.getYaw();
        this.pitch = player.getPitch();
        this.glidingProgress = state.isSwimming ? 1F : state.getGlidingProgress();
        initializeParts(player);
    }

    protected abstract void initializeParts(PlayerEntity player);

    public abstract void tick(float gravity, float maxVelocity);

    protected abstract void renderPart(P part, MatrixStack matrices, int color, float tickDelta);

    public void render(MatrixStack matrices, int color, float tickDelta) {
        Vector3f c = new Vec3d(x, y, z).subtract(mc.gameRenderer.getCamera().getCameraPos()).toVector3f();
        Quaternionf pitch = new Quaternionf().rotationX((float) Math.toRadians(glidingProgress == 1 ? this.pitch + 90 : glidingProgress * 90));
        Quaternionf yaw = new Quaternionf().rotationY((float) Math.toRadians(-this.yaw));

        matrices.push();
        matrices.multiply(yaw.mul(pitch), c.x, c.y, c.z);
        matrices.translate(c.x, c.y, c.z);

        double life = 1 - (age / (double) maxAge);
        int alpha = (int) ((color >> 24 & 0xFF) * life);
        color = (alpha << 24) | (color & 0x00FFFFFF);

        for (P part : parts.values())
            renderPart(part, matrices, color, tickDelta);

        matrices.pop();
    }

    public boolean isAlive() {
        return age < maxAge;
    }

    public float getAgeDelta() {
        return age / (float)maxAge;
    }
}