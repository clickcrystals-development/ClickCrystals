package io.github.itzispyder.clickcrystals.events.events.client;

import io.github.itzispyder.clickcrystals.events.Event;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public class EntityDamageEvent extends Event {

    private final DamageSource source;
    private final Entity entity;

    public EntityDamageEvent(ClientboundDamageEventPacket packet) {
        var world = PlayerUtils.getWorld();

        this.source = packet.getSource(world);
        this.entity = world.getEntity(packet.entityId());
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean isSelf() {
        return PlayerUtils.valid() && entity.getId() == PlayerUtils.player().getId();
    }

    public DamageSource getSource() {
        return source;
    }

    public float getCurrentHealth() {
        return PlayerUtils.invalid() ? 0.0F : PlayerUtils.player().getHealth();
    }
}
