package io.github.itzispyder.clickcrystals.modules.scripts.syntax;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.scripts.macros.InputCmd;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.VectorParser;
import io.github.itzispyder.clickcrystals.util.misc.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.function.Predicate;

public class IfCmd extends ScriptCommand implements Global {

    public IfCmd() {
        super("if");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        int beginIndex = 0;
        ConditionType type = args.get(beginIndex).toEnum(ConditionType.class, null);
        var condition = parseCondition(type, args, beginIndex);

        if (condition.left) {
            OnEventCmd.executeWithThen(args, condition.right);
        }
    }

    /**
     * Parse clickscript condition
     * @param type target condition type
     * @param args script command arguments
     * @param beginIndex begin read index
     * @return pair(yourCondition, theNextBeginIndex)
     */
    public static Pair<Boolean, Integer> parseCondition(ConditionType type, ScriptArgs args, int beginIndex) {
        int i = beginIndex;
        switch (type) {
            case HOLDING -> {
                return pair(OnEventCmd.parseItemPredicate(args.get(i + 1).toString()).test(HotbarUtils.getHand()), i + 2);
            }
            case OFF_HOLDING -> {
                return pair(OnEventCmd.parseItemPredicate(args.get(i + 1).toString()).test(HotbarUtils.getHand(Hand.OFF_HAND)), i + 2);
            }
            case TARGET_BLOCK -> {
                boolean bl = mc.crosshairTarget instanceof BlockHitResult hit && OnEventCmd.parseBlockPredicate(args.get(i + 1).toString()).test(PlayerUtils.getWorld().getBlockState(hit.getBlockPos()));
                return pair(bl, i + 2);
            }
            case TARGET_ENTITY -> {
                boolean bl = mc.crosshairTarget instanceof EntityHitResult hit && OnEventCmd.parseEntityPredicate(args.get(i + 1).toString()).test(hit.getEntity());
                return pair(bl, i + 2);
            }
            case TARGETING_BLOCK -> {
                return pair(mc.crosshairTarget instanceof BlockHitResult hit && !PlayerUtils.getWorld().getBlockState(hit.getBlockPos()).isAir(), i + 1);
            }
            case TARGETING_ENTITY -> {
                return pair(mc.crosshairTarget instanceof EntityHitResult hit && hit.getEntity().isAlive(), i + 1);
            }
            case INVENTORY_HAS -> {
                return pair(InvUtils.has(OnEventCmd.parseItemPredicate(args.get(i + 1).toString())), i + 2);
            }
            case HOTBAR_HAS -> {
                return pair(HotbarUtils.has(OnEventCmd.parseItemPredicate(args.get(i + 1).toString())), i + 2);
            }
            case INPUT_ACTIVE -> {
                return pair(args.get(i + 1).toEnum(InputCmd.Action.class, null).isActive(), i + 2);
            }
            case BLOCK_IN_RANGE -> {
                Predicate<BlockState> filter = args.match(i + 1, "any_block") ? state -> true : OnEventCmd.parseBlockPredicate(args.get(i + 1).toString());
                double range = args.get(i + 2).toDouble();
                boolean[] bl = { false };
                PlayerUtils.runOnNearestBlock(range, filter, (pos, state) -> {
                    bl[0] = pos.toCenterPos().distanceTo(PlayerUtils.getPos()) <= range;
                });
                return pair(bl[0], i + 3);
            }
            case ENTITY_IN_RANGE -> {
                Predicate<Entity> filter = args.match(i + 1, "any_entity") ? entity -> true : OnEventCmd.parseEntityPredicate(args.get(i + 1).toString());
                double range = args.get(i + 2).toDouble();
                boolean[] bl = { false };
                PlayerUtils.runOnNearestEntity(range, filter, entity -> {
                    bl[0] = entity.distanceTo(PlayerUtils.player()) <= range;
                });
                return pair(bl[0], i + 3);
            }
            case ATTACK_PROGRESS -> {
                return pair(evalIntegers(PlayerUtils.player().getAttackCooldownProgress(1.0F), args.get(i + 1).toString()), i + 2);
            }
            case HEALTH -> {
                return pair(evalIntegers((int)PlayerUtils.player().getHealth(), args.get(i + 1).toString()), i + 2);
            }
            case ARMOR -> {
                return pair(evalIntegers(PlayerUtils.player().getArmor(), args.get(i + 1).toString()), i + 2);
            }
            case POS_X -> {
                return pair(evalIntegers((int)PlayerUtils.getPos().getX(), args.get(i + 1).toString()), i + 2);
            }
            case POS_Y -> {
                return pair(evalIntegers((int)PlayerUtils.getPos().getY(), args.get(i + 1).toString()), i + 2);
            }
            case POS_Z -> {
                return pair(evalIntegers((int)PlayerUtils.getPos().getZ(), args.get(i + 1).toString()), i + 2);
            }
            case MODULE_ENABLED -> {
                Module m = system.getModuleById(args.get(i + 1).toString());
                return pair(m != null && m.isEnabled(), i + 2);
            }
            case MODULE_DISABLED -> {
                Module m = system.getModuleById(args.get(i + 1).toString());
                return pair(m != null && !m.isEnabled(), i + 2);
            }
            case BLOCK -> {
                VectorParser loc = new VectorParser(
                        args.get(i + 1).toString(),
                        args.get(i + 2).toString(),
                        args.get(i + 3).toString(),
                        PlayerUtils.player()
                );
                Predicate<BlockState> pre = OnEventCmd.parseBlockPredicate(args.get(i + 4).toString());
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
                StatusEffect statusEffect = OnEventCmd.parseStatusEffect(args.get(i + 1).toString());
                StatusEffectInstance effect = PlayerUtils.player().getStatusEffect(statusEffect);
                if (effect == null) {
                    effect = new StatusEffectInstance(statusEffect);
                }
                return pair(evalIntegers(effect.getAmplifier(), args.get(i + 2).toString()), i + 3);
            }
            case EFFECT_DURATION -> {
                StatusEffect statusEffect = OnEventCmd.parseStatusEffect(args.get(i + 1).toString());
                StatusEffectInstance effect = PlayerUtils.player().getStatusEffect(statusEffect);
                if (effect == null) {
                    effect = new StatusEffectInstance(statusEffect);
                }
                return pair(evalIntegers(effect.getDuration(), args.get(i + 2).toString()), i + 3);
            }
            case IN_GAME -> {
                return pair(mc != null && mc.world != null && mc.player != null, i + 1);
            }
            case PLAYING -> {
                return pair(mc != null && mc.world != null && mc.player != null && mc.currentScreen == null, i + 1);
            }
        }
        return pair(false, 0);
    }

    private static <L, R> Pair<L, R> pair(L left, R right) {
        return Pair.of(left, right);
    }

    public enum ConditionType {
        HOLDING,
        OFF_HOLDING,
        INVENTORY_HAS,
        HOTBAR_HAS,
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
        PLAYING
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
            if (PlayerUtils.playerNull()) {
                return false;
            }
            RegistryKey<DimensionType> target = PlayerUtils.getWorld().getDimensionKey();
            for (RegistryKey<DimensionType> key : dimKeys) {
                if (key == target) {
                    return true;
                }
            }
            return false;
        }
    }
}
