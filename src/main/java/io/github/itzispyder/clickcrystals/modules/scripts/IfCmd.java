package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.LocationParser;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
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
        ConditionType type = args.get(0).enumValue(ConditionType.class, null);

        if (mc == null || PlayerUtils.playerNull()) {
            return;
        }

        switch (type) {
            case HOLDING -> {
                if (OnEventCmd.parseItemPredicate(args.get(1).stringValue()).test(HotbarUtils.getHand())) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case OFF_HOLDING -> {
                if (OnEventCmd.parseItemPredicate(args.get(1).stringValue()).test(HotbarUtils.getHand(Hand.OFF_HAND))) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case TARGET_BLOCK -> {
                if (mc.crosshairTarget instanceof BlockHitResult hit) {
                    if (OnEventCmd.parseBlockPredicate(args.get(1).stringValue()).test(PlayerUtils.getWorld().getBlockState(hit.getBlockPos()))) {
                        OnEventCmd.executeWithThen(args, 2);
                    }
                }
            }
            case TARGET_ENTITY -> {
                if (mc.crosshairTarget instanceof EntityHitResult hit) {
                    if (OnEventCmd.parseEntityPredicate(args.get(1).stringValue()).test(hit.getEntity())) {
                        OnEventCmd.executeWithThen(args, 2);
                    }
                }
            }
            case INVENTORY_HAS -> {
                if (InvUtils.has(OnEventCmd.parseItemPredicate(args.get(1).stringValue()))) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case HOTBAR_HAS -> {
                if (HotbarUtils.has(OnEventCmd.parseItemPredicate(args.get(1).stringValue()))) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case INPUT_ACTIVE -> {
                if (args.get(1).enumValue(InputCmd.Action.class, null).isActive()) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case BLOCK_IN_RANGE -> {
                Predicate<BlockState> filter = args.match(1, "any_block") ? state -> true : OnEventCmd.parseBlockPredicate(args.get(1).stringValue());
                double range = args.get(2).doubleValue();
                PlayerUtils.runOnNearestBlock(range, filter, (pos, state) -> {
                    if (pos.toCenterPos().distanceTo(PlayerUtils.getPos()) <= range) {
                        OnEventCmd.executeWithThen(args, 3);
                    }
                });
            }
            case ENTITY_IN_RANGE -> {
                Predicate<Entity> filter = args.match(1, "any_entity") ? entity -> true : OnEventCmd.parseEntityPredicate(args.get(1).stringValue());
                double range = args.get(2).doubleValue();
                PlayerUtils.runOnNearestEntity(range, filter, entity -> {
                    if (entity.distanceTo(PlayerUtils.player()) <= range) {
                        OnEventCmd.executeWithThen(args, 3);
                    }
                });
            }
            case ATTACK_PROGRESS -> {
                if (evalIntegers(PlayerUtils.player().getAttackCooldownProgress(1.0F), args.get(1).stringValue())) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case HEALTH -> {
                if (evalIntegers((int)PlayerUtils.player().getHealth(), args.get(1).stringValue())) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case ARMOR -> {
                if (evalIntegers(PlayerUtils.player().getArmor(), args.get(1).stringValue())) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case POS_X -> {
                if (evalIntegers((int)PlayerUtils.getPos().getX(), args.get(1).stringValue())) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case POS_Y -> {
                if (evalIntegers((int)PlayerUtils.getPos().getY(), args.get(1).stringValue())) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case POS_Z -> {
                if (evalIntegers((int)PlayerUtils.getPos().getZ(), args.get(1).stringValue())) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case MODULE_ENABLED -> {
                Module m = system.getModuleById(args.get(1).stringValue());
                if (m != null && m.isEnabled()) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case BLOCK -> {
                LocationParser loc = new LocationParser(
                        args.get(1).stringValue(),
                        args.get(2).stringValue(),
                        args.get(3).stringValue(),
                        PlayerUtils.getPos()
                );
                Predicate<BlockState> pre = OnEventCmd.parseBlockPredicate(args.get(4).stringValue());
                if (pre.test(loc.getBlock(PlayerUtils.getWorld()))) {
                    OnEventCmd.executeWithThen(args, 5);
                }
            }
            case DIMENSION -> {
                boolean bl = false;
                switch (args.get(1).enumValue(Dimensions.class, null)) {
                    case OVERWORLD -> bl = Dimensions.isOverworld();
                    case THE_NETHER -> bl = Dimensions.isNether();
                    case THE_END -> bl = Dimensions.isEnd();
                }
                if (bl) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
        }
    }

    public enum ConditionType {
        HOLDING,
        OFF_HOLDING,
        INVENTORY_HAS,
        HOTBAR_HAS,
        TARGET_BLOCK,
        TARGET_ENTITY,
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
        BLOCK,
        DIMENSION
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

        if (other.startsWith("<=")) {
            return input <= new ScriptArgs(other.substring(2)).get(0).doubleValue();
        }
        else if (other.startsWith(">=")) {
            return input >= new ScriptArgs(other.substring(2)).get(0).doubleValue();
        }
        else if (other.startsWith("!=") || other.startsWith("!")) {
            return input != new ScriptArgs(other.substring(2)).get(0).doubleValue();
        }
        else if (other.startsWith("==") || other.startsWith("=")) {
            return input == new ScriptArgs(other.substring(2)).get(0).doubleValue();
        }
        else if (other.startsWith("<")) {
            return input < new ScriptArgs(other.substring(1)).get(0).doubleValue();
        }
        else if (other.startsWith(">")) {
            return input > new ScriptArgs(other.substring(1)).get(0).doubleValue();
        }
        else {
            return input == new ScriptArgs(other).get(0).doubleValue();
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
