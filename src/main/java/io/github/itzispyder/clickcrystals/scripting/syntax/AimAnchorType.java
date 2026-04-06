package io.github.itzispyder.clickcrystals.scripting.syntax;

import java.util.function.Function;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public enum AimAnchorType {

    HEAD(Entity::getEyePosition, v -> v.add(0, 1.6, 0)),
    BODY(ent -> ent.position().add(0, ent.getBbHeight() * 0.66, 0), v -> v.add(0, 1.8 * 0.66, 0)),
    LEGS(ent -> ent.position().add(0, ent.getBbHeight() * 0.33, 0), v -> v.add(0, 1.8 * 0.33, 0)),
    FEET(Entity::position, v -> v),
    RANDOM(ent -> ent.position().add(0.1 * Math.random(), ent.getBbHeight() * Math.random(), 0.1 * Math.random()),
            v -> v.add(0.1 * Math.random(), 1.6 * Math.random(), 0.1 * Math.random()));

    public final Function<Entity, Vec3> entityFactory;
    public final Function<Vec3, Vec3> positionFactory;

    AimAnchorType(Function<Entity, Vec3> entityFactory, Function<Vec3, Vec3> positionFactory) {
        this.entityFactory = entityFactory;
        this.positionFactory = positionFactory;
    }
}
