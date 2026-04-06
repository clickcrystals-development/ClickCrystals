package io.github.itzispyder.clickcrystals.modules.modules.rendering.trajectories;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.world.RenderWorldEvent;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.MissHitResult;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils3d;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

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
        Camera cam = mc.gameRenderer.getMainCamera();
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

        for (double i = 0.0; i <= dist; i += 0.0625) {
            Vec3 point = pos.add(dir.scale(i));
            for (Entity ent : world.entitiesForRendering())
                if (ent.getBoundingBox().contains(point))
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

        public void draw(RenderWorldEvent e, float tickDelta) {
            if (vertices.size() <= 1)
                return;

            LocalPlayer p = PlayerUtils.player();
            PoseStack matrices = e.getMatrices();
            Vec3 playerPos = MathUtils.lerpEntityPosVec(p, tickDelta);
            Vec3 playerEye = MathUtils.lerpEntityEyeVec(p, tickDelta);
            Vec3 offset = playerPos.subtract(p.xOld, p.yOld, p.zOld);

            for (int i = 0; i < vertices.size(); i++) {
                Vec3 vec = vertices.get(i);
                float pitch = (p.getXRot() - p.xRotO) * tickDelta;
                float yaw = (p.getYRot() - p.yRotO) * tickDelta;
                MathUtils.rotate(vec, playerEye, pitch, yaw);
            }

            Vec3 last = vertices.get(vertices.size() - 1);

            if (vertices.size() >= 2) {
                for (int i = 0; i < vertices.size() - 1; i++) {
                    boolean player = (int)vertices.get(i).y == (int)p.getY();
                    boolean hitEnt = hit.getType() == HitResult.Type.ENTITY;
                    Vec3 v1 = e.getOffsetPos(vertices.get(i).add(offset));
                    Vec3 v2 = e.getOffsetPos(vertices.get(i + 1).add(offset));
                    RenderUtils3d.drawFlatLine(matrices, v1.x, v1.y, v1.z, v2.x, v2.y, v2.z, 0.05, player || hitEnt ? 0xFFFF4040 : 0xFFFFFFFF);
                }
            }

            if (hit.getType() == HitResult.Type.MISS)
                return;
            else if (hit instanceof BlockHitResult block)
                RenderUtils3d.renderBlock(matrices, e.getOffsetPos(block.getBlockPos()), 0x40FFFFFF);
            else if (hit instanceof EntityHitResult eHit && eHit.getEntity() instanceof LivingEntity liv && liv != p) {
                AABB box = liv.getBoundingBox().move(e.getCamera().position().reverse());
                RenderUtils3d.fillBox(matrices, box, 0x40FF4040);
            }

            if (last.distanceTo(p.getEyePosition()) > 3.0) {
//                Vec3d from = e.getOffsetPos(playerPos);
                Vec3 to = e.getOffsetPos(last.add(offset));
                double w = 0.125;
                AABB box = new AABB(to.add(-w, -w, -w), to.add(w, w, w));
                RenderUtils3d.fillBox(matrices, box, 0x80FF4040);
//                RenderUtils3d.drawLine(matrices, to.x, to.y, to.z, from.x, from.y, from.z, 0xFFFF4040);
            }
        }
    }
}