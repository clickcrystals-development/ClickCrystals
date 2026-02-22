package io.github.itzispyder.clickcrystals.scripting.components.conditionalcontexts;

import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationContext;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationResult;
import io.github.itzispyder.clickcrystals.scripting.components.Conditional;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

public class ConditionalEntityInFov implements Conditional {

    @Override
    public ConditionEvaluationResult evaluate(ConditionEvaluationContext ctx) {
        Predicate<Entity> filter = ctx.match(0, "any_entity") ? entity -> true : ScriptParser.parseEntityPredicate(ctx.get(0).toString());
        float fovDeg = ctx.get(1).toFloat();
        
        if (!PlayerUtils.valid())
            return ctx.end(true, false);
            
        Vec3d eyes = PlayerUtils.getEyes();
        Vec3d dir = PlayerUtils.getDir();
        Box box = Box.from(ctx.entity.getEntityPos()).expand(fovDeg);

        for (Entity entity : ctx.entity.getEntityWorld().getOtherEntities(ctx.entity, box, filter)) {
            if (entity.distanceTo(PlayerUtils.player()) > fovDeg)
                continue;
                
            if (fovDeg != 360 && !isPointInFov(eyes, dir, fovDeg, entity.getEntityPos()))
                continue;
                
            return ctx.end(true, true);
        }
        return ctx.end(true, false);
    }

    public static boolean isPointInFov(Vec3d cam, Vec3d dir, float fovDeg, Vec3d point) {
        Vec3d va = point.subtract(cam).normalize();
        Vec3d vb = dir.normalize();
        double dot = va.dotProduct(vb); // dot product is the cosine of the angle between two unit vectors
        double rot = Math.cos(Math.toRadians(fovDeg * 0.5));
        return dot >= rot; // im flipping sign cuz cosine of bigger theta becomes smaller value
    }
}
