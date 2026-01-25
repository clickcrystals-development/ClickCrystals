package io.github.itzispyder.clickcrystals.scripting.components.conditionalcontexts;

import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationContext;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationResult;
import io.github.itzispyder.clickcrystals.scripting.components.Conditional;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.entity.Entity;

import java.util.function.Predicate;

public class ConditionalEntityInRange implements Conditional {

    @Override
    public ConditionEvaluationResult evaluate(ConditionEvaluationContext ctx) {
        Predicate<Entity> filter = ctx.match(0, "any_entity") ? entity -> true : ScriptParser.parseEntityPredicate(ctx.get(0).toString());
        double range = ctx.get(1).toDouble();
        boolean[] bl = { false };
        EntityUtils.runOnNearestEntity(ctx.entity, range, filter, entity -> {
            bl[0] = entity.distanceTo(PlayerUtils.player()) <= range;
        });
        return ctx.end(true, bl[0]);
    }
}
