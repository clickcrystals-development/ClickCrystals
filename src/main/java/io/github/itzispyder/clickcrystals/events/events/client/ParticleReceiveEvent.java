package io.github.itzispyder.clickcrystals.events.events.client;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.Vec3;

public class ParticleReceiveEvent extends Event implements Cancellable {

    private final ParticleType<?> type;
    private final Vec3 pos;
    private final Vec3 velocity;
    private boolean cancelled;

    public ParticleReceiveEvent(ParticleType<?> type, double x, double y, double z, double velX, double velY, double velZ) {
        this.type = type;
        this.pos = new Vec3(x, y, z);
        this.velocity = new Vec3(velX, velY, velZ);
        this.cancelled = false;
    }

    public ParticleType<?> getType() {
        return type;
    }

    public Vec3 getPos() {
        return pos;
    }

    public Vec3 getVelocity() {
        return velocity;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
