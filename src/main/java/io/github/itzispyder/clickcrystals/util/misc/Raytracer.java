package io.github.itzispyder.clickcrystals.util.misc;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Raytracer {

    public static Pair<BlockPos, BlockState> trace(World world, Vec3d start, Vec3d dir, double dist, HitCondition predicate) {
        BlockPos.Mutable pos = new BlockPos.Mutable();
        BlockPos prev = null;
        for (double curDist = 0.0; curDist <= dist; curDist += 0.5) {
            Vec3d point = start.add(dir.x * curDist, dir.y * curDist, dir.z * curDist);
            pos.set((int)Math.floor(point.x), (int)Math.floor(point.y), (int)Math.floor(point.z));

            if (prev == null) {
                prev = pos.toImmutable();
            }

            if (!prev.equals(pos)) {
                if (predicate.test(pos, point)) {
                    return Pair.of(pos.toImmutable(), world.getBlockState(pos));
                }
                prev = pos.toImmutable();
            }
        }

        Vec3d point = start.add(dir.x * dist, dir.y * dist, dir.z * dist);
        pos.set((int)Math.floor(point.x), (int)Math.floor(point.y), (int)Math.floor(point.z));
        return Pair.of(pos.toImmutable(), world.getBlockState(pos));
    }

    @FunctionalInterface
    public interface HitCondition {
        boolean test(BlockPos pos, Vec3d point);
    }
}
