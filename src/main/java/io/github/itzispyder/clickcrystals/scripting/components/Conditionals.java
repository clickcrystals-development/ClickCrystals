package io.github.itzispyder.clickcrystals.scripting.components;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgsReader;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.components.conditionalcontexts.*;
import io.github.itzispyder.clickcrystals.util.minecraft.*;
import io.github.itzispyder.clickcrystals.util.misc.Voidable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.GameMode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class Conditionals implements Global {

    // registry

    private static final Map<String, Conditional> registry = new HashMap<>();

    private static Conditional register(String name, Conditional conditional) {
        if (name != null && conditional != null && !name.isEmpty())
            registry.put(name.toLowerCase(), conditional);
        return conditional;
    }

    public static boolean isRegistered(String name) {
        return registry.containsKey(name.toLowerCase());
    }

    public static ConditionEvaluationResult evaluate(Entity ref, ScriptArgs args, int beginIndex) {
        ScriptArgsReader read = args.getReader();
        read.skipTo(beginIndex);
        String name = read.next(registry.keySet());
        Conditional condition = registry.get(name);

        if (condition == null)
            throw new IllegalArgumentException("cannot recognize conditional [%s]".formatted(read.getCurrentRead()));

        ConditionEvaluationContext context = new ConditionEvaluationContext(ref, args, beginIndex + read.getArgDiff());
        return condition.evaluate(context);
    }


    // conditionals


    // @Format (if|if_not) true {}
    // @Format (while|while_not) <num>? true {}
    public static final Conditional TRUE;
    // @Format (if|if_not) false {}
    // @Format (while|while_not) <num>? false {}
    public static final Conditional FALSE;
    // @Format (if|if_not) holding <identifier> {}
    // @Format (while|while_not) <num>? holding <identifier> {}
    public static final Conditional HOLDING;
    // @Format (if|if_not) off_holding <identifier> {}
    // @Format (while|while_not) <num>? off_holding <identifier> {}
    public static final Conditional OFF_HOLDING;
    // @Format (if|if_not) target_block <identifier> {}
    // @Format (while|while_not) <num>? target_block <identifier> {}
    public static final Conditional TARGET_BLOCK;
    // @Format (if|if_not) target_entity <identifier> {}
    // @Format (while|while_not) <num>? target_entity <identifier> {}
    public static final Conditional TARGET_ENTITY;
    // @Format (if|if_not) target_fluid <identifier> {}
    // @Format (while|while_not) <num>? target_fluid <identifier> {}
    public static final Conditional TARGET_FLUID;
    // @Format (if|if_not) targeting_block {}
    // @Format (while|while_not) <num>? targeting_block {}
    public static final Conditional TARGETING_BLOCK;
    // @Format (if|if_not) targeting_entity {}
    // @Format (while|while_not) <num>? targeting_entity {}
    public static final Conditional TARGETING_ENTITY;
    // @Format (if|if_not) targeting_fluid {}
    // @Format (while|while_not) <num>? targeting_fluid {}
    public static final Conditional TARGETING_FLUID;
    // @Format (if|if_not) inventory_has <identifier> {}
    // @Format (while|while_not) <num>? inventory_has <identifier> {}
    public static final Conditional INVENTORY_HAS;
    // @Format (if|if_not) inventory_count <identifier> <comparator> <int> {}
    // @Format (while|while_not) <num>? inventory_count <identifier> <comparator> <int> {}
    public static final Conditional INVENTORY_COUNT;
    // @Format (if|if_not) equipment_has <identifier> {}
    // @Format (while|while_not) <num>? equipment_has <identifier> {}
    public static final Conditional EQUIPMENT_HAS;
    // @Format (if|if_not) hotbar_has <identifier> {}
    // @Format (while|while_not) <num>? hotbar_has <identifier> {}
    public static final Conditional HOTBAR_HAS;
    // @Format (if|if_not) hotbar_count <identifier> <comparator> <int> {}
    // @Format (while|while_not) <num>? hotbar_count <identifier> <comparator> <int> {}
    public static final Conditional HOTBAR_COUNT;

    // @Format (if|if_not) input_active <input> {}
    // @Format (if|if_not) input_active key ... {}
    // @Format (while|while_not) <num>? input_active <input> {}
    // @Format (while|while_not) <num>? input_active key ... {}
    public static final Conditional INPUT_ACTIVE;

    // @Format (if|if_not) block_in_range <identifier> <num> {}
    // @Format (while|while_not) <num>? block_in_range <identifier> <num> {}
    public static final Conditional BLOCK_IN_RANGE;
    // @Format (if|if_not) entity_in_range <identifier> <num> {}
    // @Format (while|while_not) <num>? entity_in_range <identifier> <num> {}
    public static final Conditional ENTITY_IN_RANGE;
    // @Format (if|if_not) block_in_range <identifier> <num> {}
    // @Format (while|while_not) <num>? block_in_range <identifier> <num> {}
    public static final Conditional BLOCK_IN_FOV;
    // @Format (if|if_not) entity_in_fov <identifier> <num> {}
    // @Format (while|while_not) <num>? entity_in_fov <identifier> <num> {}
    public static final Conditional ENTITY_IN_FOV;
    // @Format (if|if_not) attack_progress <comparator> <num> {}
    // @Format (while|while_not) <num>? attack_progress <comparator> <num> {}
    public static final Conditional ATTACK_PROGRESS;
    // @Format (if|if_not) health <comparator> <num> {}
    // @Format (while|while_not) <num>? health <comparator> <num> {}
    public static final Conditional HEALTH;
    // @Format (if|if_not) hunger <comparator> <num> {}
    // @Format (while|while_not) <num>? hunger <comparator> <num> {}
    public static final Conditional HUNGER;
    // @Format (if|if_not) hurt_time <comparator> <int> {}
    // @Format (while|while_not) <num>? hurt_time <comparator> <int> {}
    public static final Conditional HURT_TIME;
    // @Format (if|if_not) armor <comparator> <int> {}
    // @Format (while|while_not) <num>? armor <comparator> <int> {}
    public static final Conditional ARMOR;
    // @Format (if|if_not) pos_x <comparator> <num> {}
    // @Format (while|while_not) <num>? pos_x <comparator> <num> {}
    public static final Conditional POS_X;
    // @Format (if|if_not) pos_y <comparator> <num> {}
    // @Format (while|while_not) <num>? pos_y <comparator> <num> {}
    public static final Conditional POS_Y;
    // @Format (if|if_not) pos_z <comparator> <num> {}
    // @Format (while|while_not) <num>? pos_z <comparator> <num> {}
    public static final Conditional POS_Z;
    // @Format (if|if_not) vel_x <comparator> <num> {}
    // @Format (while|while_not) <num>? vel_x <comparator> <num> {}
    public static final Conditional VEL_X;
    // @Format (if|if_not) vel_y <comparator> <num> {}
    // @Format (while|while_not) <num>? vel_y <comparator> <num> {}
    public static final Conditional VEL_Y;
    // @Format (if|if_not) vel_z <comparator> <num> {}
    // @Format (while|while_not) <num>? vel_z <comparator> <num> {}
    public static final Conditional VEL_Z;
    // @Format (if|if_not) module_enabled ... {}
    // @Format (while|while_not) <num>? module_enabled ... {}
    public static final Conditional MODULE_ENABLED;
    // @Format (if|if_not) module_disabled ... {}
    // @Format (while|while_not) <num>? module_disabled ... {}
    public static final Conditional MODULE_DISABLED;
    // @Format (if|if_not) block <x> <y> <z> {}
    // @Format (while|while_not) <num>? block <x> <y> <z> {}
    public static final Conditional BLOCK;
    // @Format (if|if_not) entity <x> <y> <z> {}
    // @Format (while|while_not) <num>? entity <x> <y> <z> {}
    public static final Conditional ENTITY;
    // @Format (if|if_not) dimension (overworld|the_nether|the_end) {}
    // @Format (while|while_not) <num>? dimension (overworld|the_nether|the_end) {}
    public static final Conditional DIMENSION;
    // @Format (if|if_not) effect_amplifier <identifier> <comparator> <int> {}
    // @Format (while|while_not) <num>? <identifier> <comparator> <int> {}
    public static final Conditional EFFECT_AMPLIFIER;
    // @Format (if|if_not) effect_duration <identifier> <comparator> <int> {}
    // @Format (while|while_not) <num>? <identifier> <comparator> <int> {}
    public static final Conditional EFFECT_DURATION;
    // @Format (if|if_not) in_game {}
    // @Format (while|while_not) <num>? in_game {}
    public static final Conditional IN_GAME;
    // @Format (if|if_not) in_singleplayer {}
    // @Format (while|while_not) <num>? in_singleplayer {}
    public static final Conditional IN_SINGLEPLAYER;
    // @Format (if|if_not) playing {}
    // @Format (while|while_not) <num>? playing {}
    public static final Conditional PLAYING;
    // @Format (if|if_not) in_screen {}
    // @Format (while|while_not) <num>? in_screen {}
    public static final Conditional IN_SCREEN;
    // @Format (if|if_not) chance_of <num> {}
    // @Format (while|while_not) <num>? chance_of <num> {}
    public static final Conditional CHANCE_OF;
    // @Format (if|if_not) colliding {}
    // @Format (while|while_not) <num>? colliding {}
    public static final Conditional COLLIDING;
    // @Format (if|if_not) colliding_horizontally {}
    // @Format (while|while_not) <num>? colliding_horizontally {}
    public static final Conditional COLLIDING_HORIZONTALLY;
    // @Format (if|if_not) colliding_vertically {}
    // @Format (while|while_not) <num>? colliding_vertically {}
    public static final Conditional COLLIDING_VERTICALLY;
    // @Format (if|if_not) jumping {}
    // @Format (while|while_not) <num>? jumping {}
    public static final Conditional JUMPING;
    // @Format (if|if_not) moving {}
    // @Format (while|while_not) <num>? moving {}
    public static final Conditional MOVING;
    // @Format (if|if_not) blocking {}
    // @Format (while|while_not) <num>? blocking {}
    public static final Conditional BLOCKING;
    // @Format (if|if_not) on_ground {}
    // @Format (while|while_not) <num>? on_ground {}
    public static final Conditional ON_GROUND;
    // @Format (if|if_not) on_fire {}
    // @Format (while|while_not) <num>? on_fire {}
    public static final Conditional ON_FIRE;
    // @Format (if|if_not) frozen {}
    // @Format (while|while_not) <num>? frozen {}
    public static final Conditional FROZEN;
    // @Format (if|if_not) dead {}
    // @Format (while|while_not) <num>? dead {}
    public static final Conditional DEAD;
    // @Format (if|if_not) alive {}
    // @Format (while|while_not) <num>? alive {}
    public static final Conditional ALIVE;
    // @Format (if|if_not) falling {}
    // @Format (while|while_not) <num>? falling {}
    public static final Conditional FALLING;
    // @Format (if|if_not) cursor_item <identifier> {}
    // @Format (while|while_not) <num>? cursor_item <identifier> {}
    public static final Conditional CURSOR_ITEM;
    // @Format (if|if_not) hovering_over <identifier> {}
    // @Format (while|while_not) <num>? hovering_over <identifier> {}
    public static final Conditional HOVERING_OVER;

    // @Format (if|if_not) reference_entity (client|any_entity) {}
    // @Format (if|if_not) reference_entity <identifier> {}
    // @Format (while|while_not) <num>? reference_entity (client|any_entity) {}
    // @Format (while|while_not) <num>? reference_entity <identifier> {}
    public static final Conditional REFERENCE_ENTITY;

    // @Format (if|if_not) item_count <identifier> <comparator> <int> {}
    // @Format (if|if_not) item_count (mainhand|offhand) <comparator> <int> {}
    // @Format (while|while_not) <num>? item_count <identifier> <comparator> <int> {}
    // @Format (while|while_not) <num>? item_count (mainhand|offhand) <comparator> <int> {}
    public static final Conditional ITEM_COUNT;

    // @Format (if|if_not) item_durability <identifier> <comparator> <num> {}
    // @Format (if|if_not) item_durability (mainhand|offhand) <comparator> <num> {}
    // @Format (while|while_not) <num>? item_durability <identifier> <comparator> <num> {}
    // @Format (while|while_not) <num>? item_durability (mainhand|offhand) <comparator> <num> {}
    public static final Conditional ITEM_DURABILITY;

    // @Format (if|if_not) item_cooldown <identifier> <comparator> <num> {}
    // @Format (if|if_not) item_cooldown (mainhand|offhand) <comparator> <num> {}
    // @Format (while|while_not) <num>? item_cooldown <identifier> <comparator> <num> {}
    // @Format (while|while_not) <num>? item_cooldown (mainhand|offhand) <comparator> <num> {}
    public static final Conditional ITEM_COOLDOWN;
    // @Format (if|if_not) gamemode (creative|survival|adventure|spectator) {}
    // @Format (while|while_not) <num>? gamemode (creative|survival|adventure|spectator) {}
    public static final Conditional GAMEMODE;
    // @Format (if|if_not) ping <comparator> <int> {}
    // @Format (while|while_not) <num>? ping <comparator> <int> {}
    public static final Conditional PING;
    // @Format (if|if_not) fps <comparator> <int> {}
    // @Format (while|while_not) <num>? fps <comparator> <int> {}
    public static final Conditional FPS;
    // @Format (if|if_not) line_of_sight {}
    // @Format (while|while_not) <num>? line_of_sight {}
    public static final Conditional LINE_OF_SIGHT;
    // @Format (if|if_not) flying {}
    // @Format (while|while_not) <num>? flying {}
    public static final Conditional FLYING;
    // @Format (if|if_not) sneaking {}
    // @Format (while|while_not) <num>? sneaking {}
    public static final Conditional SNEAKING;
    // @Format (if|if_not) sprinting {}
    // @Format (while|while_not) <num>? sprinting {}
    public static final Conditional SPRINTING;
    // @Format (if|if_not) swimming {}
    // @Format (while|while_not) <num>? swimming {}
    public static final Conditional SWIMMING;
    // @Format (if|if_not) gliding {}
    // @Format (while|while_not) <num>? gliding {}
    public static final Conditional GLIDING;
    // @Format (if|if_not) invisible {}
    // @Format (while|while_not) <num>? invisible {}
    public static final Conditional INVISIBLE;
    // @Format (if|if_not) inventory_slot <int> <identifier> {}
    // @Format (while|while_not) inventory_slot <int> <identifier> {}
    public static final Conditional INVENTORY_SLOT;


    static {
        TRUE = register("true", ctx -> ctx.end(true));
        FALSE = register("false", ctx -> ctx.end(false));
        HOLDING = register("holding", ctx -> ctx.end(true, EntityUtils.isHolding(ctx.entity, ScriptParser.parseItemPredicate(ctx.get(0).toString()))));
        OFF_HOLDING = register("off_holding", ctx -> ctx.end(true, EntityUtils.isOffHolding(ctx.entity, ScriptParser.parseItemPredicate(ctx.get(0).toString()))));
        TARGET_BLOCK = register("target_block", ctx -> {
            Predicate<BlockState> test = ScriptParser.parseBlockPredicate(ctx.get(0).toString());
            return ctx.end(true, EntityUtils.getTarget(ctx.entity) instanceof BlockHitResult hit && test.test(PlayerUtils.getWorld().getBlockState(hit.getBlockPos())));
        });
        TARGET_ENTITY = register("target_entity", ctx -> {
            Predicate<Entity> test = ScriptParser.parseEntityPredicate(ctx.get(0).toString());
            return ctx.end(true, EntityUtils.getTarget(ctx.entity) instanceof EntityHitResult hit && test.test(hit.getEntity()));
        });
        TARGET_FLUID = register("target_fluid", ctx -> {
            Voidable<FluidState> state = EntityUtils.getTargetFluid(ctx.entity, true);
            Predicate<BlockState> test = ScriptParser.parseBlockPredicate(ctx.get(0).toString());
            return ctx.end(true, state.isPresent() && test.test(state.get().getBlockState()));
        });
        TARGETING_BLOCK = register("targeting_block", ctx -> ctx.end(true, EntityUtils.getTarget(ctx.entity) instanceof BlockHitResult hit && !PlayerUtils.getWorld().getBlockState(hit.getBlockPos()).isAir()));
        TARGETING_ENTITY = register("targeting_entity", ctx -> ctx.end(true, EntityUtils.getTarget(ctx.entity) instanceof EntityHitResult hit && hit.getEntity().isAlive()));
        TARGETING_FLUID = register("targeting_fluid", ctx -> ctx.end(true, EntityUtils.getTargetFluid(ctx.entity, false).isPresent()));
        INVENTORY_HAS = register("inventory_has", ctx -> ctx.assertClientPlayer().end(InvUtils.has(ScriptParser.parseItemPredicate(ctx.get(0).toString()))));
        INVENTORY_COUNT = register("inventory_count", ctx -> {
            ctx.assertClientPlayer();
            Predicate<ItemStack> item = ScriptParser.parseItemPredicate(ctx.get(0).toString());
            return ctx.end(ctx.compareNumArg(1, InvUtils.count(item)));
        });
        EQUIPMENT_HAS = register("equipment_has", ctx -> ctx.end(true, EntityUtils.hasEquipment(ctx.entity, ScriptParser.parseItemPredicate(ctx.get(0).toString()))));
        HOTBAR_HAS = register("hotbar_has", ctx -> ctx.assertClientPlayer().end(HotbarUtils.has(ScriptParser.parseItemPredicate(ctx.get(0).toString()))));
        HOTBAR_COUNT = register("hotbar_count", ctx -> {
            ctx.assertClientPlayer();
            Predicate<ItemStack> item = ScriptParser.parseItemPredicate(ctx.get(0).toString());
            return ctx.end(ctx.compareNumArg(1, HotbarUtils.count(item)));
        });
        INPUT_ACTIVE = register("input_active", new ConditionalInputActive());
        BLOCK_IN_RANGE = register("block_in_range", new ConditionalBlockInRange());
        ENTITY_IN_RANGE = register("entity_in_range", new ConditionalEntityInRange());
        BLOCK_IN_FOV = register("block_in_fov", new ConditionalBlockInFov());
        ENTITY_IN_FOV = register("entity_in_fov", new ConditionalEntityInFov());
        ATTACK_PROGRESS = register("attack_progress", ctx -> ctx.assertClientPlayer().end(ctx.compareNumArg(0, PlayerUtils.player().getAttackCooldownProgress(1.0F))));
        HEALTH = register("health", ctx -> ctx.end(true, ctx.entity instanceof LivingEntity liv && ctx.compareNumArg(0, (int) liv.getHealth())));
        HUNGER = register("hunger", ctx -> ctx.end(true, ctx.entity instanceof PlayerEntity liv && ctx.compareNumArg(0, liv.getHungerManager().getFoodLevel())));
        HURT_TIME = register("hurt_time", ctx -> ctx.end(true, ctx.entity instanceof LivingEntity liv && ctx.compareNumArg(0, liv.hurtTime)));
        ARMOR = register("armor", ctx -> ctx.end(true, ctx.entity instanceof LivingEntity liv && ctx.compareNumArg(0, liv.getArmor())));
        POS_X = register("pos_x", ctx -> ctx.end(true, ctx.compareNumArg(0, (int) ctx.entity.getX())));
        POS_Y = register("pos_y", ctx -> ctx.end(true, ctx.compareNumArg(0, (int) ctx.entity.getY())));
        POS_Z = register("pos_z", ctx -> ctx.end(true, ctx.compareNumArg(0, (int) ctx.entity.getZ())));
        VEL_X = register("vel_x", ctx -> ctx.end(true, ctx.compareNumArg(0, ctx.entity.getVelocity().getX())));
        VEL_Y = register("vel_y", ctx -> ctx.end(true, ctx.compareNumArg(0, ctx.entity.getVelocity().getY())));
        VEL_Z = register("vel_z", ctx -> ctx.end(true, ctx.compareNumArg(0, ctx.entity.getVelocity().getZ())));
        MODULE_ENABLED = register("module_enabled", ctx -> {
            Module m = system.getModuleById(ctx.get(0).toString());
            return ctx.end(m != null && m.isEnabled());
        });
        MODULE_DISABLED = register("module_disabled", ctx -> {
            Module m = system.getModuleById(ctx.get(0).toString());
            return ctx.end(m != null && !m.isEnabled());
        });
        BLOCK = register("block", ctx -> {
            VectorParser loc = new VectorParser(ctx.get(0), ctx.get(1), ctx.get(2), ctx.entity);
            Predicate<BlockState> pre = ScriptParser.parseBlockPredicate(ctx.get(3).toString());
            return ctx.end(true, pre.test(loc.getBlock(PlayerUtils.getWorld())));
        });
        ENTITY = register("entity", ctx -> {
            VectorParser loc = new VectorParser(ctx.get(0), ctx.get(1), ctx.get(2), ctx.entity);
            Predicate<Entity> pre = ScriptParser.parseEntityPredicate(ctx.get(3).toString());
            return ctx.end(true, EntityUtils.checkEntityAt(loc.getBlockPos(), pre));
        });
        DIMENSION = register("dimension", new ConditionalDimension());
        EFFECT_AMPLIFIER = register("effect_amplifier", ctx -> {
            StatusEffectInstance effect = EntityUtils.getEffect(ctx.entity, ScriptParser.parseStatusEffect(ctx.get(0).toString()));
            return ctx.end(true, ctx.compareNumArg(1, effect.getAmplifier()));
        });
        EFFECT_DURATION = register("effect_duration", ctx -> {
            StatusEffectInstance effect = EntityUtils.getEffect(ctx.entity, ScriptParser.parseStatusEffect(ctx.get(0).toString()));
            return ctx.end(true, ctx.compareNumArg(1, effect.getDuration()));
        });
        IN_GAME = register("in_game", ctx -> ctx.end(PlayerUtils.valid()));
        IN_SINGLEPLAYER = register("in_singleplayer", ctx -> ctx.end(mc.isInSingleplayer()));
        PLAYING = register("playing", ctx -> ctx.end(PlayerUtils.valid() && mc.currentScreen == null));
        IN_SCREEN = register("in_screen", ctx -> ctx.end(PlayerUtils.valid() && mc.currentScreen != null));
        CHANCE_OF = register("chance_of", ctx -> ctx.end(Math.random() * 100 < ctx.get(0).toDouble()));
        COLLIDING = register("colliding", ctx -> ctx.end(true, EntityUtils.isColliding(ctx.entity)));
        COLLIDING_HORIZONTALLY = register("colliding_horizontally", ctx -> ctx.end(true, EntityUtils.isCollidingHorizontally(ctx.entity)));
        COLLIDING_VERTICALLY = register("colliding_vertically", ctx -> ctx.end(true, EntityUtils.isCollidingVertically(ctx.entity)));
        JUMPING = register("jumping", ctx -> ctx.end(true, PlayerUtils.valid() && mc.player.input.playerInput.jump() && !mc.player.isSubmergedInWater()));
        MOVING = register("moving", ctx -> ctx.end(true, EntityUtils.isMoving(ctx.entity)));
        BLOCKING = register("blocking", ctx -> ctx.end(true, EntityUtils.isBlocking(ctx.entity)));
        ON_GROUND = register("on_ground", ctx -> ctx.end(true, ctx.entity.isOnGround()));
        ON_FIRE = register("on_fire", ctx -> ctx.end(true, ctx.entity.isOnFire()));
        FROZEN = register("frozen", ctx -> ctx.end(true, ctx.entity.isFrozen()));
        DEAD = register("dead", ctx -> ctx.end(true, ctx.entity instanceof LivingEntity liv && liv.isDead()));
        ALIVE = register("alive", ctx -> ctx.end(true, ctx.entity instanceof LivingEntity liv && liv.isAlive()));
        FALLING = register("falling", ctx -> ctx.end(true, ctx.entity instanceof LivingEntity liv && !liv.isOnGround() && liv.fallDistance > 0.0));
        CURSOR_ITEM = register("cursor_item", new ConditionalCursorItem());
        HOVERING_OVER = register("hovering_over", new ConditionalHoveringOver());
        REFERENCE_ENTITY = register("reference_entity", ctx -> {
            if (ctx.match(0, "client"))
                return ctx.end(ctx.entity == PlayerUtils.player());
            Predicate<Entity> filter = ctx.match(0, "any_entity") ? entity -> true : ScriptParser.parseEntityPredicate(ctx.get(0).toString());
            return ctx.end(filter.test(ctx.entity));
        });
        ITEM_COUNT = register("item_count", ctx -> {
            ItemStack item = ScriptParser.parseItemStack(ctx.get(0).toString());
            return ctx.end(item != null && ctx.compareNumArg(1, item.getCount()));
        });
        ITEM_DURABILITY = register("item_durability", ctx -> {
            ItemStack item = ScriptParser.parseItemStack(ctx.get(0).toString());
            return ctx.end(item != null && ctx.compareNumArg(1, 1 - item.getDamage() / (double) item.getMaxDamage()));
        });
        ITEM_COOLDOWN = register("item_cooldown", ctx -> {
            if (!(ctx.entity instanceof PlayerEntity player))
                return ctx.end(true, false);
            ItemStack item = ScriptParser.parseItemStack(player, ctx.get(0).toString());
            return ctx.end(true, item != null && ctx.compareNumArg(1, player.getItemCooldownManager().getCooldownProgress(item, 1.0F)));
        });
        GAMEMODE = register("gamemode", ctx -> {
            GameMode gm = ctx.get(0).toEnum(GameMode.class);
            return ctx.end(ctx.entity instanceof PlayerEntity p && p.getGameMode() == gm);
        });
        PING = register("ping", ctx -> ctx.assertClientPlayer().end(ctx.compareNumArg(0, PlayerUtils.getPing())));
        FPS = register("fps", ctx -> ctx.assertClientPlayer().end(ctx.compareNumArg(0, PlayerUtils.getFps())));
        LINE_OF_SIGHT = register("line_of_sight", ctx -> ctx.end(true, PlayerUtils.valid() && ctx.entity != PlayerUtils.player() && PlayerUtils.player().canSee(ctx.entity)));
        FLYING = register("flying", ctx -> ctx.assertClientPlayer().end(PlayerUtils.valid() && PlayerUtils.player().getAbilities().flying));
        SNEAKING = register("sneaking", ctx -> ctx.end(true, ctx.entity.isSneaking()));
        SPRINTING = register("sprinting", ctx -> ctx.end(true, ctx.entity.isSprinting()));
        SWIMMING = register("swimming", ctx -> ctx.end(true, ctx.entity.isSwimming()));
        GLIDING = register("gliding", ctx -> ctx.end(true, ctx.entity instanceof PlayerEntity player && player.isGliding()));
        INVISIBLE = register("invisible", ctx -> ctx.end(true, ctx.entity.isInvisible()));
        INVENTORY_SLOT = register("inventory_slot", new ConditionalInventorySlot());
    }
}
