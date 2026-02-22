package io.github.itzispyder.clickcrystals.scripting.components.conditionalcontexts;

import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationContext;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationResult;
import io.github.itzispyder.clickcrystals.scripting.components.Conditional;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class ConditionalEntityInFov implements Conditional {

    @Override
    public ConditionEvaluationResult evaluate(ConditionEvaluationContext ctx) {
        Predicate<Entity> filter = ctx.match(0, "any_entity") ? entity -> true : ScriptParser.parseEntityPredicate(ctx.get(0).toString());
        AtomicBoolean bl = new AtomicBoolean(false);
        float fovDeg = ctx.get(1).toFloat();

        EntityUtils.runOnNearestEntity(ctx.entity, 256,
                entity -> filter.test(entity) && validEntity(entity, fovDeg),
                entity -> bl.set(true));
        return ctx.end(true, bl.get());
    }

    private boolean validEntity(Entity entity, float fovDeg) {
        if (fovDeg != 360 && PlayerUtils.valid())
            if (!isPointInFov(PlayerUtils.getEyes(), PlayerUtils.getDir(), fovDeg, entity.getEntityPos()))
                return false;
        return entity.distanceTo(PlayerUtils.player()) <= fovDeg;
    }

    public static boolean isPointInFov(Vec3d cam, Vec3d dir, float fovDeg, Vec3d point) {
        Vec3d va = point.subtract(cam).normalize();
        Vec3d vb = dir.normalize();
        double dot = va.dotProduct(vb); // dot product is the cosine of the angle between two unit vectors
        double rot = MathHelper.cos(Math.toRadians(fovDeg * 0.5));
        return dot >= rot; // im flipping sign cuz cosine of bigger theta becomes smaller value
    }
}