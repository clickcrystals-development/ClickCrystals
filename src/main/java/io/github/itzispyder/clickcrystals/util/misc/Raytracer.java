package io.github.itzispyder.clickcrystals.util.misc;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3;
import net.minecraft.world.World;

public class Raytracer {

    public static Pair<BlockPos, BlockState> trace(World world, Vec3 start, Vec3 dir, double dist, HitCondition predicate) {
        BlockPos prev = null;
        for (double curDist = 0.0; curDist <= dist; curDist += 0.5) {
            Vec3 point = start.add(dir.x * curDist, dir.y * curDist, dir.z * curDist);
            BlockPos pos = new BlockPos((int)Math.floor(point.x), (int)Math.floor(point.y), (int)Math.floor(point.z));

            if (prev == null) {
                prev = pos;
            }

            if (!prev.equals(pos)) {
                if (predicate.test(pos, point)) {
                    return Pair.of(pos, world.getBlockState(pos));
                }
                prev = pos;
            }
        }

        Vec3 point = start.add(dir.x * dist, dir.y * dist, dir.z * dist);
        BlockPos pos = new BlockPos((int)Math.floor(point.x), (int)Math.floor(point.y), (int)Math.floor(point.z));
        return Pair.of(pos, world.getBlockState(pos));
    }

    @FunctionalInterface
    public interface HitCondition {
        boolean test(BlockPos pos, Vec3 point);
    }
}
