package io.github.itzispyder.clickcrystals.modules.modules.rendering.entityindicators;

import io.github.itzispyder.clickcrystals.gui.misc.brushes.MobHeadBrush;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.misc.Voidable;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3;

public class SimulationEntry {

    private final Class<? extends Entity> entityClass;
    private final Vec3 vecDifference;
    private final float yawDifference;

    public SimulationEntry(Entity entity, ClientPlayerEntity client) {
        this.entityClass = entity.getClass();
        this.vecDifference = entity.position().subtract(client.position()).normalize();

        float[] rot = MathUtils.toPolar(vecDifference.x, vecDifference.y, vecDifference.z);
        this.yawDifference = rot[1] - client.getYaw();
    }

    public Class<? extends Entity> getEntityClass() {
        return entityClass;
    }

    public Voidable<Identifier> getTexture() {
        return Voidable.of(MobHeadBrush.getIdentifier(entityClass));
    }

    public Vec3 getVecDifference() {
        return vecDifference;
    }

    public float getYawDifference() {
        return yawDifference;
    }
}
