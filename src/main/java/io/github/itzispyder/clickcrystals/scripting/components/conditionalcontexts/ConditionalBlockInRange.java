package io.github.itzispyder.clickcrystals.scripting.components.conditionalcontexts;

import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationContext;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationResult;
import io.github.itzispyder.clickcrystals.scripting.components.Conditional;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.block.BlockState;

import java.util.function.Predicate;

public class ConditionalBlockInRange implements Conditional {

    @Override
    public ConditionEvaluationResult evaluate(ConditionEvaluationContext ctx) {
        Predicate<BlockState> filter = ctx.match(0, "any_block") ? state -> true : ScriptParser.parseBlockPredicate(ctx.get(0).toString());
        double range = ctx.get(1).toDouble();
        boolean[] bl = { false };
        EntityUtils.runOnNearestBlock(ctx.entity, range, filter, (pos, state) -> {
            bl[0] = pos.toCenterPos().distanceTo(PlayerUtils.getPos()) <= range;
        });
        return ctx.end(true, bl[0]);
    }
}
