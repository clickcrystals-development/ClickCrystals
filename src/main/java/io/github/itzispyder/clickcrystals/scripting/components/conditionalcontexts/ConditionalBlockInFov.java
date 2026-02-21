package io.github.itzispyder.clickcrystals.scripting.components.conditionalcontexts;

import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationContext;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationResult;
import io.github.itzispyder.clickcrystals.scripting.components.Conditional;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.function.Predicate;

public class ConditionalBlockInFov implements Conditional {

    @Override
    public ConditionEvaluationResult evaluate(ConditionEvaluationContext ctx) {
        Predicate<BlockState> filter = ctx.match(0, "any_block") ? state -> true : ScriptParser.parseBlockPredicate(ctx.get(0).toString());
        float fovDeg = ctx.get(1).toFloat();
        Box box = ctx.entity.getBoundingBox().expand(fovDeg);

        for (BlockPos pos : BlockPos.iterate((int)box.minX, (int)box.minY, (int)box.minZ, (int)box.maxX, (int)box.maxY, (int)box.maxZ)) {
            BlockState state = ctx.entity.getEntityWorld().getBlockState(pos);
            if (filter.test(state) && validBlock(pos, fovDeg))
                return ctx.end(true, true);
        }
        return ctx.end(true, false);
    }

    private boolean validBlock(BlockPos pos, float fovDeg) {
        if (!PlayerUtils.valid())
            return false;
        if (pos.toCenterPos().distanceTo(PlayerUtils.getPos()) > fovDeg)
            return false;
        if (fovDeg != 360)
            return ConditionalEntityInFov.isPointInFov(PlayerUtils.getEyes(), PlayerUtils.getDir(), fovDeg, pos.toCenterPos());
        return true;
    }
}
