package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.gui.misc.Color;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.EnumSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.FireworkExplosion;

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
    public final ModuleSetting<FireworkExplosion.Shape> shape = scGeneral.add(EnumSetting.create(FireworkExplosion.Shape.class)
            .name("rocket-shape")
            .description("Decide what will be the rocket shape")
            .def(FireworkExplosion.Shape.BURST)
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
    public final ModuleSetting<Color> color = rocketColors.add(createColorSetting()
            .name("color")
            .description("Color of the rockets")
            .def(Color.WHITE)
            .build()
    );

    public DeathEffects() {
        super("death-effects", Categories.MISC, "Spawn lightning/rocket particle on entity death");
    }

    private static final Map<Entity, Long> lightningRender = new ConcurrentHashMap<>();
    private static final long LIGHTNING_LIFETIME = 4500;

    @EventHandler
    private void onReceivePacket(PacketReceiveEvent event) {
        if (!(event.getPacket() instanceof ClientboundEntityEventPacket packet)) {
            return;
        }

        if (packet.getEventId() != EntityEvent.DEATH) {
            return;
        }

        Entity entity = packet.getEntity(PlayerUtils.getWorld());

        if (entity == null || !shouldApplyEffect(entity))
            return;

        if (effectType.getVal().isRocket()) spawnFirework(entity);
        else spawnLightning(entity);
    }

    private void spawnFirework(Entity ent){
        IntList colors = new IntArrayList();
        IntList fadeColors = new IntArrayList();
        colors.add(color.getVal().getHexOpaque());
        fadeColors.add(color.getVal().brighter(2).getHexOpaque());

        FireworkExplosion fireworkExplosion = new FireworkExplosion(shape.getVal(), colors, fadeColors,true,true);
        mc.level.createFireworks(ent.getX(), ent.getY(), ent.getZ(), 0, vRocket.getVal(), 0, Collections.singletonList(fireworkExplosion), true);
    }

    private void spawnLightning(Entity ent) {
        LightningBolt lightningEntity = new LightningBolt(EntityTypes.LIGHTNING_BOLT, mc.level);
        lightningEntity.snapTo(ent.getX(), ent.getY(), ent.getZ());
        mc.level.addEntity(lightningEntity);
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
            case PLAYERS -> entity instanceof Player;
            case ENTITIES -> !(entity instanceof Player);
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
