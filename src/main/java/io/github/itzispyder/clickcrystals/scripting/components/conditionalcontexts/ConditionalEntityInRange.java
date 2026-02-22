package io.github.itzispyder.clickcrystals.scripting.components.conditionalcontexts;

import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationContext;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationResult;
import io.github.itzispyder.clickcrystals.scripting.components.Conditional;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.entity.Entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class ConditionalEntityInRange implements Conditional {

    private static final Map<String, Predicate<Entity>> PREDICATE_CACHE = new ConcurrentHashMap<>();

    @Override
    public ConditionEvaluationResult evaluate(ConditionEvaluationContext ctx) {
        Predicate<Entity> filter = resolveFilter(ctx);
        AtomicBoolean bl = new AtomicBoolean(false);
        double range = ctx.get(1).toDouble();

        EntityUtils.runOnNearestEntity(ctx.entity, range, filter,
                entity -> bl.set(entity.distanceTo(PlayerUtils.player()) <= range));
        return ctx.end(true, bl.get());
    }

    private Predicate<Entity> resolveFilter(ConditionEvaluationContext ctx) {
        if (ctx.match(0, "any_entity"))
            return entity -> true;
        String arg = ctx.get(0).toString();
        return PREDICATE_CACHE.computeIfAbsent(arg, ScriptParser::parseEntityPredicate);
    }
}
