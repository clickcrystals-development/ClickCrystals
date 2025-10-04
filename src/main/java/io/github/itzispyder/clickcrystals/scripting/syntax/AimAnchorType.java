package io.github.itzispyder.clickcrystals.scripting.syntax;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.function.Function;

public enum AimAnchorType {

    HEAD(Entity::getEyePos, v -> v.add(0, 1.6, 0)),
    BODY(ent -> ent.getPos().add(0, ent.getHeight() * 0.66, 0), v -> v.add(0, 1.8 * 0.66, 0)),
    LEGS(ent -> ent.getPos().add(0, ent.getHeight() * 0.33, 0), v -> v.add(0, 1.8 * 0.33, 0)),
    FEET(Entity::getPos, v -> v),
    RANDOM(ent -> ent.getPos().add(0.1 * Math.random(), ent.getHeight() * Math.random(), 0.1 * Math.random()),
            v -> v.add(0.1 * Math.random(), 1.6 * Math.random(), 0.1 * Math.random()));

    public final Function<Entity, Vec3d> entityFactory;
    public final Function<Vec3d, Vec3d> positionFactory;

    AimAnchorType(Function<Entity, Vec3d> entityFactory, Function<Vec3d, Vec3d> positionFactory) {
        this.entityFactory = entityFactory;
        this.positionFactory = positionFactory;
    }
}
