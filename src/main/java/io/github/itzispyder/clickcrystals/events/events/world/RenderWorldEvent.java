package io.github.itzispyder.clickcrystals.events.events.world;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.itzispyder.clickcrystals.events.Event;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class RenderWorldEvent extends Event {

    private final PoseStack matrices;
    private final Matrix4f rotationMatrix, projectionMatrix;
    private final GameRenderer renderer;
    private final DeltaTracker tickCounter;

    public RenderWorldEvent(GameRenderer renderer, Matrix4f rotationMatrix, Matrix4f projectionMatrix, DeltaTracker tickCounter) {
        this.tickCounter = tickCounter;
        this.renderer = renderer;
        this.rotationMatrix = rotationMatrix;
        this.projectionMatrix = projectionMatrix;
        this.matrices = new PoseStack();
        this.matrices.mulPose(rotationMatrix);
    }

    public PoseStack getMatrices() {
        return matrices;
    }

    public DeltaTracker getTickCounter() {
        return tickCounter;
    }

    public GameRenderer getRenderer() {
        return renderer;
    }

    public Matrix4f getRotationMatrix() {
        return rotationMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Camera getCamera() {
        return renderer.getMainCamera();
    }

    public LocalPlayer getPlayer() {
        return PlayerUtils.player();
    }

    public Vec3 getOffsetPos(Vec3 pos) {
        return pos.subtract(getCamera().position());
    }

    public Vec3 getOffsetPos(BlockPos pos) {
        var c = getCamera().position();
        double x = pos.getX() - c.x;
        double y = pos.getY() - c.y;
        double z = pos.getZ() - c.z;
        return new Vec3(x, y, z);
    }

    public boolean valid() {
        return PlayerUtils.valid();
    }

    public boolean invalid() {
        return PlayerUtils.invalid();
    }
}