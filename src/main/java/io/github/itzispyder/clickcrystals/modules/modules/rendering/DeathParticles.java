package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.DoubleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.EnumSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DeathParticles extends ListenerModule {

    private final SettingSection scGeneral = getGeneralSection();

    public final ModuleSetting<Entities> entitySelection = scGeneral.add(EnumSetting.create(Entities.class)
            .name("entity-selection")
            .description("Choose which entity will have this effect.")
            .def(Entities.BOTH)
            .build()
    );

    public final ModuleSetting<Particles> particlesType = scGeneral.add(EnumSetting.create(Particles.class)
            .name("particle-type")
            .description("Choose the particle effect shown when the entity dies.")
            .def(Particles.FLASH)
            .build()
    );

    public final ModuleSetting<Double> particleVelocity = scGeneral.add(DoubleSetting.create()
            .name("particles-velocity")
            .description("Set the speed of the particles.")
            .def(1.0)
            .min(1.0)
            .max(6.0)
            .build()
    );

    @EventHandler
    private void onReceivePacket(PacketReceiveEvent e) {
        if (!(e.getPacket() instanceof EntityStatusS2CPacket packet))
            return;
        if (packet.getStatus() != 3)
            return;

        Entity entity = packet.getEntity(mc.player.getWorld());
        if (!shouldApplyEffect(entity))
            return;

        ParticleType<? extends ParticleEffect> particle = particlesType.getVal().particleType;
        double velocity = system.random.getRandomDouble(particleVelocity.getVal());
        BlockPos entityPos = entity.getBlockPos();
        World world = entity.getEntityWorld();

        for (int i = 0; i < system.random.getRandomInt(5, 10); i++) {
            Direction direction = Direction.byId(system.random.getRandomInt(0, 5));
            Vec3d velocityVec = new Vec3d(
                    system.random.getRandomDouble(-velocity, velocity),
                    system.random.getRandomDouble(velocity * 0.5),
                    system.random.getRandomDouble(-velocity, velocity)
            );
            ParticleUtil.spawnParticle(world, entityPos, direction, (ParticleEffect) particle, velocityVec, 0.55);
        }
    }

    public boolean shouldApplyEffect(Entity entity) {
        return switch (entitySelection.getVal()) {
            case PLAYERS -> entity instanceof PlayerEntity;
            case ENTITIES -> !(entity instanceof PlayerEntity);
            case BOTH -> true;
        };
    }

    public DeathParticles() {
        super("death-particles", Categories.RENDER, "Spawn particles upon entity death.");
    }

    public enum Entities {
        PLAYERS,
        ENTITIES,
        BOTH
    }

    public enum Particles {
        TOTEM(ParticleTypes.TOTEM_OF_UNDYING),
        FIREWORK(ParticleTypes.FIREWORK),
        BIG_EXPLOSION(ParticleTypes.EXPLOSION_EMITTER),
        SMALL_EXPLOSION(ParticleTypes.EXPLOSION),
        FLASH(ParticleTypes.FLASH),
        WARDEN_BEAM(ParticleTypes.SONIC_BOOM);

        private final ParticleType<? extends ParticleEffect> particleType;

        Particles(ParticleType<? extends ParticleEffect> particleType) {
            this.particleType = particleType;
        }
    }
}
