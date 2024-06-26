package io.github.itzispyder.clickcrystals.modules.scripts.syntax;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptParser;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.scripts.ThenChainable;
import io.github.itzispyder.clickcrystals.modules.scripts.macros.InputCmd;
import io.github.itzispyder.clickcrystals.util.minecraft.*;
import io.github.itzispyder.clickcrystals.util.misc.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.function.Predicate;

public class IfCmd extends ScriptCommand implements Global, ThenChainable {

    public IfCmd() {
        super("if");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        var condition = parseCondition(AsCmd.getCurrentReferenceEntity(), args, 0);
        if (condition.left) {
            executeWithThen(args, condition.right);
        }
    }

    public static Pair<Boolean, Integer> parseCondition(ScriptArgs args, int beginIndex) {
        return parseCondition(PlayerUtils.player(), args, beginIndex);
    }

    /**
     * Parse clickscript condition
     * @param args script command arguments
     * @param beginIndex begin index
     * @return pair(yourCondition, theNextBeginIndex)
     */
    public static Pair<Boolean, Integer> parseCondition(Entity ref, ScriptArgs args, int beginIndex) {
        ConditionType type = args.get(beginIndex).toEnum(ConditionType.class, null);
        int i = beginIndex;

        switch (type) {
            case TRUE -> {
                return pair(true, i + 1);
            }
            case FALSE -> {
                return pair(false, i + 1);
            }
            case HOLDING -> {
                return pair(EntityUtils.isHolding(ref, ScriptParser.parseItemPredicate(args.get(i + 1).toString())), i + 2);
            }
            case OFF_HOLDING -> {
                return pair(EntityUtils.isOffHolding(ref, ScriptParser.parseItemPredicate(args.get(i + 1).toString())), i + 2);
            }
            case TARGET_BLOCK -> {
                boolean bl = EntityUtils.getTarget(ref) instanceof BlockHitResult hit && ScriptParser.parseBlockPredicate(args.get(i + 1).toString()).test(PlayerUtils.getWorld().getBlockState(hit.getBlockPos()));
                return pair(bl, i + 2);
            }
            case TARGET_ENTITY -> {
                boolean bl = EntityUtils.getTarget(ref) instanceof EntityHitResult hit && ScriptParser.parseEntityPredicate(args.get(i + 1).toString()).test(hit.getEntity());
                return pair(bl, i + 2);
            }
            case TARGETING_BLOCK -> {
                return pair(EntityUtils.getTarget(ref) instanceof BlockHitResult hit && !PlayerUtils.getWorld().getBlockState(hit.getBlockPos()).isAir(), i + 1);
            }
            case TARGETING_ENTITY -> {
                return pair(EntityUtils.getTarget(ref) instanceof EntityHitResult hit && hit.getEntity().isAlive(), i + 1);
            }
            case INVENTORY_HAS -> {
                assertClientPlayer();
                return pair(InvUtils.has(ScriptParser.parseItemPredicate(args.get(i + 1).toString())), i + 2);
            }
            case EQUIPMENT_HAS -> {
                return pair(EntityUtils.hasEquipment(ref, ScriptParser.parseItemPredicate(args.get(i + 1).toString())), i + 2);
            }
            case HOTBAR_HAS -> {
                assertClientPlayer();
                return pair(HotbarUtils.has(ScriptParser.parseItemPredicate(args.get(i + 1).toString())), i + 2);
            }
            case INPUT_ACTIVE -> {
                assertClientPlayer();

                InputCmd.Action a = args.get(i + 1).toEnum(InputCmd.Action.class, null);
                if (a != InputCmd.Action.KEY)
                    return pair(a.isActive(), i + 2);
                else
                    return pair(InteractionUtils.isKeyExtendedNamePressed(args.get(i + 2).toString()), i + 3);
            }
            case BLOCK_IN_RANGE -> {
                Predicate<BlockState> filter = args.match(i + 1, "any_block") ? state -> true : ScriptParser.parseBlockPredicate(args.get(i + 1).toString());
                double range = args.get(i + 2).toDouble();
                boolean[] bl = { false };
                EntityUtils.runOnNearestBlock(ref, range, filter, (pos, state) -> {
                    bl[0] = pos.toCenterPos().distanceTo(PlayerUtils.getPos()) <= range;
                });
                return pair(bl[0], i + 3);
            }
            case ENTITY_IN_RANGE -> {
                Predicate<Entity> filter = args.match(i + 1, "any_entity") ? entity -> true : ScriptParser.parseEntityPredicate(args.get(i + 1).toString());
                double range = args.get(i + 2).toDouble();
                boolean[] bl = { false };
                EntityUtils.runOnNearestEntity(ref, range, filter, entity -> {
                    bl[0] = entity.distanceTo(PlayerUtils.player()) <= range;
                });
                return pair(bl[0], i + 3);
            }
            case ATTACK_PROGRESS -> {
                assertClientPlayer();
                return pair(evalIntegers(PlayerUtils.player().getAttackCooldownProgress(1.0F), args.get(i + 1).toString()), i + 2);
            }
            case HEALTH -> {
                return pair(ref instanceof LivingEntity liv && evalIntegers((int)liv.getHealth(), args.get(i + 1).toString()), i + 2);
            }
            case ARMOR -> {
                return pair(ref instanceof LivingEntity liv && evalIntegers(liv.getArmor(), args.get(i + 1).toString()), i + 2);
            }
            case POS_X -> {
                return pair(evalIntegers((int)ref.getX(), args.get(i + 1).toString()), i + 2);
            }
            case POS_Y -> {
                return pair(evalIntegers((int)ref.getY(), args.get(i + 1).toString()), i + 2);
            }
            case POS_Z -> {
                return pair(evalIntegers((int)ref.getZ(), args.get(i + 1).toString()), i + 2);
            }
            case MODULE_ENABLED -> {
                assertClientPlayer();

                Module m = system.getModuleById(args.get(i + 1).toString());
                return pair(m != null && m.isEnabled(), i + 2);
            }
            case MODULE_DISABLED -> {
                assertClientPlayer();

                Module m = system.getModuleById(args.get(i + 1).toString());
                return pair(m != null && !m.isEnabled(), i + 2);
            }
            case BLOCK -> {
                VectorParser loc = new VectorParser(
                        args.get(i + 1).toString(),
                        args.get(i + 2).toString(),
                        args.get(i + 3).toString(),
                        ref
                );
                Predicate<BlockState> pre = ScriptParser.parseBlockPredicate(args.get(i + 4).toString());
                return pair(pre.test(loc.getBlock(PlayerUtils.getWorld())), i + 5);
            }
            case DIMENSION -> {
                boolean bl = false;
                switch (args.get(i + 1).toEnum(Dimensions.class, null)) {
                    case OVERWORLD -> bl = Dimensions.isOverworld();
                    case THE_NETHER -> bl = Dimensions.isNether();
                    case THE_END -> bl = Dimensions.isEnd();
                }
                return pair(bl, i + 2);
            }
            case EFFECT_AMPLIFIER -> {
                StatusEffectInstance effect = EntityUtils.getEffect(ref, ScriptParser.parseStatusEffect(args.get(i + 1).toString()));
                return pair(evalIntegers(effect.getAmplifier(), args.get(i + 2).toString()), i + 3);
            }
            case EFFECT_DURATION -> {
                StatusEffectInstance effect = EntityUtils.getEffect(ref, ScriptParser.parseStatusEffect(args.get(i + 1).toString()));
                return pair(evalIntegers(effect.getDuration(), args.get(i + 2).toString()), i + 3);
            }
            case IN_GAME -> {
                assertClientPlayer();
                return pair(PlayerUtils.valid(), i + 1);
            }
            case PLAYING -> {
                assertClientPlayer();
                return pair(PlayerUtils.valid() && mc.currentScreen == null, i + 1);
            }
            case IN_SCREEN -> {
                assertClientPlayer();
                return pair(PlayerUtils.valid() && mc.currentScreen != null, i + 1);
            }
            case CHANCE_OF -> {
                return pair(Math.random() * 100 < args.get(i + 1).toDouble(), i + 2);
            }
            case COLLIDING -> {
                return pair(EntityUtils.isColliding(ref), i + 1);
            }
            case COLLIDING_HORIZONTALLY -> {
                return pair(EntityUtils.isCollidingHorizontally(ref), i + 1);
            }
            case COLLIDING_VERTICALLY -> {
                return pair(EntityUtils.isCollidingVertically(ref), i + 1);
            }
            case MOVING -> {
                return pair(EntityUtils.isMoving(ref), i + 1);
            }
            case BLOCKING -> {
                return pair(EntityUtils.isBlocking(ref), i + 1);
            }
            case CURSOR_ITEM -> {
                assertClientPlayer();

                ClientPlayerEntity p = PlayerUtils.player();
                Predicate<ItemStack> item = ScriptParser.parseItemPredicate(args.get(i + 1).toString());
                if (p == null || p.currentScreenHandler == null)
                    return pair(false, i + 2);
                ItemStack stack = p.currentScreenHandler.getCursorStack();
                return pair(stack != null && item.test(stack), i + 2);
            }
            case REFERENCE_ENTITY -> {
                if (args.match(i + 1, "client")) {
                    return pair(ref == PlayerUtils.player(), i + 2);
                }
                Predicate<Entity> filter = args.match(i + 1, "any_entity") ? entity -> true : ScriptParser.parseEntityPredicate(args.get(i + 1).toString());
                return pair(filter.test(ref), i + 2);
            }
        }
        return pair(false, 0);
    }

