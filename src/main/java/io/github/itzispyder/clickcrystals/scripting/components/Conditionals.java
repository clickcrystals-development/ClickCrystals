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
    public static final Conditional TRUE; // @Format (if|if_not|!if|while|while_not|!while) true
    public static final Conditional FALSE; // @Format (if|if_not|!if|while|while_not|!while) false
    public static final Conditional HOLDING; // @Format (if|if_not|!if|while|while_not|!while) holding <identifier>
    public static final Conditional OFF_HOLDING; // @Format (if|if_not|!if|while|while_not|!while) off_holding <identifier>
    public static final Conditional TARGET_BLOCK; // @Format (if|if_not|!if|while|while_not|!while) target_block <identifier>
    public static final Conditional TARGET_ENTITY; // @Format (if|if_not|!if|while|while_not|!while) target_entity <identifier>
    public static final Conditional TARGETING_BLOCK; // @Format (if|if_not|!if|while|while_not|!while) targeting_block
    public static final Conditional TARGETING_ENTITY; // @Format (if|if_not|!if|while|while_not|!while) targeting_entity
    public static final Conditional INVENTORY_HAS; // @Format (if|if_not|!if|while|while_not|!while) inventory_has <identifier>
    public static final Conditional EQUIPMENT_HAS; // @Format (if|if_not|!if|while|while_not|!while) equipment_has <identifier>
    public static final Conditional HOTBAR_HAS; // @Format (if|if_not|!if|while|while_not|!while) hotbar_has <identifier>

    // @Format (if|if_not|!if|while|while_not|!while) input_active (attack|use|forward|backward|strafe_left|strafe_right|jump|sprint|sneak|lock_cursor|unlock_cursor|left|right|middle|inventory)
    // @Format (if|if_not|!if|while|while_not|!while) input_active key ...
    public static final Conditional INPUT_ACTIVE;
    public static final Conditional BLOCK_IN_RANGE; // @Format (if|if_not|!if|while|while_not|!while) block_in_range <identifier> <num>
    public static final Conditional ENTITY_IN_RANGE; // @Format (if|if_not|!if|while|while_not|!while) entity_in_range <identifier> <num>
    public static final Conditional ATTACK_PROGRESS; // @Format (if|if_not|!if|while|while_not|!while) attack_progress <comparator> <num>
    public static final Conditional HEALTH; // @Format (if|if_not|!if|while|while_not|!while) health <comparator> <num>
    public static final Conditional HUNGER; // @Format (if|if_not|!if|while|while_not|!while) hunger <comparator> <num>
    public static final Conditional HURT_TIME; // @Format (if|if_not|!if|while|while_not|!while) hurt_time <comparator> <int>
    public static final Conditional ARMOR; // @Format (if|if_not|!if|while|while_not|!while) armor <comparator> <int>
    public static final Conditional POS_X; // @Format (if|if_not|!if|while|while_not|!while) pos_x <comparator> <num>
    public static final Conditional POS_Y; // @Format (if|if_not|!if|while|while_not|!while) pos_y <comparator> <num>
    public static final Conditional POS_Z; // @Format (if|if_not|!if|while|while_not|!while) pos_z <comparator> <num>
    public static final Conditional VEL_X; // @Format (if|if_not|!if|while|while_not|!while) vel_x <comparator> <num>
    public static final Conditional VEL_Y; // @Format (if|if_not|!if|while|while_not|!while) vel_y <comparator> <num>
    public static final Conditional VEL_Z; // @Format (if|if_not|!if|while|while_not|!while) vel_z <comparator> <num>
    public static final Conditional MODULE_ENABLED; // @Format (if|if_not|!if|while|while_not|!while) module_enabled ...
    public static final Conditional MODULE_DISABLED; // @Format (if|if_not|!if|while|while_not|!while) module_disabled ...
    public static final Conditional BLOCK; // @Format (if|if_not|!if|while|while_not|!while) block <x> <y> <z>
    public static final Conditional ENTITY; // @Format (if|if_not|!if|while|while_not|!while) entity <x> <y> <z>
    public static final Conditional DIMENSION; // @Format (if|if_not|!if|while|while_not|!while) dimension (overworld|the_nether|the_end)
    public static final Conditional EFFECT_AMPLIFIER; // @Format (if|if_not|!if|while|while_not|!while) <identifier> <comparator> <int>
    public static final Conditional EFFECT_DURATION; // @Format (if|if_not|!if|while|while_not|!while) <identifier> <comparator> <int>
    public static final Conditional IN_GAME; // @Format (if|if_not|!if|while|while_not|!while) in_game
    public static final Conditional IN_SINGLEPLAYER; // @Format (if|if_not|!if|while|while_not|!while) in_singleplayer
    public static final Conditional PLAYING; // @Format (if|if_not|!if|while|while_not|!while) playing
    public static final Conditional IN_SCREEN; // @Format (if|if_not|!if|while|while_not|!while) in_screen
    public static final Conditional CHANCE_OF; // @Format (if|if_not|!if|while|while_not|!while) chance_of <num>
    public static final Conditional COLLIDING; // @Format (if|if_not|!if|while|while_not|!while) colliding
    public static final Conditional COLLIDING_HORIZONTALLY; // @Format (if|if_not|!if|while|while_not|!while) colliding_horizontally
    public static final Conditional COLLIDING_VERTICALLY; // @Format (if|if_not|!if|while|while_not|!while) colliding_vertically
    public static final Conditional JUMPING; // @Format (if|if_not|!if|while|while_not|!while) jumping
    public static final Conditional MOVING; // @Format (if|if_not|!if|while|while_not|!while) moving
    public static final Conditional BLOCKING; // @Format (if|if_not|!if|while|while_not|!while) blocking
    public static final Conditional ON_GROUND; // @Format (if|if_not|!if|while|while_not|!while) on_ground
    public static final Conditional ON_FIRE; // @Format (if|if_not|!if|while|while_not|!while) on_fire
    public static final Conditional FROZEN; // @Format (if|if_not|!if|while|while_not|!while) frozen
    public static final Conditional DEAD; // @Format (if|if_not|!if|while|while_not|!while) dead
    public static final Conditional ALIVE; // @Format (if|if_not|!if|while|while_not|!while) alive
    public static final Conditional FALLING; // @Format (if|if_not|!if|while|while_not|!while) falling
    public static final Conditional CURSOR_ITEM; // @Format (if|if_not|!if|while|while_not|!while) cursor_item <identifier>
    public static final Conditional HOVERING_OVER; // @Format (if|if_not|!if|while|while_not|!while) hovering_over <identifier>

    // @Format (if|if_not|!if|while|while_not|!while) reference_entity (client|any_entity)
    // @Format (if|if_not|!if|while|while_not|!while) reference_entity <identifier>
    public static final Conditional REFERENCE_ENTITY;
    public static final Conditional ITEM_COUNT; // @Format (if|if_not|!if|while|while_not|!while) <identifier> <comparator> <int>
    public static final Conditional ITEM_DURABILITY; // @Format (if|if_not|!if|while|while_not|!while) <identifier> <comparator> <num>
    public static final Conditional GAMEMODE; // @Format (if|if_not|!if|while|while_not|!while) gamemode (creative|survival|adventure|spectator)
    public static final Conditional LINE_OF_SIGHT; // @Format (if|if_not|!if|while|while_not|!while) line_of_sight


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
        REFERENCE_ENTITY = register("ctx.entityerence_entity", ctx -> {
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
        LINE_OF_SIGHT = register("line_of_sight", ctx -> ctx.end(PlayerUtils.valid() && ctx.entity != PlayerUtils.player() && PlayerUtils.player().canSee(ctx.entity)));
    }
}
