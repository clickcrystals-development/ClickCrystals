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
            AllayEntity.class, new Identifier(modId, "textures/display/icons/entities/allay.png"),
            AxolotlEntity.class, new Identifier(modId, "textures/display/icons/entities/axolotl.png"),
            BatEntity.class, new Identifier(modId, "textures/display/icons/entities/bat.png"),
            BeeEntity.class, new Identifier(modId, "textures/display/icons/entities/bee.png"),
            BlazeEntity.class, new Identifier(modId, "textures/display/icons/entities/blaze.png"),
            MooshroomEntity.class, new Identifier(modId, "textures/display/icons/entities/brown_mooshroom.png"),
            CamelEntity.class, new Identifier(modId, "textures/display/icons/entities/camel.png"),
            CatEntity.class, new Identifier(modId, "textures/display/icons/entities/cat.png"),
            CaveSpiderEntity.class, new Identifier(modId, "textures/display/icons/entities/cave_spider.png"),
            ChickenEntity.class, new Identifier(modId, "textures/display/icons/entities/chicken.png"),
            CodEntity.class, new Identifier(modId, "textures/display/icons/entities/cod.png"),
            CowEntity.class, new Identifier(modId, "textures/display/icons/entities/cow.png"),
            CreeperEntity.class, new Identifier(modId, "textures/display/icons/entities/creeper.png"),
            DolphinEntity.class, new Identifier(modId, "textures/display/icons/entities/dolphin.png"),
            DrownedEntity.class, new Identifier(modId, "textures/display/icons/entities/drowned.png"),
            EnderDragonEntity.class, new Identifier(modId, "textures/display/icons/entities/enderdragon.png"),
            EndermanEntity.class, new Identifier(modId, "textures/display/icons/entities/enderman.png"),
            EndermiteEntity.class, new Identifier(modId, "textures/display/icons/entities/endermite.png"),
            EvokerEntity.class, new Identifier(modId, "textures/display/icons/entities/evoker.png"),
            FoxEntity.class, new Identifier(modId, "textures/display/icons/entities/fox.png"),
            FrogEntity.class, new Identifier(modId, "textures/display/icons/entities/frog.png"),
            GhastEntity.class, new Identifier(modId, "textures/display/icons/entities/ghast.png"),
            GlowSquidEntity.class, new Identifier(modId, "textures/display/icons/entities/glow_squid.png"),
            GoatEntity.class, new Identifier(modId, "textures/display/icons/entities/goat.png"),
            GuardianEntity.class, new Identifier(modId, "textures/display/icons/entities/guardian.png"),
            HoglinEntity.class, new Identifier(modId, "textures/display/icons/entities/hoglin.png"),
            HorseEntity.class, new Identifier(modId, "textures/display/icons/entities/horse.png"),
            HuskEntity.class, new Identifier(modId, "textures/display/icons/entities/husk.png"),
            IllusionerEntity.class, new Identifier(modId, "textures/display/icons/entities/illusioner.png"),
            IronGolemEntity.class, new Identifier(modId, "textures/display/icons/entities/iron_golem.png"),
            LlamaEntity.class, new Identifier(modId, "textures/display/icons/entities/llama.png"),
            MagmaCubeEntity.class, new Identifier(modId, "textures/display/icons/entities/magmacube.png"),
            OcelotEntity.class, new Identifier(modId, "textures/display/icons/entities/ocelot.png"),
            PandaEntity.class, new Identifier(modId, "textures/display/icons/entities/panda.png"),
            ParrotEntity.class, new Identifier(modId, "textures/display/icons/entities/parrot.png"),
            PhantomEntity.class, new Identifier(modId, "textures/display/icons/entities/phantom.png"),
            PigEntity.class, new Identifier(modId, "textures/display/icons/entities/pig.png"),
            PiglinEntity.class, new Identifier(modId, "textures/display/icons/entities/piglin.png"),
            PiglinBruteEntity.class, new Identifier(modId, "textures/display/icons/entities/piglin_brute.png"),
            PillagerEntity.class, new Identifier(modId, "textures/display/icons/entities/pillager.png"),
            PolarBearEntity.class, new Identifier(modId, "textures/display/icons/entities/polarbear.png"),
            PufferfishEntity.class, new Identifier(modId, "textures/display/icons/entities/pufferfish.png"),
            RabbitEntity.class, new Identifier(modId, "textures/display/icons/entities/rabbit.png"),
            RavagerEntity.class, new Identifier(modId, "textures/display/icons/entities/ravager.png"),
            SalmonEntity.class, new Identifier(modId, "textures/display/icons/entities/salmon.png"),
            TurtleEntity.class, new Identifier(modId, "textures/display/icons/entities/sea_turtle.png"),
            SheepEntity.class, new Identifier(modId, "textures/display/icons/entities/sheep.png"),
            ShulkerEntity.class, new Identifier(modId, "textures/display/icons/entities/shulker.png"),
            SilverfishEntity.class, new Identifier(modId, "textures/display/icons/entities/silverfish.png"),
            SkeletonEntity.class, new Identifier(modId, "textures/display/icons/entities/skeleton.png"),
            SlimeEntity.class, new Identifier(modId, "textures/display/icons/entities/slime.png"),
            SnifferEntity.class, new Identifier(modId, "textures/display/icons/entities/sniffer.png"),
            SnowGolemEntity.class, new Identifier(modId, "textures/display/icons/entities/snow_golem.png"),
            SpiderEntity.class, new Identifier(modId, "textures/display/icons/entities/spider.png"),
            SquidEntity.class, new Identifier(modId, "textures/display/icons/entities/squid.png"),
            StrayEntity.class, new Identifier(modId, "textures/display/icons/entities/stray.png"),
            StriderEntity.class, new Identifier(modId, "textures/display/icons/entities/strider.png"),
            TropicalFishEntity.class, new Identifier(modId, "textures/display/icons/entities/tropical_fish.png"),
            VexEntity.class, new Identifier(modId, "textures/display/icons/entities/vex.png"),
            VillagerEntity.class, new Identifier(modId, "textures/display/icons/entities/villager.png"),
            VindicatorEntity.class, new Identifier(modId, "textures/display/icons/entities/vindicator.png"),
            WanderingTraderEntity.class, new Identifier(modId, "textures/display/icons/entities/wandering_trader.png"),
            WardenEntity.class, new Identifier(modId, "textures/display/icons/entities/warden.png"),
            WitchEntity.class, new Identifier(modId, "textures/display/icons/entities/witch.png"),
            WitherEntity.class, new Identifier(modId, "textures/display/icons/entities/wither.png"),
            WitherSkeletonEntity.class, new Identifier(modId, "textures/display/icons/entities/wither_skeleton.png"),
            WolfEntity.class, new Identifier(modId, "textures/display/icons/entities/wolf.png"),
            ZoglinEntity.class, new Identifier(modId, "textures/display/icons/entities/zoglin.png"),
            ZombieEntity.class, new Identifier(modId, "textures/display/icons/entities/zombie.png"),
            ZombieVillagerEntity.class, new Identifier(modId, "textures/display/icons/entities/zombie_villager.png"),
            ZombifiedPiglinEntity.class, new Identifier(modId, "textures/display/icons/entities/zombified_piglin.png")
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
