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
        
        if (!PlayerUtils.valid())
            return ctx.end(true, false);
            
        Vec3d playerPos = ctx.entity.getEntityPos();
        Vec3d eyes = PlayerUtils.getEyes();
        Vec3d dir = PlayerUtils.getDir();
        double maxDist = fovDeg;
        double maxDistSq = maxDist * maxDist;
        
        int minX = (int) Math.floor(playerPos.x - maxDist);
        int minY = (int) Math.floor(playerPos.y - maxDist);
        int minZ = (int) Math.floor(playerPos.z - maxDist);
        int maxX = (int) Math.ceil(playerPos.x + maxDist);
        int maxY = (int) Math.ceil(playerPos.y + maxDist);
        int maxZ = (int) Math.ceil(playerPos.z + maxDist);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    Vec3d center = pos.toCenterPos();
                    
                    if (center.squaredDistanceTo(playerPos) > maxDistSq)
                        continue;
                        
                    if (fovDeg != 360 && !ConditionalEntityInFov.isPointInFov(eyes, dir, fovDeg, center))
                        continue;
                        
                    BlockState state = ctx.entity.getEntityWorld().getBlockState(pos);
                    if (filter.test(state))
                        return ctx.end(true, true);
                }
            }
        }
        return ctx.end(true, false);
    }
}