    public static void assertClientPlayer() {
        if (AsCmd.getCurrentReferenceEntity() != PlayerUtils.player())
            throw new IllegalArgumentException("unsupported action on non-client player or entity; did you unintentionally use the 'as' command?");
    }

    private static <L, R> Pair<L, R> pair(L left, R right) {
        AsCmd.resetReferenceEntity();
        return Pair.of(left, right);
    }

    public enum ConditionType {
        TRUE,
        FALSE,
        HOLDING,
        OFF_HOLDING,
        INVENTORY_HAS,
        HOTBAR_HAS,
        EQUIPMENT_HAS,
        TARGET_BLOCK,
        TARGET_ENTITY,
        TARGETING_BLOCK,
        TARGETING_ENTITY,
        INPUT_ACTIVE,
        BLOCK_IN_RANGE,
        ENTITY_IN_RANGE,
        ATTACK_PROGRESS,
        HEALTH,
        ARMOR,
        POS_X,
        POS_Y,
        POS_Z,
        MODULE_ENABLED,
        MODULE_DISABLED,
        BLOCK,
        DIMENSION,
        EFFECT_DURATION,
        EFFECT_AMPLIFIER,
        IN_GAME,
        PLAYING,
        CHANCE_OF,
        IN_SCREEN,
        COLLIDING,
        COLLIDING_HORIZONTALLY,
        COLLIDING_VERTICALLY,
        MOVING,
        CURSOR_ITEM,
        REFERENCE_ENTITY,
        BLOCKING
    }

