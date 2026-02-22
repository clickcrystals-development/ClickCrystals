package io.github.itzispyder.clickcrystals.scripting.components.conditionalcontexts;

import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationContext;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationResult;
import io.github.itzispyder.clickcrystals.scripting.components.Conditional;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class ConditionalBlockInFov implements Conditional {

    @Override
    public ConditionEvaluationResult evaluate(ConditionEvaluationContext ctx) {
        Predicate<BlockState> filter = ctx.match(0, "any_block") ? state -> true : ScriptParser.parseBlockPredicate(ctx.get(0).toString());
        AtomicBoolean bl = new AtomicBoolean(false);
        float fovDeg = ctx.get(1).toFloat();

        EntityUtils.runOnNearestBlock(ctx.entity, 32,
                (pos, state) -> filter.test(state) && validBlock(pos, fovDeg),
                (pos, state) -> bl.set(true));
        return ctx.end(true, bl.get());
    }

    private boolean validBlock(BlockPos pos, float fovDeg) {
        if (fovDeg != 360 && PlayerUtils.valid())
            if (!ConditionalEntityInFov.isPointInFov(PlayerUtils.getEyes(), PlayerUtils.getDir(), fovDeg, pos.toCenterPos()))
                return false;
        return pos.toCenterPos().distanceTo(PlayerUtils.getPos()) <= fovDeg;
    }
}