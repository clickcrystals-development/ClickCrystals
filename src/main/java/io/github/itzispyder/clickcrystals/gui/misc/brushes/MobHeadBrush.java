package io.github.itzispyder.clickcrystals.gui.misc.brushes;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.util.misc.ManualMap;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.util.Identifier;

import java.util.Map;

public class MobHeadBrush implements Global {

    public static final Map<Class<? extends Entity>, Identifier> REGISTRY = ManualMap.fromItems(
            AllayEntity.class, Identifier.of(modId, "textures/display/icons/entities/allay.png"),
            AxolotlEntity.class, Identifier.of(modId, "textures/display/icons/entities/axolotl.png"),
            BatEntity.class, Identifier.of(modId, "textures/display/icons/entities/bat.png"),
            BeeEntity.class, Identifier.of(modId, "textures/display/icons/entities/bee.png"),
            BlazeEntity.class, Identifier.of(modId, "textures/display/icons/entities/blaze.png"),
            MooshroomEntity.class, Identifier.of(modId, "textures/display/icons/entities/brown_mooshroom.png"),
            CamelEntity.class, Identifier.of(modId, "textures/display/icons/entities/camel.png"),
            CatEntity.class, Identifier.of(modId, "textures/display/icons/entities/cat.png"),
            CaveSpiderEntity.class, Identifier.of(modId, "textures/display/icons/entities/cave_spider.png"),
            ChickenEntity.class, Identifier.of(modId, "textures/display/icons/entities/chicken.png"),
            CodEntity.class, Identifier.of(modId, "textures/display/icons/entities/cod.png"),
            CowEntity.class, Identifier.of(modId, "textures/display/icons/entities/cow.png"),
            CreeperEntity.class, Identifier.of(modId, "textures/display/icons/entities/creeper.png"),
            DolphinEntity.class, Identifier.of(modId, "textures/display/icons/entities/dolphin.png"),
            DrownedEntity.class, Identifier.of(modId, "textures/display/icons/entities/drowned.png"),
            EnderDragonEntity.class, Identifier.of(modId, "textures/display/icons/entities/enderdragon.png"),
            EndermanEntity.class, Identifier.of(modId, "textures/display/icons/entities/enderman.png"),
            EndermiteEntity.class, Identifier.of(modId, "textures/display/icons/entities/endermite.png"),
            EvokerEntity.class, Identifier.of(modId, "textures/display/icons/entities/evoker.png"),
            FoxEntity.class, Identifier.of(modId, "textures/display/icons/entities/fox.png"),
            FrogEntity.class, Identifier.of(modId, "textures/display/icons/entities/frog.png"),
            GhastEntity.class, Identifier.of(modId, "textures/display/icons/entities/ghast.png"),
            GlowSquidEntity.class, Identifier.of(modId, "textures/display/icons/entities/glow_squid.png"),
            GoatEntity.class, Identifier.of(modId, "textures/display/icons/entities/goat.png"),
            GuardianEntity.class, Identifier.of(modId, "textures/display/icons/entities/guardian.png"),
            HoglinEntity.class, Identifier.of(modId, "textures/display/icons/entities/hoglin.png"),
            HorseEntity.class, Identifier.of(modId, "textures/display/icons/entities/horse.png"),
            HuskEntity.class, Identifier.of(modId, "textures/display/icons/entities/husk.png"),
            IllusionerEntity.class, Identifier.of(modId, "textures/display/icons/entities/illusioner.png"),
            IronGolemEntity.class, Identifier.of(modId, "textures/display/icons/entities/iron_golem.png"),
            LlamaEntity.class, Identifier.of(modId, "textures/display/icons/entities/llama.png"),
            MagmaCubeEntity.class, Identifier.of(modId, "textures/display/icons/entities/magmacube.png"),
            OcelotEntity.class, Identifier.of(modId, "textures/display/icons/entities/ocelot.png"),
            PandaEntity.class, Identifier.of(modId, "textures/display/icons/entities/panda.png"),
            ParrotEntity.class, Identifier.of(modId, "textures/display/icons/entities/parrot.png"),
            PhantomEntity.class, Identifier.of(modId, "textures/display/icons/entities/phantom.png"),
            PigEntity.class, Identifier.of(modId, "textures/display/icons/entities/pig.png"),
            PiglinEntity.class, Identifier.of(modId, "textures/display/icons/entities/piglin.png"),
            PiglinBruteEntity.class, Identifier.of(modId, "textures/display/icons/entities/piglin_brute.png"),
            PillagerEntity.class, Identifier.of(modId, "textures/display/icons/entities/pillager.png"),
            PolarBearEntity.class, Identifier.of(modId, "textures/display/icons/entities/polarbear.png"),
            PufferfishEntity.class, Identifier.of(modId, "textures/display/icons/entities/pufferfish.png"),
            RabbitEntity.class, Identifier.of(modId, "textures/display/icons/entities/rabbit.png"),
            RavagerEntity.class, Identifier.of(modId, "textures/display/icons/entities/ravager.png"),
            SalmonEntity.class, Identifier.of(modId, "textures/display/icons/entities/salmon.png"),
            TurtleEntity.class, Identifier.of(modId, "textures/display/icons/entities/sea_turtle.png"),
            SheepEntity.class, Identifier.of(modId, "textures/display/icons/entities/sheep.png"),
            ShulkerEntity.class, Identifier.of(modId, "textures/display/icons/entities/shulker.png"),
            SilverfishEntity.class, Identifier.of(modId, "textures/display/icons/entities/silverfish.png"),
            SkeletonEntity.class, Identifier.of(modId, "textures/display/icons/entities/skeleton.png"),
            SlimeEntity.class, Identifier.of(modId, "textures/display/icons/entities/slime.png"),
            SnifferEntity.class, Identifier.of(modId, "textures/display/icons/entities/sniffer.png"),
            SnowGolemEntity.class, Identifier.of(modId, "textures/display/icons/entities/snow_golem.png"),
            SpiderEntity.class, Identifier.of(modId, "textures/display/icons/entities/spider.png"),
            SquidEntity.class, Identifier.of(modId, "textures/display/icons/entities/squid.png"),
            StrayEntity.class, Identifier.of(modId, "textures/display/icons/entities/stray.png"),
            StriderEntity.class, Identifier.of(modId, "textures/display/icons/entities/strider.png"),
            TropicalFishEntity.class, Identifier.of(modId, "textures/display/icons/entities/tropical_fish.png"),
            VexEntity.class, Identifier.of(modId, "textures/display/icons/entities/vex.png"),
            VillagerEntity.class, Identifier.of(modId, "textures/display/icons/entities/villager.png"),
            VindicatorEntity.class, Identifier.of(modId, "textures/display/icons/entities/vindicator.png"),
            WanderingTraderEntity.class, Identifier.of(modId, "textures/display/icons/entities/wandering_trader.png"),
            WardenEntity.class, Identifier.of(modId, "textures/display/icons/entities/warden.png"),
            WitchEntity.class, Identifier.of(modId, "textures/display/icons/entities/witch.png"),
            WitherEntity.class, Identifier.of(modId, "textures/display/icons/entities/wither.png"),
            WitherSkeletonEntity.class, Identifier.of(modId, "textures/display/icons/entities/wither_skeleton.png"),
            WolfEntity.class, Identifier.of(modId, "textures/display/icons/entities/wolf.png"),
            ZoglinEntity.class, Identifier.of(modId, "textures/display/icons/entities/zoglin.png"),
            ZombieEntity.class, Identifier.of(modId, "textures/display/icons/entities/zombie.png"),
            ZombieVillagerEntity.class, Identifier.of(modId, "textures/display/icons/entities/zombie_villager.png"),
            ZombifiedPiglinEntity.class, Identifier.of(modId, "textures/display/icons/entities/zombified_piglin.png")
    );

    public static Identifier getIdentifier(Class<? extends Entity> entity) {
        return REGISTRY.get(entity);
    }

    public static void drawHead(DrawContext context, Class<? extends Entity> entity, int x, int y, int size) {
        Identifier tex = getIdentifier(entity);
        if (tex != null) {
            context.drawTexture(tex, x, y, 0, 0, size, size, size, size);
        }
    }

    public static void drawHead(DrawContext context, Entity entity, int x, int y, int size) {
        drawHead(context, entity.getClass(), x, y, size);
    }
}