    /**
     * Evaluates if input [>, <, =, >=, <=, ==, !=] other number
     * @param input a number
     * @param other other number as string
     * @return
     */
    public static boolean evalIntegers(double input, String other) {
        if (other.isEmpty()) {
            return false;
        }

        else if (other.startsWith("<=")) {
            return input <= Double.parseDouble(other.substring(2));
        }
        else if (other.startsWith(">=")) {
            return input >= Double.parseDouble(other.substring(2));
        }
        else if (other.startsWith("!=")) {
            return input != Double.parseDouble(other.substring(2));
        }
        else if (other.startsWith("==")) {
            return input == Double.parseDouble(other.substring(2));
        }
        else if (other.startsWith("<")) {
            return input < Double.parseDouble(other.substring(1));
        }
        else if (other.startsWith(">")) {
            return input > Double.parseDouble(other.substring(1));
        }
        else if (other.startsWith("!")) {
            return input != Double.parseDouble(other.substring(1));
        }
        else if (other.startsWith("=")) {
            return input == Double.parseDouble(other.substring(1));
        }
        else {
            return input == Double.parseDouble(other);
        }
    }

    public enum Dimensions {
        OVERWORLD,
        THE_END,
        THE_NETHER;

        public static boolean isOverworld() {
            return check(DimensionTypes.OVERWORLD, DimensionTypes.OVERWORLD_CAVES);
        }

        public static boolean isNether() {
            return check(DimensionTypes.THE_NETHER);
        }

        public static boolean isEnd() {
            return check(DimensionTypes.THE_END);
        }

        @SafeVarargs
        private static boolean check(RegistryKey<DimensionType>... dimKeys) {
            if (PlayerUtils.invalid()) {
                return false;
            }
            var entry = PlayerUtils.getWorld().getDimensionEntry().getKey();
            if (entry.isEmpty())
                return false;

            var target = entry.get();
            for (RegistryKey<DimensionType> key : dimKeys) {
                if (key == target) {
                    return true;
                }
            }
            return false;
        }
    }
}
