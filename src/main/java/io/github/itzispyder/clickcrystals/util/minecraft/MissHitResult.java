package io.github.itzispyder.clickcrystals.util.minecraft;

import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class MissHitResult extends HitResult {

    public static final MissHitResult MISS = new MissHitResult();

    protected MissHitResult() {
        super(Vec3.ZERO);
    }

    @Override
    public Type getType() {
        return Type.MISS;
    }
}
