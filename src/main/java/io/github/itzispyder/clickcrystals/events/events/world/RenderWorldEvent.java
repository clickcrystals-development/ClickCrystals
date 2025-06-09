package io.github.itzispyder.clickcrystals.events.events.world;

import io.github.itzispyder.clickcrystals.events.Event;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class RenderWorldEvent extends Event {

    private final MatrixStack matrices;
    private final Matrix4f rotationMatrix, projectionMatrix;
    private final GameRenderer renderer;
    private final RenderTickCounter tickCounter;

    public RenderWorldEvent(GameRenderer renderer, Matrix4f rotationMatrix, Matrix4f projectionMatrix, RenderTickCounter tickCounter) {
        this.tickCounter = tickCounter;
        this.renderer = renderer;
        this.rotationMatrix = rotationMatrix;
        this.projectionMatrix = projectionMatrix;
        this.matrices = new MatrixStack();
        this.matrices.multiplyPositionMatrix(rotationMatrix);
    }

    public MatrixStack getMatrices() {
        return matrices;
    }

    public RenderTickCounter getTickCounter() {
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
        return renderer.getCamera();
    }

    public ClientPlayerEntity getPlayer() {
        return PlayerUtils.player();
    }

    public Vec3d getOffsetPos(Vec3d pos) {
        return pos.subtract(getCamera().getPos());
    }

    public Vec3d getOffsetPos(BlockPos pos) {
        var c = getCamera().getPos();
        double x = pos.getX() - c.x;
        double y = pos.getY() - c.y;
        double z = pos.getZ() - c.z;
        return new Vec3d(x, y, z);
    }

    public boolean valid() {
        return PlayerUtils.valid();
    }

    public boolean invalid() {
        return PlayerUtils.invalid();
    }
}