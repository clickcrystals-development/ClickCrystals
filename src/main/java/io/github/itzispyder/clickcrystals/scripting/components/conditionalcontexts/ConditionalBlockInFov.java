package io.github.itzispyder.clickcrystals.scripting.components.conditionalcontexts;

import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationContext;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationResult;
import io.github.itzispyder.clickcrystals.scripting.components.Conditional;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

public class ConditionalBlockInFov implements Conditional {

    private static final double DEFAULT_RANGE = 6.0;
    private static final float[] YAW_OFFSETS = {0, -1, 1};
    private static final float[] PITCH_OFFSETS = {0, -1, 1};
    private static final Map<String, Predicate<BlockState>> PREDICATE_CACHE = Collections.synchronizedMap(
        new LinkedHashMap<>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, Predicate<BlockState>> eldest) {
                return size() > 32;
            }
        }
    );

    @Override
    public ConditionEvaluationResult evaluate(ConditionEvaluationContext ctx) {
        if (!PlayerUtils.valid())
            return ctx.end(true, false);

        Predicate<BlockState> filter = resolveFilter(ctx);
        float fovDeg = resolveFov(ctx);
        boolean found = scanCone(ctx, filter, fovDeg);
        
        return ctx.end(true, found);
    }

    private Predicate<BlockState> resolveFilter(ConditionEvaluationContext ctx) {
        if (ctx.match(0, "any_block"))
            return state -> true;
        
        String arg = ctx.get(0).toString();
        Predicate<BlockState> cached = PREDICATE_CACHE.get(arg);
        if (cached != null)
            return cached;
        
        synchronized (PREDICATE_CACHE) {
            cached = PREDICATE_CACHE.get(arg);
            if (cached != null)
                return cached;
            cached = ScriptParser.parseBlockPredicate(arg);
            PREDICATE_CACHE.put(arg, cached);
            return cached;
        }
    }

    private float resolveFov(ConditionEvaluationContext ctx) {
        float fov = ctx.get(1).toFloat();
        if (fov <= 0 || fov > 360)
            return 90;
        return fov;
    }

    private boolean scanCone(ConditionEvaluationContext ctx, Predicate<BlockState> filter, float fovDeg) {
        Vec3d eyes = PlayerUtils.getEyes();
        float yaw = PlayerUtils.player().getYaw();
        float pitch = PlayerUtils.player().getPitch();
        float step = fovDeg / 4;

        for (float yawMult : YAW_OFFSETS) {
            for (float pitchMult : PITCH_OFFSETS) {
                Vec3d dir = getDirection(yaw + yawMult * step, pitch + pitchMult * step);
                Vec3d end = eyes.add(dir.multiply(DEFAULT_RANGE));

                BlockHitResult hit = ctx.entity.getEntityWorld().raycast(
                    new RaycastContext(eyes, end, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, ctx.entity)
                );

                if (hit.getType() == HitResult.Type.BLOCK) {
                    BlockState state = ctx.entity.getEntityWorld().getBlockState(hit.getBlockPos());
                    if (filter.test(state))
                        return true;
                }
            }
        }
        return false;
    }

    private Vec3d getDirection(float yaw, float pitch) {
        float yawRad = (float) Math.toRadians(yaw);
        float pitchRad = (float) Math.toRadians(pitch);
        float cosPitch = (float) Math.cos(pitchRad);
        return new Vec3d(
            -Math.sin(yawRad) * cosPitch,
            -Math.sin(pitchRad),
            Math.cos(yawRad) * cosPitch
        );
    }
}
