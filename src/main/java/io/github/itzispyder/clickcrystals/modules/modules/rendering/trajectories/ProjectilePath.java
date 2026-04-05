package io.github.itzispyder.clickcrystals.modules.modules.rendering.trajectories;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.world.RenderWorldEvent;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.MissHitResult;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils3d;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3;
import net.minecraft.world.RaycastContext;

import java.util.ArrayList;
import java.util.List;

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

        ClientPlayerEntity shooter = PlayerUtils.player();
        ClientWorld world = PlayerUtils.getClientWorld();
        double velocity = maxVelocity * useDelta;
        Camera cam = mc.gameRenderer.getCamera();
        float pitch = (float)MathUtils.lerp(shooter.renderPitch, shooter.getPitch(), tickDelta);
        float yaw = (float)MathUtils.lerp(shooter.renderYaw, shooter.getYaw(), tickDelta);
        boolean firstPerson = mc.options.getPerspective().isFirstPerson();

        Vec3 dir = Vec3.fromPolar(pitch, yaw).multiply(velocity);
        Vec3 pos = shooter.getEyePosition();
        Vec3 pos2 = MathUtils.forward((firstPerson ? cam.getCameraPos() : pos).add(0, -0.05, 0), Vec3.fromPolar(0, yaw + 90), renderOffset);
        Vec3 prevPos;

        HitResult hit = MissHitResult.MISS;
        List<Vec3> vertices = new ArrayList<>();

        vertices.add(pos2);
        for (int i = 0; i < maxTicks; i++) {
            prevPos = pos;
            pos = pos.add(dir);
            pos2 = pos2.add(dir);

            dir = dir.add(0, gravity, 0);
            dir = dir.multiply(isInFluid(world, pos) ? waterDrag : airDrag);

            if (prevPos.distanceTo(pos) >= 0.0625)
                vertices.add(pos2);
            if ((hit = get(world, pos, prevPos)).getType() != HitResult.Type.MISS) {
                if (hit instanceof EntityHitResult eHit && !eHit.getEntity().isLiving())
                    continue;
                vertices.add(pos2);
                break;
            }
        }
        return new Result(hit, vertices);
    }

    private HitResult get(ClientWorld world, Vec3 pos, Vec3 prevPos) {
        Vec3 dir = pos.subtract(prevPos).normalize();
        double dist = prevPos.distanceTo(pos);

        for (double i = 0.0; i <= dist; i += 0.0625) {
            Vec3 point = pos.add(dir.multiply(i));
            for (Entity ent : world.getEntities())
                if (ent.getBoundingBox().contains(point))
                    return new EntityHitResult(ent, pos);
        }

        RaycastContext context = new RaycastContext(prevPos, pos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, PlayerUtils.player());
        return world.raycast(context);
    }

    private boolean isInFluid(ClientWorld world, Vec3 pos) {
        FluidState state = world.getFluidState(BlockPos.ofFloored(pos));
        Fluid fluid = state.getFluid();

        if (fluid != Fluids.WATER && fluid != Fluids.FLOWING_WATER)
            return false;
        return MathUtils.floorDiff(pos.y) <= state.getHeight();
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

            ClientPlayerEntity p = PlayerUtils.player();
            MatrixStack matrices = e.getMatrices();
            Vec3 playerPos = MathUtils.lerpEntityPosVec(p, tickDelta);
            Vec3 playerEye = MathUtils.lerpEntityEyeVec(p, tickDelta);
            Vec3 offset = playerPos.subtract(p.lastRenderX, p.lastRenderY, p.lastRenderZ);

            for (int i = 0; i < vertices.size(); i++) {
                Vec3 vec = vertices.get(i);
                float pitch = (p.getPitch() - p.lastPitch) * tickDelta;
                float yaw = (p.getYaw() - p.lastYaw) * tickDelta;
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
                Box box = liv.getBoundingBox().offset(e.getCamera().getCameraPos().negate());
                RenderUtils3d.fillBox(matrices, box, 0x40FF4040);
            }

            if (last.distanceTo(p.getEyePosition()) > 3.0) {
//                Vec3 from = e.getOffsetPos(playerPos);
                Vec3 to = e.getOffsetPos(last.add(offset));
                double w = 0.125;
                Box box = new Box(to.add(-w, -w, -w), to.add(w, w, w));
                RenderUtils3d.fillBox(matrices, box, 0x80FF4040);
//                RenderUtils3d.drawLine(matrices, to.x, to.y, to.z, from.x, from.y, from.z, 0xFFFF4040);
            }
        }
    }
}