package io.github.itzispyder.clickcrystals.util.minecraft;

import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class MissHitResult extends HitResult {

    public static final MissHitResult MISS = new MissHitResult();

    protected MissHitResult() {
        super(Vec3d.ZERO);
    }

    @Override
    public Type getType() {
        return Type.MISS;
    }
}
