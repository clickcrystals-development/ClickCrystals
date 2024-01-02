package io.github.itzispyder.clickcrystals.events.events.client;

import io.github.itzispyder.clickcrystals.events.Event;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;

public class EntityDamageEvent extends Event {

    private final DamageSource source;
    private final Entity entity;

    public EntityDamageEvent(EntityDamageS2CPacket packet) {
        var world = PlayerUtils.getWorld();
        this.source = packet.createDamageSource(world);
        this.entity = world.getEntityById(packet.entityId());
    }

    public boolean isSelf() {
        return PlayerUtils.playerNotNull() && entity.getId() == PlayerUtils.player().getId();
    }

    public DamageSource getSource() {
        return source;
    }

    public float getCurrentHealth() {
        return PlayerUtils.playerNull() ? 0.0F : PlayerUtils.player().getHealth();
    }
}
