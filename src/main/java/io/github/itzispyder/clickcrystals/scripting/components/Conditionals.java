package io.github.itzispyder.clickcrystals.scripting.components;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.interfaces.HandledScreenAccessor;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.syntax.InputType;
import io.github.itzispyder.clickcrystals.util.minecraft.*;
import io.github.itzispyder.clickcrystals.util.misc.Dimensions;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.GameMode;

import java.awt.*;
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
        String arg = args.get(beginIndex).toString();
        Conditional condition = registry.get(arg.toLowerCase());

        if (condition == null)
            throw new IllegalArgumentException("cannot recognize conditional [%s]".formatted(arg));

        ConditionEvaluationContext context = new ConditionEvaluationContext(ref, args, beginIndex);
        return condition.evaluate(context);
    }


    // conditionals


    // @Format (if|if_not|!if) true {}
    // @Format (while|while_not|!while) <num>? true {}
    public static final Conditional TRUE;
    // @Format (if|if_not|!if) false {}
    // @Format (while|while_not|!while) <num>? false {}
    public static final Conditional FALSE;
    // @Format (if|if_not|!if) holding <identifier> {}
    // @Format (while|while_not|!while) <num>? holding <identifier> {}
    public static final Conditional HOLDING;
    // @Format (if|if_not|!if) off_holding <identifier> {}
    // @Format (while|while_not|!while) <num>? off_holding <identifier> {}
    public static final Conditional OFF_HOLDING;
    // @Format (if|if_not|!if) target_block <identifier> {}
    // @Format (while|while_not|!while) <num>? target_block <identifier> {}
    public static final Conditional TARGET_BLOCK;
    // @Format (if|if_not|!if) target_entity <identifier> {}
    // @Format (while|while_not|!while) <num>? target_entity <identifier> {}
    public static final Conditional TARGET_ENTITY;
    // @Format (if|if_not|!if) targeting_block {}
    // @Format (while|while_not|!while) <num>? targeting_block {}
    public static final Conditional TARGETING_BLOCK;
    // @Format (if|if_not|!if) targeting_entity {}
    // @Format (while|while_not|!while) <num>? targeting_entity {}
    public static final Conditional TARGETING_ENTITY;
    // @Format (if|if_not|!if) inventory_has <identifier> {}
    // @Format (while|while_not|!while) <num>? inventory_has <identifier> {}
    public static final Conditional INVENTORY_HAS;
    // @Format (if|if_not|!if) equipment_has <identifier> {}
    // @Format (while|while_not|!while) <num>? equipment_has <identifier> {}
    public static final Conditional EQUIPMENT_HAS;
    // @Format (if|if_not|!if) hotbar_has <identifier> {}
    // @Format (while|while_not|!while) <num>? hotbar_has <identifier> {}
    public static final Conditional HOTBAR_HAS;

    // @Format (if|if_not|!if) input_active (attack|mouse_wheel_up|mouse_wheel_down|use|forward|backward|strafe_left|strafe_right|jump|sprint|sneak|lock_cursor|unlock_cursor|left|right|middle|inventory) {}
    // @Format (if|if_not|!if) input_active key ... {}
    // @Format (while|while_not|!while) <num>? input_active (attack|mouse_wheel_up|mouse_wheel_down|use|forward|backward|strafe_left|strafe_right|jump|sprint|sneak|lock_cursor|unlock_cursor|left|right|middle|inventory) {}
    // @Format (while|while_not|!while) <num>? input_active key ... {}
    public static final Conditional INPUT_ACTIVE;

    // @Format (if|if_not|!if) block_in_range <identifier> <num> {}
    // @Format (while|while_not|!while) <num>? block_in_range <identifier> <num> {}
    public static final Conditional BLOCK_IN_RANGE;
    // @Format (if|if_not|!if) entity_in_range <identifier> <num> {}
    // @Format (while|while_not|!while) <num>? entity_in_range <identifier> <num> {}
    public static final Conditional ENTITY_IN_RANGE;
    // @Format (if|if_not|!if) attack_progress <comparator> <num> {}
    // @Format (while|while_not|!while) <num>? attack_progress <comparator> <num> {}
    public static final Conditional ATTACK_PROGRESS;
    // @Format (if|if_not|!if) health <comparator> <num> {}
    // @Format (while|while_not|!while) <num>? health <comparator> <num> {}
    public static final Conditional HEALTH;
    // @Format (if|if_not|!if) hunger <comparator> <num> {}
    // @Format (while|while_not|!while) <num>? hunger <comparator> <num> {}
    public static final Conditional HUNGER;
    // @Format (if|if_not|!if) hurt_time <comparator> <int> {}
    // @Format (while|while_not|!while) <num>? hurt_time <comparator> <int> {}
    public static final Conditional HURT_TIME;
    // @Format (if|if_not|!if) armor <comparator> <int> {}
    // @Format (while|while_not|!while) <num>? armor <comparator> <int> {}
    public static final Conditional ARMOR;
    // @Format (if|if_not|!if) pos_x <comparator> <num> {}
    // @Format (while|while_not|!while) <num>? pos_x <comparator> <num> {}
    public static final Conditional POS_X;
    // @Format (if|if_not|!if) pos_y <comparator> <num> {}
    // @Format (while|while_not|!while) <num>? pos_y <comparator> <num> {}
    public static final Conditional POS_Y;
    // @Format (if|if_not|!if) pos_z <comparator> <num> {}
    // @Format (while|while_not|!while) <num>? pos_z <comparator> <num> {}
    public static final Conditional POS_Z;
    // @Format (if|if_not|!if) vel_x <comparator> <num> {}
    // @Format (while|while_not|!while) <num>? vel_x <comparator> <num> {}
    public static final Conditional VEL_X;
    // @Format (if|if_not|!if) vel_y <comparator> <num> {}
    // @Format (while|while_not|!while) <num>? vel_y <comparator> <num> {}
    public static final Conditional VEL_Y;
    // @Format (if|if_not|!if) vel_z <comparator> <num> {}
    // @Format (while|while_not|!while) <num>? vel_z <comparator> <num> {}
    public static final Conditional VEL_Z;
    // @Format (if|if_not|!if) module_enabled ... {}
    // @Format (while|while_not|!while) <num>? module_enabled ... {}
    public static final Conditional MODULE_ENABLED;
    // @Format (if|if_not|!if) module_disabled ... {}
    // @Format (while|while_not|!while) <num>? module_disabled ... {}
    public static final Conditional MODULE_DISABLED;
    // @Format (if|if_not|!if) block <x> <y> <z> {}
    // @Format (while|while_not|!while) <num>? block <x> <y> <z> {}
    public static final Conditional BLOCK;
    // @Format (if|if_not|!if) entity <x> <y> <z> {}
    // @Format (while|while_not|!while) <num>? entity <x> <y> <z> {}
    public static final Conditional ENTITY;
    // @Format (if|if_not|!if) dimension (overworld|the_nether|the_end) {}
    // @Format (while|while_not|!while) <num>? dimension (overworld|the_nether|the_end) {}
    public static final Conditional DIMENSION;
    // @Format (if|if_not|!if) effect_amplifier <identifier> <comparator> <int> {}
    // @Format (while|while_not|!while) <num>? <identifier> <comparator> <int> {}
    public static final Conditional EFFECT_AMPLIFIER;
    // @Format (if|if_not|!if) effect_duration <identifier> <comparator> <int> {}
    // @Format (while|while_not|!while) <num>? <identifier> <comparator> <int> {}
    public static final Conditional EFFECT_DURATION;
    // @Format (if|if_not|!if) in_game {}
    // @Format (while|while_not|!while) <num>? in_game {}
    public static final Conditional IN_GAME;
    // @Format (if|if_not|!if) in_singleplayer {}
    // @Format (while|while_not|!while) <num>? in_singleplayer {}
    public static final Conditional IN_SINGLEPLAYER;
    // @Format (if|if_not|!if) playing {}
    // @Format (while|while_not|!while) <num>? playing {}
    public static final Conditional PLAYING;
    // @Format (if|if_not|!if) in_screen {}
    // @Format (while|while_not|!while) <num>? in_screen {}
    public static final Conditional IN_SCREEN;
    // @Format (if|if_not|!if) chance_of <num> {}
    // @Format (while|while_not|!while) <num>? chance_of <num> {}
    public static final Conditional CHANCE_OF;
    // @Format (if|if_not|!if) colliding {}
    // @Format (while|while_not|!while) <num>? colliding {}
    public static final Conditional COLLIDING;
    // @Format (if|if_not|!if) colliding_horizontally {}
    // @Format (while|while_not|!while) <num>? colliding_horizontally {}
    public static final Conditional COLLIDING_HORIZONTALLY;
    // @Format (if|if_not|!if) colliding_vertically {}
    // @Format (while|while_not|!while) <num>? colliding_vertically {}
    public static final Conditional COLLIDING_VERTICALLY;
    // @Format (if|if_not|!if) jumping {}
    // @Format (while|while_not|!while) <num>? jumping {}
    public static final Conditional JUMPING;
    // @Format (if|if_not|!if) moving {}
    // @Format (while|while_not|!while) <num>? moving {}
    public static final Conditional MOVING;
    // @Format (if|if_not|!if) blocking {}
    // @Format (while|while_not|!while) <num>? blocking {}
    public static final Conditional BLOCKING;
    // @Format (if|if_not|!if) on_ground {}
    // @Format (while|while_not|!while) <num>? on_ground {}
    public static final Conditional ON_GROUND;
    // @Format (if|if_not|!if) on_fire {}
    // @Format (while|while_not|!while) <num>? on_fire {}
    public static final Conditional ON_FIRE;
    // @Format (if|if_not|!if) frozen {}
    // @Format (while|while_not|!while) <num>? frozen {}
    public static final Conditional FROZEN;
    // @Format (if|if_not|!if) dead {}
    // @Format (while|while_not|!while) <num>? dead {}
    public static final Conditional DEAD;
    // @Format (if|if_not|!if) alive {}
    // @Format (while|while_not|!while) <num>? alive {}
    public static final Conditional ALIVE;
    // @Format (if|if_not|!if) falling {}
    // @Format (while|while_not|!while) <num>? falling {}
    public static final Conditional FALLING;
    // @Format (if|if_not|!if) cursor_item <identifier> {}
    // @Format (while|while_not|!while) <num>? cursor_item <identifier> {}
    public static final Conditional CURSOR_ITEM;
    // @Format (if|if_not|!if) hovering_over <identifier> {}
    // @Format (while|while_not|!while) <num>? hovering_over <identifier> {}
    public static final Conditional HOVERING_OVER;

    // @Format (if|if_not|!if) reference_entity (client|any_entity) {}
    // @Format (if|if_not|!if) reference_entity <identifier> {}
    // @Format (while|while_not|!while) <num>? reference_entity (client|any_entity) {}
    // @Format (while|while_not|!while) <num>? reference_entity <identifier> {}
    public static final Conditional REFERENCE_ENTITY;

    // @Format (if|if_not|!if) item_count <identifier> <comparator> <int> {}
    // @Format (if|if_not|!if) item_count (holding|off_holding) <comparator> <int> {}
    // @Format (while|while_not|!while) <num>? item_count <identifier> <comparator> <int> {}
    // @Format (while|while_not|!while) <num>? item_count (holding|off_holding) <comparator> <int> {}
    public static final Conditional ITEM_COUNT;

    // @Format (if|if_not|!if) item_durability <identifier> <comparator> <num> {}
    // @Format (if|if_not|!if) item_durability (holding|off_holding) <comparator> <num> {}
    // @Format (while|while_not|!while) <num>? item_durability <identifier> <comparator> <num> {}
    // @Format (while|while_not|!while) <num>? item_durability (holding|off_holding) <comparator> <num> {}
    public static final Conditional ITEM_DURABILITY;
    // @Format (if|if_not|!if) gamemode (creative|survival|adventure|spectator) {}
    // @Format (while|while_not|!while) <num>? gamemode (creative|survival|adventure|spectator) {}
    public static final Conditional GAMEMODE;
    // @Format (if|if_not|!if) line_of_sight {}
    // @Format (while|while_not|!while) <num>? line_of_sight {}
    public static final Conditional LINE_OF_SIGHT;
    // @Format (if|if_not|!if) flying {}
    // @Format (while|while_not|!while) <num>? flying {}
    public static final Conditional FLYING;
    // @Format (if|if_not|!if) sneaking {}
    // @Format (while|while_not|!while) <num>? sneaking {}
    public static final Conditional SNEAKING;
    // @Format (if|if_not|!if) sprinting {}
    // @Format (while|while_not|!while) <num>? sprinting {}
    public static final Conditional SPRINTING;
    // @Format (if|if_not|!if) swimming {}
    // @Format (while|while_not|!while) <num>? swimming {}
    public static final Conditional SWIMMING;
    // @Format (if|if_not|!if) gliding {}
    // @Format (while|while_not|!while) <num>? gliding {}
    public static final Conditional GLIDING;


    static {
        TRUE = register("true", ctx -> ctx.end(true));
        FALSE = register("false", ctx -> ctx.end(false));
        HOLDING = register("holding", ctx -> ctx.end(true, EntityUtils.isHolding(ctx.entity, ScriptParser.parseItemPredicate(ctx.get(0).toString()))));
        OFF_HOLDING = register("off_holding", ctx -> ctx.end(true, EntityUtils.isOffHolding(ctx.entity, ScriptParser.parseItemPredicate(ctx.get(0).toString()))));
        TARGET_BLOCK = register("target_block", ctx -> ctx.end(true, EntityUtils.getTarget(ctx.entity) instanceof BlockHitResult hit && ScriptParser.parseBlockPredicate(ctx.get(0).toString()).test(PlayerUtils.getWorld().getBlockState(hit.getBlockPos()))));
        TARGET_ENTITY = register("target_entity", ctx -> ctx.end(true, EntityUtils.getTarget(ctx.entity) instanceof EntityHitResult hit && ScriptParser.parseEntityPredicate(ctx.get(0).toString()).test(hit.getEntity())));
        TARGETING_BLOCK = register("targeting_block", ctx -> ctx.end(true, EntityUtils.getTarget(ctx.entity) instanceof BlockHitResult hit && !PlayerUtils.getWorld().getBlockState(hit.getBlockPos()).isAir()));
        TARGETING_ENTITY = register("targeting_entity", ctx -> ctx.end(true, EntityUtils.getTarget(ctx.entity) instanceof EntityHitResult hit && hit.getEntity().isAlive()));
        INVENTORY_HAS = register("inventory_has", ctx -> ctx.assertClientPlayer().end(InvUtils.has(ScriptParser.parseItemPredicate(ctx.get(0).toString()))));
        EQUIPMENT_HAS = register("equipment_has", ctx -> ctx.end(true, EntityUtils.hasEquipment(ctx.entity, ScriptParser.parseItemPredicate(ctx.get(0).toString()))));
        HOTBAR_HAS = register("hotbar_has", ctx -> ctx.assertClientPlayer().end(HotbarUtils.has(ScriptParser.parseItemPredicate(ctx.get(0).toString()))));
        INPUT_ACTIVE = register("input_active", ctx -> {
            ctx.assertClientPlayer();
            InputType a = ctx.get(0).toEnum(InputType.class, null);
            if (a != InputType.KEY)
                return ctx.end(a.isActive());
            else
                return ctx.end(InteractionUtils.isKeyExtendedNamePressed(ctx.get(1).toString()));
        });
        BLOCK_IN_RANGE = register("block_in_range", ctx -> {
            Predicate<BlockState> filter = ctx.match(0, "any_block") ? state -> true : ScriptParser.parseBlockPredicate(ctx.get(0).toString());
            double range = ctx.get(1).toDouble();
            boolean[] bl = { false };
            EntityUtils.runOnNearestBlock(ctx.entity, range, filter, (pos, state) -> {
                bl[0] = pos.toCenterPos().distanceTo(PlayerUtils.getPos()) <= range;
            });
            return ctx.end(true, bl[0]);
        });
        ENTITY_IN_RANGE = register("entity_in_range", ctx -> {
            Predicate<Entity> filter = ctx.match(0, "any_entity") ? entity -> true : ScriptParser.parseEntityPredicate(ctx.get(0).toString());
            double range = ctx.get(1).toDouble();
            boolean[] bl = { false };
            EntityUtils.runOnNearestEntity(ctx.entity, range, filter, entity -> {
                bl[0] = entity.distanceTo(PlayerUtils.player()) <= range;
            });
            return ctx.end(true, bl[0]);
        });
        ATTACK_PROGRESS = register("attack_progress", ctx -> ctx.assertClientPlayer().end(ctx.compareNumArg(0, PlayerUtils.player().getAttackCooldownProgress(1.0F))));
        HEALTH = register("health", ctx -> ctx.end(true, ctx.entity instanceof LivingEntity liv && ctx.compareNumArg(0, (int) liv.getHealth())));
        HUNGER = register("hunger", ctx -> ctx.end(true, ctx.entity instanceof PlayerEntity liv && ctx.compareNumArg(0, liv.getHungerManager().getFoodLevel())));
        HURT_TIME = register("hurt_time", ctx -> ctx.end(true, ctx.entity instanceof LivingEntity liv && ctx.compareNumArg(0, liv.hurtTime)));
        ARMOR = register("armor", ctx -> ctx.end(true, ctx.entity instanceof LivingEntity liv && ctx.compareNumArg(0, liv.getArmor())));
        POS_X = register("pos_x", ctx -> ctx.end(true, ctx.compareNumArg(0, (int) ctx.entity.getX())));
        POS_Y = register("pos_y", ctx -> ctx.end(true, ctx.compareNumArg(0, (int) ctx.entity.getY())));
        POS_Z = register("pos_z", ctx -> ctx.end(true, ctx.compareNumArg(0, (int) ctx.entity.getZ())));
        VEL_X = register("vel_x", ctx -> ctx.end(true, ctx.compareNumArg(0, (int) ctx.entity.getVelocity().getX())));
        VEL_Y = register("vel_y", ctx -> ctx.end(true, ctx.compareNumArg(0, (int) ctx.entity.getVelocity().getY())));
        VEL_Z = register("vel_z", ctx -> ctx.end(true, ctx.compareNumArg(0, (int) ctx.entity.getVelocity().getZ())));
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
        DIMENSION = register("dimension", ctx -> {
            boolean bl = false;
            switch (ctx.get(0).toEnum(Dimensions.class, null)) {
                case OVERWORLD -> bl = Dimensions.isOverworld();
                case THE_NETHER -> bl = Dimensions.isNether();
                case THE_END -> bl = Dimensions.isEnd();
            }
            return ctx.end(bl);
        });
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
        CURSOR_ITEM = register("cursor_item", ctx -> {
            ClientPlayerEntity p = PlayerUtils.player();
            Predicate<ItemStack> item = ScriptParser.parseItemPredicate(ctx.get(0).toString());
            if (p == null || p.currentScreenHandler == null)
                return ctx.end(false);
            ItemStack stack = p.currentScreenHandler.getCursorStack();
            return ctx.end(stack != null && item.test(stack));
        });
        HOVERING_OVER = register("hovering_over", ctx -> {
            ClientPlayerEntity p = PlayerUtils.player();
            Predicate<ItemStack> item = ScriptParser.parseItemPredicate(ctx.get(0).toString());
            if (p == null || p.currentScreenHandler == null || !(mc.currentScreen instanceof HandledScreen<?> handle))
                return ctx.end(false);

            HandledScreenAccessor screen = (HandledScreenAccessor) handle;
            Point cursor = InteractionUtils.getCursor();

            for (Slot slot : p.currentScreenHandler.slots) {
                if (!screen.isHovered(slot, cursor.x, cursor.y))
                    continue;
                ItemStack stack = slot.getStack();
                if (stack == null || !item.test(stack))
                    continue;
                return ctx.end(true);
            }
            return ctx.end(false);
        });
        REFERENCE_ENTITY = register("reference_entity", ctx -> {
            if (ctx.match(0, "client")) {
                return ctx.end(ctx.entity == PlayerUtils.player());
            }
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
        GAMEMODE = register("gamemode", ctx -> {
            GameMode gm = ctx.get(0).toEnum(GameMode.class);
            return ctx.end(ctx.entity instanceof PlayerEntity p && p.getGameMode() == gm);
        });
        LINE_OF_SIGHT = register("line_of_sight", ctx -> ctx.end(true, PlayerUtils.valid() && ctx.entity != PlayerUtils.player() && PlayerUtils.player().canSee(ctx.entity)));
        FLYING = register("flying", ctx -> ctx.assertClientPlayer().end(PlayerUtils.valid() && PlayerUtils.player().getAbilities().flying));
        SNEAKING = register("sneaking", ctx -> ctx.end(true, ctx.entity.isSneaking()));
        SPRINTING = register("sprinting", ctx -> ctx.end(true, ctx.entity.isSprinting()));
        SWIMMING = register("swimming", ctx -> ctx.end(true, ctx.entity.isSwimming()));
        GLIDING = register("gliding", ctx -> ctx.end(true, ctx.entity instanceof PlayerEntity player && player.isGliding()));
    }
}
