package io.github.itzispyder.clickcrystals.modules.modules.rendering.entityindicators;

import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.misc.Voidable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;

import java.util.ArrayList;
import java.util.List;

public class EntityIndicatorSimulation {

    private final List<SimulationEntry> entities;
    private SimulationEntry nearestEntity;
    private final EntityIndicatorSimulationRenderer renderer;

    public EntityIndicatorSimulation() {
        this.entities = new ArrayList<>();
        this.nearestEntity = null;
        this.renderer = new EntityIndicatorSimulationRenderer(this, 100);
    }

    public void update(int range, boolean onlyMonsters) {
        var player = PlayerUtils.player();
        entities.clear();
        nearestEntity = null;

        PlayerUtils.runOnNearestEntity(range, entity -> {
            boolean bl = entity instanceof MobEntity && entity.isAlive() && !entity.isInvisibleTo(player);
            boolean bl2 = !(!(entity instanceof Monster) && onlyMonsters);
            if (bl && bl2) {
                entities.add(new SimulationEntry(entity, player));
            }
            return bl && bl2;
        }, nearest -> {
            nearestEntity = new SimulationEntry(nearest, player);
            entities.remove(nearestEntity);
        });
    }

    public void clear() {
        entities.clear();
        nearestEntity = null;
    }

    public int size() {
        return entities.size();
    }

    public boolean isEmpty() {
        return entities.isEmpty();
    }

    public boolean notEmpty() {
        return !entities.isEmpty();
    }

    public Voidable<SimulationEntry> getNearestEntity() {
        return Voidable.of(nearestEntity);
    }

    public List<SimulationEntry> getEntities() {
        return entities;
    }

    public EntityIndicatorSimulationRenderer getRenderer() {
        return renderer;
    }
}
