package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.EnumSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeathEffects extends ListenerModule {

    private final SettingSection scGeneral = getGeneralSection();
    private final SettingSection rocketColors = createSettingSection("rocket-colors");
    public final ModuleSetting<EffectType> effectType = scGeneral.add(createEnumSetting(EffectType.class)
            .name("effect-type")
            .description("spawn selected effect on entity death position.")
            .def(EffectType.THUNDERBOLT)
            .build()
    );
    public final ModuleSetting<Entities> entitySelection = scGeneral.add(EnumSetting.create(Entities.class)
            .name("entity-selection")
            .description("Choose which entity will have this effect.")
            .def(Entities.BOTH)
            .build()
    );
    public final ModuleSetting<FireworkExplosionComponent.Type> shape = scGeneral.add(EnumSetting.create(FireworkExplosionComponent.Type.class)
            .name("rocket-shape")
            .description("Decide what will be the rocket shape")
            .def(FireworkExplosionComponent.Type.BURST)
            .build()
    );
    public final ModuleSetting<Integer> vRocket = scGeneral.add(createIntSetting()
            .name("rocket-velocity")
            .description("Set the rocket velocity")
            .max(5)
            .def(1)
            .min(0)
            .build()
    );
    public final ModuleSetting<Double> red = rocketColors.add(createDoubleSetting()
            .name("Red")
            .description("Decide how much red will be on the color pattern.")
            .def(255.0)
            .max(255.0)
            .min(0.0)
            .decimalPlaces(1)
            .build()
    );
    public final ModuleSetting<Double> green = rocketColors.add(createDoubleSetting()
            .name("Green")
            .description("Decide how much green will be on the color pattern.")
            .def(255.0)
            .max(255.0)
            .min(0.0)
            .decimalPlaces(1)
            .build()
    );
    public final ModuleSetting<Double> blue = rocketColors.add(createDoubleSetting()
            .name("Blue")
            .description("Decide how much blue will be on the color pattern.")
            .def(255.0)
            .max(255.0)
            .min(0.0)
            .decimalPlaces(1)
            .build()
    );

    public DeathEffects() {
        super("death-effects", Categories.MISC, "Spawn lightning/rocket particle on entity death");
    }

    private static final Map<Entity, Long> lightningRender = new ConcurrentHashMap<>();
    private static final long LIGHTNING_LIFETIME = 4500;

    @EventHandler
    private void onReceivePacket(PacketReceiveEvent event) {
        if (!(event.getPacket() instanceof EntityStatusS2CPacket packet)) {
            return;
        }

        if (packet.getStatus() != EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
            return;
        }

        Entity entity = packet.getEntity(PlayerUtils.getWorld());

        if (entity == null || !shouldApplyEffect(entity))
            return;

        if (effectType.getVal().isRocket()) spawnFirework(entity);
        else spawnLightning(entity);
    }

    public int rocketColor() {
        int redValue = red.getVal().intValue();
        int greenValue = green.getVal().intValue();
        int blueValue = blue.getVal().intValue();
        return (redValue << 16) | (greenValue << 8) | blueValue;
    }
    private int fadeColor(int baseColor) {
        int red = (baseColor >> 16) & 0xFF;
        int green = (baseColor >> 8) & 0xFF;
        int blue = baseColor & 0xFF;
        red = Math.min(255, red + 30);
        green = Math.min(255, green + 30);
        blue = Math.min(255, blue + 30);
        return (red << 16) | (green << 8) | blue;
    }

    private void spawnFirework(Entity ent){
        IntList colors = new IntArrayList();
        colors.add(rocketColor());
        IntList fadeColors = new IntArrayList();
        fadeColors.add(fadeColor(rocketColor()));
        FireworkExplosionComponent fireworkExplosion = new FireworkExplosionComponent(shape.getVal(),colors,fadeColors,true,true);
        mc.world.addFireworkParticle(ent.getX(),ent.getY(),ent.getZ(),0,vRocket.getVal(),0, Collections.singletonList(fireworkExplosion));
    }

    private void spawnLightning(Entity ent) {
        LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, mc.world);
        lightningEntity.refreshPositionAfterTeleport(ent.getX(), ent.getY(), ent.getZ());
        mc.world.addEntity(lightningEntity);
        lightningRender.put(ent, System.currentTimeMillis());
    }

    @EventHandler
    public void onUpdate(ClientTickEndEvent e) {
        Iterator<Map.Entry<Entity, Long>> iterator = lightningRender.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Entity, Long> entry = iterator.next();
            long currentTime = System.currentTimeMillis();
            if (currentTime - entry.getValue() > LIGHTNING_LIFETIME) {
                iterator.remove();
            }
        }
    }

    public boolean shouldApplyEffect(Entity entity) {
        return switch (entitySelection.getVal()) {
            case PLAYERS -> entity instanceof PlayerEntity;
            case ENTITIES -> !(entity instanceof PlayerEntity);
            case BOTH -> true;
        };
    }

    public enum EffectType {
        ROCKET,
        THUNDERBOLT;

        boolean isRocket() {
            return this != THUNDERBOLT;
        }
    }


        public enum Entities {
        PLAYERS,
        ENTITIES,
        BOTH
    }
}
