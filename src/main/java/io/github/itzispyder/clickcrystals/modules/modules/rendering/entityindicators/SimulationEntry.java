package io.github.itzispyder.clickcrystals.modules.modules.rendering.entityindicators;

import io.github.itzispyder.clickcrystals.gui.misc.brushes.MobHeadBrush;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.misc.Voidable;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

public class SimulationEntry {

    private final EntityType<?> entityType;
    private final Vec3 vecDifference;
    private final float yawDifference;

    public SimulationEntry(Entity entity, LocalPlayer client) {
        this.entityType = entity.getType();
        this.vecDifference = entity.position().subtract(client.position()).normalize();

        float[] rot = MathUtils.toPolar(vecDifference.x, vecDifference.y, vecDifference.z);
        this.yawDifference = rot[1] - client.getYRot();
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }

    public Voidable<Identifier> getTexture() {
        return Voidable.of(MobHeadBrush.getIdentifier(entityType));
    }

    public Vec3 getVecDifference() {
        return vecDifference;
    }

    public float getYawDifference() {
        return yawDifference;
    }
}
