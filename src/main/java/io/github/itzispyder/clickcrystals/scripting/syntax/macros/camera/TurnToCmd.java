package io.github.itzispyder.clickcrystals.scripting.syntax.macros.camera;

import io.github.itzispyder.clickcrystals.modules.modules.misc.TeamDetector;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.syntax.TargetType;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PolarParser;
import io.github.itzispyder.clickcrystals.util.minecraft.VectorParser;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Random;
import java.util.function.Predicate;

public class TurnToCmd extends ScriptCommand {

    private static final Random RNG = new Random();

    private static float prevTickYaw     = Float.NaN;
    private static float prevTickPitch   = Float.NaN;
    private static float prevDeltaYaw    = 0.0f;
    private static float prevDeltaPitch  = 0.0f;

    private static final float INERTIA_BLEND = 0.18f;
    private static final float ACCELERATION_CAP = 22.0f;
    private static final float DECEL_FACTOR = 0.72f;

    public TurnToCmd() {
        super("turn_to");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (PlayerUtils.invalid()) return;

        TurnOptions.sampleCurrentRotation();

        Vec3 eyes = PlayerUtils.player().getEyePosition();
        var read = args.getReader();

        switch (read.next(TargetType.class)) {
            case NEAREST_BLOCK -> {
                Predicate<BlockState> filter = ScriptParser.parseBlockPredicate(read.nextStr());
                PlayerUtils.runOnNearestBlock(32, filter, (pos, state) -> turn(VectorParser.getCenter(pos), eyes, args));
            }
            case NEAREST_ENTITY -> {
                Predicate<Entity> filter = ScriptParser.parseEntityPredicate(read.nextStr());
                PlayerUtils.runOnNearestEntity(128, filter, entity -> {
                    if (!(entity instanceof Player) || !TeamDetector.isTeammate((Player) entity))
                        turn(entity.position(), eyes, args);
                });
            }
            case ANY_BLOCK -> PlayerUtils.runOnNearestBlock(32, (pos, state) -> true, (pos, state) -> turn(VectorParser.getCenter(pos), eyes, args));
            case ANY_ENTITY -> PlayerUtils.runOnNearestEntity(128, Entity::isAlive, entity -> {
                if (!(entity instanceof Player) || !TeamDetector.isTeammate((Player) entity))
                    turn(entity.position(), eyes, args);
            });
            case POSITION -> {
                VectorParser parser = new VectorParser(read.nextStr(), read.nextStr(), read.nextStr(), PlayerUtils.player());
                turn(parser.getVector(), eyes, args);
            }
            case POLAR -> {
                PolarParser parser = new PolarParser(read.nextStr(), read.nextStr(), PlayerUtils.player());
                turn(eyes.add(parser.getVector()), eyes, args);
            }
            default -> throw new IllegalArgumentException("unsupported operation");
        }
    }

    private void turn(Vec3 dest, Vec3 camPos, ScriptArgs args) {
        if (system.cameraRotator.isRunningTicket()) return;

        var read = args.getReader();
        TurnOptions options = new TurnOptions();
        options.configure(read);
        options.configure(read);

        var player = PlayerUtils.player();
        float currentYaw   = player.getYRot();
        float currentPitch = player.getXRot();

        Vec3 dir = options.getCameraTicketPos(camPos, dest);
        float targetYaw   = (float) Math.toDegrees(Math.atan2(-dir.x, dir.z));
        float targetPitch = (float) Math.toDegrees(Math.asin(-dir.y));

        float rawDeltaYaw   = wrapDelta(targetYaw   - currentYaw);
        float rawDeltaPitch = wrapDelta(targetPitch - currentPitch);

        float angularDist = (float) Math.sqrt(rawDeltaYaw * rawDeltaYaw + rawDeltaPitch * rawDeltaPitch);

        float speedScale = computeSpeedScale(angularDist, options.speed);

        float stepYaw   = rawDeltaYaw   * speedScale;
        float stepPitch = rawDeltaPitch * speedScale;

        stepYaw   = blendInertia(stepYaw,   prevDeltaYaw);
        stepPitch = blendInertia(stepPitch, prevDeltaPitch);

        stepYaw   = capAcceleration(stepYaw,   prevDeltaYaw);
        stepPitch = capAcceleration(stepPitch, prevDeltaPitch);

        stepYaw   = TurnOptions.snapToGcd(stepYaw);
        stepPitch = TurnOptions.snapToGcd(stepPitch);

        stepYaw   = TurnOptions.applySubtickJitter(stepYaw);
        stepPitch = TurnOptions.applySubtickJitter(stepPitch);

        float finalYaw   = TurnOptions.avoidExactInteger(currentYaw   + stepYaw);
        float finalPitch = TurnOptions.avoidExactInteger(currentPitch + stepPitch);
        finalPitch = Math.max(-89.9f, Math.min(89.9f, finalPitch));

        prevDeltaYaw   = stepYaw;
        prevDeltaPitch = stepPitch;
        prevTickYaw    = finalYaw;
        prevTickPitch  = finalPitch;

        system.cameraRotator.ready()
                .addTicket(dir, options.speed, options.speed, true)
                .setFinishCallback((pitch, yaw, rotator) -> {
                    player.setYRot(finalYaw);
                    player.setXRot(finalPitch);
                    read.executeThenChain();
                })
                .openCurrentTicket();
    }

    private float computeSpeedScale(float angularDist, float baseSpeed) {
        if (angularDist < 0.001f) return 1.0f;
        float scale = baseSpeed / 100.0f;
        if (angularDist < 10.0f) {
            scale *= DECEL_FACTOR * (angularDist / 10.0f);
        }
        return Math.max(0.04f, Math.min(1.0f, scale));
    }

    private float blendInertia(float current, float prev) {
        return prev * INERTIA_BLEND + current * (1.0f - INERTIA_BLEND);
    }

    private float capAcceleration(float delta, float prevDelta) {
        float accel = delta - prevDelta;
        if (Math.abs(accel) > ACCELERATION_CAP) {
            accel = Math.signum(accel) * ACCELERATION_CAP;
            return prevDelta + accel;
        }
        return delta;
    }

    private float wrapDelta(float delta) {
        while (delta > 180.0f)  delta -= 360.0f;
        while (delta < -180.0f) delta += 360.0f;
        return delta;
    }
}
