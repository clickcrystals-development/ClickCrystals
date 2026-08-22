package io.github.itzispyder.clickcrystals.modules.modules.rendering.trajectories;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.MissHitResult;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils3d;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProjectilePath implements Global {

    private double airDrag, waterDrag, gravity, maxVelocity;

    public ProjectilePath(double airDrag, double waterDrag, double gravity, double maxVelocity) {
        this.airDrag = airDrag;
        this.waterDrag = waterDrag;
        this.gravity = gravity;
        this.maxVelocity = maxVelocity;
    }

    public Result simulate(int maxTicks, float useDelta, float tickDelta, double renderOffset) {
        if (maxTicks <= 0 || PlayerUtils.invalid())
            return Result.MISS;

        LocalPlayer shooter = PlayerUtils.player();
        ClientLevel world = PlayerUtils.getClientWorld();
        double velocity = maxVelocity * useDelta;
        Camera cam = mc.gameRenderer.mainCamera();
        float pitch = (float)MathUtils.lerp(shooter.xBob, shooter.getXRot(), tickDelta);
        float yaw = (float)MathUtils.lerp(shooter.yBob, shooter.getYRot(), tickDelta);
        boolean firstPerson = mc.options.getCameraType().isFirstPerson();

        Vec3 dir = Vec3.directionFromRotation(pitch, yaw).scale(velocity);
        Vec3 pos = shooter.getEyePosition();
        Vec3 pos2 = MathUtils.forward((firstPerson ? cam.position() : pos).add(0, -0.05, 0), Vec3.directionFromRotation(0, yaw + 90), renderOffset);
        Vec3 prevPos;

        HitResult hit = MissHitResult.MISS;
        List<Vec3> vertices = new ArrayList<>();

        vertices.add(pos2);
        for (int i = 0; i < maxTicks; i++) {
            prevPos = pos;
            pos = pos.add(dir);
            pos2 = pos2.add(dir);

            dir = dir.add(0, gravity, 0);
            dir = dir.scale(isInFluid(world, pos) ? waterDrag : airDrag);

            if (prevPos.distanceTo(pos) >= 0.0625)
                vertices.add(pos2);
            if ((hit = get(world, pos, prevPos)).getType() != HitResult.Type.MISS) {
                if (hit instanceof EntityHitResult eHit && !eHit.getEntity().showVehicleHealth())
                    continue;
                vertices.add(pos2);
                break;
            }
        }
        return new Result(hit, vertices);
    }

    private HitResult get(ClientLevel world, Vec3 pos, Vec3 prevPos) {
        Vec3 dir = pos.subtract(prevPos).normalize();
        double dist = prevPos.distanceTo(pos);
        Vec3 end = pos.add(dir.scale(dist));
        Set<Entity> nearby = new HashSet<>(world.getEntities((Entity)null, new AABB(pos, end).inflate(1.0E-7), entity -> true));
        List<Entity> candidates = new ArrayList<>(nearby.size());

        for (Entity entity : world.entitiesForRendering())
            if (nearby.contains(entity))
                candidates.add(entity);

        for (double i = 0.0; i <= dist; i += 0.0625) {
            double x = pos.x + dir.x * i;
            double y = pos.y + dir.y * i;
            double z = pos.z + dir.z * i;

            for (Entity ent : candidates)
                if (ent.getBoundingBox().contains(x, y, z))
                    return new EntityHitResult(ent, pos);
        }

        ClipContext context = new ClipContext(prevPos, pos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, PlayerUtils.player());
        return world.clip(context);
    }

    private boolean isInFluid(ClientLevel world, Vec3 pos) {
        FluidState state = world.getFluidState(BlockPos.containing(pos));
        Fluid fluid = state.getType();

        if (fluid != Fluids.WATER && fluid != Fluids.FLOWING_WATER)
            return false;
        return MathUtils.floorDiff(pos.y) <= state.getOwnHeight();
    }

    public void set(double airDrag, double waterDrag, double gravity, double maxVelocity) {
        this.airDrag = airDrag;
        this.waterDrag = waterDrag;
        this.gravity = gravity;
        this.maxVelocity = maxVelocity;
    }

    public record Result(HitResult hit, List<Vec3> vertices) {
        public static final Result MISS = new Result(MissHitResult.MISS, new ArrayList<>());

        public void draw(PoseStack matrices, SubmitNodeCollector submitNodeCollector, Vec3 cameraPosition, float tickDelta) {
            if (vertices.size() <= 1)
                return;

            LocalPlayer p = PlayerUtils.player();
            Vec3 playerPos = MathUtils.lerpEntityPosVec(p, tickDelta);
            Vec3 offset = playerPos.subtract(p.xOld, p.yOld, p.zOld);

            Vec3 last = vertices.get(vertices.size() - 1);

            if (vertices.size() >= 2) {
                for (int i = 0; i < vertices.size() - 1; i++) {
                    boolean player = (int)vertices.get(i).y == (int)p.getY();
                    boolean hitEnt = hit.getType() == HitResult.Type.ENTITY;
                    Vec3 v1 = vertices.get(i).add(offset).subtract(cameraPosition);
                    Vec3 v2 = vertices.get(i + 1).add(offset).subtract(cameraPosition);
                    RenderUtils3d.drawFlatLine(matrices, submitNodeCollector, v1.x, v1.y, v1.z, v2.x, v2.y, v2.z, 0.05, player || hitEnt ? 0xFFFF4040 : 0xFFFFFFFF);
                }
            }

            if (hit.getType() == HitResult.Type.MISS)
                return;
            else if (hit instanceof BlockHitResult block)
                RenderUtils3d.renderBlock(matrices, submitNodeCollector, Vec3.atLowerCornerOf(block.getBlockPos()).subtract(cameraPosition), 0x40FFFFFF);
            else if (hit instanceof EntityHitResult eHit && eHit.getEntity() instanceof LivingEntity liv && liv != p) {
                AABB box = liv.getBoundingBox().move(cameraPosition.reverse());
                RenderUtils3d.fillBox(matrices, submitNodeCollector, box, 0x40FF4040);
            }

            if (last.distanceTo(p.getEyePosition()) > 3.0) {
//                Vec3 from = playerPos.subtract(cameraPosition);
                Vec3 to = last.add(offset).subtract(cameraPosition);
                double w = 0.125;
                AABB box = new AABB(to.add(-w, -w, -w), to.add(w, w, w));
                RenderUtils3d.fillBox(matrices, submitNodeCollector, box, 0x80FF4040);
//                RenderUtils3d.drawLine(matrices, submitNodeCollector, to.x, to.y, to.z, from.x, from.y, from.z, 0xFFFF4040);
            }
        }
    }
}
