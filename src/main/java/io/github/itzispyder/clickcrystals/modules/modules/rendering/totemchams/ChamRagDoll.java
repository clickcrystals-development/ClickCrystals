package io.github.itzispyder.clickcrystals.modules.modules.rendering.totemchams;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.totemchams.parts.ChamPart;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
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

    public ChamRagDoll(Player player, int maxAge) {
        this.maxAge = maxAge;
        this.parts = new HashMap<>();

        AvatarRenderState state = (AvatarRenderState) mc.getEntityRenderDispatcher().getRenderer(player).createRenderState(player, 1F);
        this.x = state.x;
        this.y = state.y;
        this.z = state.z;
        this.yaw = player.getYRot();
        this.pitch = player.getXRot();
        this.glidingProgress = state.isVisuallySwimming ? 1F : state.fallFlyingScale();
        initializeParts(player);
    }

    protected abstract void initializeParts(Player player);

    public abstract void tick(float gravity, float maxVelocity);

    protected abstract void renderPart(P part, PoseStack matrices, int color, float tickDelta);

    public void render(PoseStack matrices, int color, float tickDelta) {
        Vector3f c = new Vec3(x, y, z).subtract(mc.gameRenderer.getMainCamera().position()).toVector3f();
        Quaternionf pitch = new Quaternionf().rotationX((float) Math.toRadians(glidingProgress == 1 ? this.pitch + 90 : glidingProgress * 90));
        Quaternionf yaw = new Quaternionf().rotationY((float) Math.toRadians(-this.yaw));

        matrices.pushPose();
        matrices.rotateAround(yaw.mul(pitch), c.x, c.y, c.z);
        matrices.translate(c.x, c.y, c.z);

        double life = 1 - (age / (double) maxAge);
        int alpha = (int) ((color >> 24 & 0xFF) * life);
        color = (alpha << 24) | (color & 0x00FFFFFF);

        for (P part : parts.values())
            renderPart(part, matrices, color, tickDelta);

        matrices.popPose();
    }

    public boolean isAlive() {
        return age < maxAge;
    }

    public float getAgeDelta() {
        return age / (float)maxAge;
    }
}