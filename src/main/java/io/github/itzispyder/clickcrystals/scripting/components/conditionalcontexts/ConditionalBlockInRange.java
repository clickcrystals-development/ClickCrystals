package io.github.itzispyder.clickcrystals.scripting.components.conditionalcontexts;

import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationContext;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationResult;
import io.github.itzispyder.clickcrystals.scripting.components.Conditional;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.block.BlockState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class ConditionalBlockInRange implements Conditional {

    private static final Map<String, Predicate<BlockState>> PREDICATE_CACHE = new ConcurrentHashMap<>();

    @Override
    public ConditionEvaluationResult evaluate(ConditionEvaluationContext ctx) {
        Predicate<BlockState> filter = resolveFilter(ctx);
        AtomicBoolean bl = new AtomicBoolean(false);
        double range = ctx.get(1).toDouble();

        EntityUtils.runOnNearestBlock(ctx.entity, range, filter,
                (pos, state) -> bl.set(pos.toCenterPos().distanceTo(PlayerUtils.getPos()) <= range));
        return ctx.end(true, bl.get());
    }

    private Predicate<BlockState> resolveFilter(ConditionEvaluationContext ctx) {
        if (ctx.match(0, "any_block"))
            return state -> true;
        String arg = ctx.get(0).toString();
        return PREDICATE_CACHE.computeIfAbsent(arg, ScriptParser::parseBlockPredicate);
    }
}
