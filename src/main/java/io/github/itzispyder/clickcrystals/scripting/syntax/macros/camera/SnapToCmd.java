package io.github.itzispyder.clickcrystals.scripting.syntax.macros.camera;

import io.github.itzispyder.clickcrystals.modules.modules.misc.TeamDetector;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgsReader;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.syntax.TargetType;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PolarParser;
import io.github.itzispyder.clickcrystals.util.minecraft.VectorParser;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Random;
import java.util.function.Predicate;

public class SnapToCmd extends ScriptCommand {

    private static final Random RNG = new Random();

    private static final float MAX_YAW_STEP   = 35.0f;
    private static final float MAX_PITCH_STEP = 25.0f;

    private static final float ACCEL_EASE_TICKS = 3.0f;

    private float pendingYaw;
    private float pendingPitch;
    private boolean hasPending = false;
    private int ticksActive = 0;

    public SnapToCmd() {
        super("snap_to");
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
                PlayerUtils.runOnNearestBlock(32, filter, (pos, state) -> snap(VectorParser.getCenter(pos), eyes, args));
            }
            case NEAREST_ENTITY -> {
                Predicate<Entity> filter = ScriptParser.parseEntityPredicate(read.nextStr());
                PlayerUtils.runOnNearestEntity(128, filter, entity -> {
                    if (!(entity instanceof Player) || !TeamDetector.isTeammate((Player) entity))
                        snap(entity.position(), eyes, args);
                });
            }
            case ANY_BLOCK -> PlayerUtils.runOnNearestBlock(32, (pos, state) -> true, (pos, state) -> snap(VectorParser.getCenter(pos), eyes, args));
            case ANY_ENTITY -> PlayerUtils.runOnNearestEntity(128, Entity::isAlive, entity -> {
                if (!(entity instanceof Player) || !TeamDetector.isTeammate((Player) entity))
                    snap(entity.position(), eyes, args);
            });
            case POSITION -> {
                VectorParser parser = new VectorParser(read.nextStr(), read.nextStr(), read.nextStr(), PlayerUtils.player());
                snap(parser.getVector(), eyes, args);
            }
            case POLAR -> {
                PolarParser parser = new PolarParser(read.nextStr(), read.nextStr(), PlayerUtils.player());
                snap(eyes.add(parser.getVector()), eyes, args);
            }
            default -> throw new IllegalArgumentException("unsupported operation");
        }
    }

    private void snap(Vec3 dest, Vec3 camPos, ScriptArgs args) {
        TurnOptions options = new TurnOptions();
        ScriptArgsReader read = args.getReader();
        options.configure(read);

        Vec3 target = options.getCameraTicketPos(camPos, dest);
        float[] rot = MathUtils.toPolar(target.x, target.y, target.z);

        float rawPitch = (float) MathUtils.wrapDegrees(rot[0]);
        float rawYaw   = (float) MathUtils.wrapDegrees(rot[1]);

        var player = PlayerUtils.player();
        float currentYaw   = player.getYRot();
        float currentPitch = player.getXRot();

        float deltaYaw   = wrapDelta(rawYaw   - currentYaw);
        float deltaPitch = wrapDelta(rawPitch - currentPitch);

        deltaYaw   = clampStep(deltaYaw,   MAX_YAW_STEP);
        deltaPitch = clampStep(deltaPitch, MAX_PITCH_STEP);

        deltaYaw   = easeIn(deltaYaw,   ++ticksActive);
        deltaPitch = easeIn(deltaPitch, ticksActive);

        deltaYaw   = TurnOptions.snapToGcd(deltaYaw);
        deltaPitch = TurnOptions.snapToGcd(deltaPitch);

        deltaYaw   = TurnOptions.applySubtickJitter(deltaYaw);
        deltaPitch = TurnOptions.applySubtickJitter(deltaPitch);

        float finalYaw   = TurnOptions.avoidExactInteger(currentYaw   + deltaYaw);
        float finalPitch = TurnOptions.avoidExactInteger(currentPitch + deltaPitch);

        finalPitch = Math.max(-89.9f, Math.min(89.9f, finalPitch));

        player.setYRot(finalYaw);
        player.setXRot(finalPitch);

        read.executeThenChain();
    }

    private float wrapDelta(float delta) {
        while (delta > 180.0f)  delta -= 360.0f;
        while (delta < -180.0f) delta += 360.0f;
        return delta;
    }

    private float clampStep(float delta, float max) {
        return Math.max(-max, Math.min(max, delta));
    }

    private float easeIn(float delta, int ticks) {
        if (ticks <= 0) return delta;
        float factor = Math.min(1.0f, ticks / ACCEL_EASE_TICKS);
        float ease = (float)(1.0 - Math.cos(factor * Math.PI)) * 0.5f;
        return delta * (0.45f + 0.55f * ease);
    }
}
