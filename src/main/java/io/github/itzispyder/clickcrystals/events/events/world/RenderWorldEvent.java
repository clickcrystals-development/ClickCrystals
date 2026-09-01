package io.github.itzispyder.clickcrystals.events.events.world;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class RenderWorldEvent extends Event {

    private final PoseStack poseStack;
    private final Vec3 camera;
    private final DeltaTracker deltaTracker;
    private final SubmitNodeCollector submitNodeCollector;

    public RenderWorldEvent(PoseStack poseStack, LevelRenderState levelRenderState, DeltaTracker deltaTracker, SubmitNodeCollector submitNodeCollector) {
        this.poseStack = poseStack;
        this.camera = levelRenderState.cameraRenderState.pos;
        this.deltaTracker = deltaTracker;
        this.submitNodeCollector = submitNodeCollector;
    }

    public PoseStack getPoseStack() {
        return poseStack;
    }

    public DeltaTracker getDeltaTracker() {
        return deltaTracker;
    }

    public SubmitNodeCollector getSubmitNodeCollector() {
        return submitNodeCollector;
    }

    public Vec3 getCamera() {
        return camera;
    }

    public Vec3 getCameraRelativePosition(Vec3 position) {
        return position.subtract(camera);
    }

    public Vec3 getCameraRelativePosition(BlockPos blockPos) {
        double x = blockPos.getX() - camera.x;
        double y = blockPos.getY() - camera.y;
        double z = blockPos.getZ() - camera.z;
        return new Vec3(x, y, z);
    }
}
