package io.github.itzispyder.clickcrystals.gui.misc.brushes;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class MobHeadBrush implements Global {

    public static final Map<Class<? extends Entity>, Identifier> REGISTRY = new HashMap<>() {{
        this.put(EnderDragonEntity.class, Identifier.of(modId, "textures/display/icons/entities/ender_dragon.png"));
        this.put(WitherEntity.class, Identifier.of(modId, "textures/display/icons/entities/wither.png"));
        this.put(AmbientEntity.class, Identifier.of(modId, "textures/display/icons/entities/ambient.png"));
        this.put(BlazeEntity.class, Identifier.of(modId, "textures/display/icons/entities/blaze.png"));
        this.put(BoggedEntity.class, Identifier.of(modId, "textures/display/icons/entities/bogged.png"));
        this.put(BreezeEntity.class, Identifier.of(modId, "textures/display/icons/entities/breeze.png"));
        this.put(CaveSpiderEntity.class, Identifier.of(modId, "textures/display/icons/entities/cave_spider.png"));
        this.put(CreakingEntity.class, Identifier.of(modId, "textures/display/icons/entities/creaking.png"));
        this.put(CreeperEntity.class, Identifier.of(modId, "textures/display/icons/entities/creeper.png"));
        this.put(DrownedEntity.class, Identifier.of(modId, "textures/display/icons/entities/drowned.png"));
        this.put(ElderGuardianEntity.class, Identifier.of(modId, "textures/display/icons/entities/elder_guardian.png"));
        this.put(EndermanEntity.class, Identifier.of(modId, "textures/display/icons/entities/enderman.png"));
        this.put(EndermiteEntity.class, Identifier.of(modId, "textures/display/icons/entities/endermite.png"));
        this.put(EvokerEntity.class, Identifier.of(modId, "textures/display/icons/entities/evoker.png"));
        this.put(EvokerFangsEntity.class, Identifier.of(modId, "textures/display/icons/entities/evoker_fangs.png"));
        this.put(GhastEntity.class, Identifier.of(modId, "textures/display/icons/entities/ghast.png"));
        this.put(GiantEntity.class, Identifier.of(modId, "textures/display/icons/entities/giant.png"));
        this.put(GuardianEntity.class, Identifier.of(modId, "textures/display/icons/entities/guardian.png"));
        this.put(HoglinEntity.class, Identifier.of(modId, "textures/display/icons/entities/hoglin.png"));
        this.put(HostileEntity.class, Identifier.of(modId, "textures/display/icons/entities/hostile.png"));
        this.put(HuskEntity.class, Identifier.of(modId, "textures/display/icons/entities/husk.png"));
        this.put(IllagerEntity.class, Identifier.of(modId, "textures/display/icons/entities/illager.png"));
        this.put(IllusionerEntity.class, Identifier.of(modId, "textures/display/icons/entities/illusioner.png"));
        this.put(MagmaCubeEntity.class, Identifier.of(modId, "textures/display/icons/entities/magma_cube.png"));
        this.put(MobEntity.class, Identifier.of(modId, "textures/display/icons/entities/mob.png"));
        this.put(PathAwareEntity.class, Identifier.of(modId, "textures/display/icons/entities/path_aware.png"));
        this.put(PhantomEntity.class, Identifier.of(modId, "textures/display/icons/entities/phantom.png"));
        this.put(PiglinBruteEntity.class, Identifier.of(modId, "textures/display/icons/entities/piglin_brute.png"));
        this.put(PiglinEntity.class, Identifier.of(modId, "textures/display/icons/entities/piglin.png"));
        this.put(PillagerEntity.class, Identifier.of(modId, "textures/display/icons/entities/pillager.png"));
        this.put(RavagerEntity.class, Identifier.of(modId, "textures/display/icons/entities/ravager.png"));
        this.put(ShulkerEntity.class, Identifier.of(modId, "textures/display/icons/entities/shulker.png"));
        this.put(SilverfishEntity.class, Identifier.of(modId, "textures/display/icons/entities/silverfish.png"));
        this.put(SkeletonEntity.class, Identifier.of(modId, "textures/display/icons/entities/skeleton.png"));
        this.put(SkeletonHorseEntity.class, Identifier.of(modId, "textures/display/icons/entities/skeleton_horse.png"));
        this.put(SlimeEntity.class, Identifier.of(modId, "textures/display/icons/entities/slime.png"));
        this.put(SpellcastingIllagerEntity.class, Identifier.of(modId, "textures/display/icons/entities/spellcasting_illager.png"));
        this.put(SpiderEntity.class, Identifier.of(modId, "textures/display/icons/entities/spider.png"));
        this.put(StrayEntity.class, Identifier.of(modId, "textures/display/icons/entities/stray.png"));
        this.put(VexEntity.class, Identifier.of(modId, "textures/display/icons/entities/vex.png"));
        this.put(VindicatorEntity.class, Identifier.of(modId, "textures/display/icons/entities/vindicator.png"));
        this.put(WardenEntity.class, Identifier.of(modId, "textures/display/icons/entities/warden.png"));
        this.put(WaterCreatureEntity.class, Identifier.of(modId, "textures/display/icons/entities/water_creature.png"));
        this.put(WitchEntity.class, Identifier.of(modId, "textures/display/icons/entities/witch.png"));
        this.put(WitherSkeletonEntity.class, Identifier.of(modId, "textures/display/icons/entities/wither_skeleton.png"));
        this.put(ZoglinEntity.class, Identifier.of(modId, "textures/display/icons/entities/zoglin.png"));
        this.put(ZombieEntity.class, Identifier.of(modId, "textures/display/icons/entities/zombie.png"));
        this.put(ZombieHorseEntity.class, Identifier.of(modId, "textures/display/icons/entities/zombie_horse.png"));
        this.put(ZombieVillagerEntity.class, Identifier.of(modId, "textures/display/icons/entities/zombie_villager.png"));
        this.put(ZombifiedPiglinEntity.class, Identifier.of(modId, "textures/display/icons/entities/zombified_piglin.png"));
        this.put(AllayEntity.class, Identifier.of(modId, "textures/display/icons/entities/allay.png"));
        this.put(AnimalEntity.class, Identifier.of(modId, "textures/display/icons/entities/animal.png"));
        this.put(ArmadilloEntity.class, Identifier.of(modId, "textures/display/icons/entities/armadillo.png"));
        this.put(AxolotlEntity.class, Identifier.of(modId, "textures/display/icons/entities/axolotl.png"));
        this.put(BatEntity.class, Identifier.of(modId, "textures/display/icons/entities/bat.png"));
        this.put(BeeEntity.class, Identifier.of(modId, "textures/display/icons/entities/bee.png"));
        this.put(CamelEntity.class, Identifier.of(modId, "textures/display/icons/entities/camel.png"));
        this.put(CatEntity.class, Identifier.of(modId, "textures/display/icons/entities/cat.png"));
        this.put(ChickenEntity.class, Identifier.of(modId, "textures/display/icons/entities/chicken.png"));
        this.put(CodEntity.class, Identifier.of(modId, "textures/display/icons/entities/cod.png"));
        this.put(CowEntity.class, Identifier.of(modId, "textures/display/icons/entities/cow.png"));
        this.put(DolphinEntity.class, Identifier.of(modId, "textures/display/icons/entities/dolphin.png"));
        this.put(DonkeyEntity.class, Identifier.of(modId, "textures/display/icons/entities/donkey.png"));
        this.put(FishEntity.class, Identifier.of(modId, "textures/display/icons/entities/fish.png"));
        this.put(FoxEntity.class, Identifier.of(modId, "textures/display/icons/entities/fox.png"));
        this.put(FrogEntity.class, Identifier.of(modId, "textures/display/icons/entities/frog.png"));
        this.put(GlowSquidEntity.class, Identifier.of(modId, "textures/display/icons/entities/glow_squid.png"));
        this.put(GoatEntity.class, Identifier.of(modId, "textures/display/icons/entities/goat.png"));
        this.put(GolemEntity.class, Identifier.of(modId, "textures/display/icons/entities/golem.png"));
        this.put(HappyGhastEntity.class, Identifier.of(modId, "textures/display/icons/entities/happy_ghast.png"));
        this.put(HorseEntity.class, Identifier.of(modId, "textures/display/icons/entities/horse.png"));
        this.put(IronGolemEntity.class, Identifier.of(modId, "textures/display/icons/entities/iron_golem.png"));
        this.put(LlamaEntity.class, Identifier.of(modId, "textures/display/icons/entities/llama.png"));
        this.put(MerchantEntity.class, Identifier.of(modId, "textures/display/icons/entities/merchant.png"));
        this.put(MooshroomEntity.class, Identifier.of(modId, "textures/display/icons/entities/mooshroom.png"));
        this.put(MuleEntity.class, Identifier.of(modId, "textures/display/icons/entities/mule.png"));
        this.put(OcelotEntity.class, Identifier.of(modId, "textures/display/icons/entities/ocelot.png"));
        this.put(PandaEntity.class, Identifier.of(modId, "textures/display/icons/entities/panda.png"));
        this.put(ParrotEntity.class, Identifier.of(modId, "textures/display/icons/entities/parrot.png"));
        this.put(PassiveEntity.class, Identifier.of(modId, "textures/display/icons/entities/passive.png"));
        this.put(PigEntity.class, Identifier.of(modId, "textures/display/icons/entities/pig.png"));
        this.put(PolarBearEntity.class, Identifier.of(modId, "textures/display/icons/entities/polar_bear.png"));
        this.put(PufferfishEntity.class, Identifier.of(modId, "textures/display/icons/entities/pufferfish.png"));
        this.put(RabbitEntity.class, Identifier.of(modId, "textures/display/icons/entities/rabbit.png"));
        this.put(SalmonEntity.class, Identifier.of(modId, "textures/display/icons/entities/salmon.png"));
        this.put(SchoolingFishEntity.class, Identifier.of(modId, "textures/display/icons/entities/schooling_fish.png"));
        this.put(SheepEntity.class, Identifier.of(modId, "textures/display/icons/entities/sheep.png"));
        this.put(SnifferEntity.class, Identifier.of(modId, "textures/display/icons/entities/sniffer.png"));
        this.put(SnowGolemEntity.class, Identifier.of(modId, "textures/display/icons/entities/snow_golem.png"));
        this.put(SquidEntity.class, Identifier.of(modId, "textures/display/icons/entities/squid.png"));
        this.put(StriderEntity.class, Identifier.of(modId, "textures/display/icons/entities/strider.png"));
        this.put(TadpoleEntity.class, Identifier.of(modId, "textures/display/icons/entities/tadpole.png"));
        this.put(TameableEntity.class, Identifier.of(modId, "textures/display/icons/entities/tameable.png"));
        this.put(TameableShoulderEntity.class, Identifier.of(modId, "textures/display/icons/entities/tameable_shoulder.png"));
        this.put(TraderLlamaEntity.class, Identifier.of(modId, "textures/display/icons/entities/trader_llama.png"));
        this.put(TropicalFishEntity.class, Identifier.of(modId, "textures/display/icons/entities/tropical_fish.png"));
        this.put(TurtleEntity.class, Identifier.of(modId, "textures/display/icons/entities/turtle.png"));
        this.put(VillagerEntity.class, Identifier.of(modId, "textures/display/icons/entities/villager.png"));
        this.put(WanderingTraderEntity.class, Identifier.of(modId, "textures/display/icons/entities/wandering_trader.png"));
        this.put(WaterAnimalEntity.class, Identifier.of(modId, "textures/display/icons/entities/water_animal.png"));
        this.put(WolfEntity.class, Identifier.of(modId, "textures/display/icons/entities/wolf.png"));
    }};

    public static Identifier getIdentifier(Class<? extends Entity> entity) {
        return REGISTRY.get(entity);
    }

    public static void drawHead(DrawContext context, Class<? extends Entity> entity, int x, int y, int size) {
        Identifier tex = getIdentifier(entity);
        if (tex != null) {
            RenderUtils.drawTexture(context,tex, x, y, size, size);
        }
    }

    public static void drawHead(DrawContext context, Entity entity, int x, int y, int size) {
        drawHead(context, entity.getClass(), x, y, size);
    }
}
