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
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;

import java.util.function.Predicate;

public class IfNotCmd extends ScriptCommand implements Global {

    public IfNotCmd() {
        super("if_not");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        IfCmd.ConditionType type = args.get(0).toEnum(IfCmd.ConditionType.class, null);

        if (mc == null || PlayerUtils.playerNull()) {
            return;
        }

        switch (type) {
            case HOLDING -> {
                if (!OnEventCmd.parseItemPredicate(args.get(1).toString()).test(HotbarUtils.getHand())) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case OFF_HOLDING -> {
                if (!OnEventCmd.parseItemPredicate(args.get(1).toString()).test(HotbarUtils.getHand(Hand.OFF_HAND))) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case TARGET_BLOCK -> {
                if (mc.crosshairTarget instanceof BlockHitResult hit) {
                    if (!OnEventCmd.parseBlockPredicate(args.get(1).toString()).test(PlayerUtils.getWorld().getBlockState(hit.getBlockPos()))) {
                        OnEventCmd.executeWithThen(args, 2);
                    }
                }
            }
            case TARGET_ENTITY -> {
                if (mc.crosshairTarget instanceof EntityHitResult hit) {
                    if (!OnEventCmd.parseEntityPredicate(args.get(1).toString()).test(hit.getEntity())) {
                        OnEventCmd.executeWithThen(args, 2);
                    }
                }
            }
            case INVENTORY_HAS -> {
                if (!InvUtils.has(OnEventCmd.parseItemPredicate(args.get(1).toString()))) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case HOTBAR_HAS -> {
                if (!HotbarUtils.has(OnEventCmd.parseItemPredicate(args.get(1).toString()))) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case INPUT_ACTIVE -> {
                if (!args.get(1).toEnum(InputCmd.Action.class, null).isActive()) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case BLOCK_IN_RANGE -> {
                Predicate<BlockState> filter = args.match(1, "any_block") ? state -> true : OnEventCmd.parseBlockPredicate(args.get(1).toString());
                double range = args.get(2).toDouble();
                boolean result = PlayerUtils.runOnNearestBlock(range, filter, (pos, state) -> {
                    if (pos.toCenterPos().distanceTo(PlayerUtils.getPos()) > range) {
                        OnEventCmd.executeWithThen(args, 3);
                    }
                });
                if (!result) {
                    OnEventCmd.executeWithThen(args, 3);
                }
            }
            case ENTITY_IN_RANGE -> {
                Predicate<Entity> filter = args.match(1, "any_entity") ? entity -> true : OnEventCmd.parseEntityPredicate(args.get(1).toString());
                double range = args.get(2).toDouble();
                boolean result = PlayerUtils.runOnNearestEntity(range, filter, entity -> {
                    if (entity.distanceTo(PlayerUtils.player()) > range) {
                        OnEventCmd.executeWithThen(args, 3);
                    }
                });
                if (!result) {
                    OnEventCmd.executeWithThen(args, 3);
                }
            }
            case ATTACK_PROGRESS -> {
                if (!IfCmd.evalIntegers(PlayerUtils.player().getAttackCooldownProgress(1.0F), args.get(1).toString())) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case HEALTH -> {
                if (!IfCmd.evalIntegers((int)PlayerUtils.player().getHealth(), args.get(1).toString())) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case ARMOR -> {
                if (!IfCmd.evalIntegers(PlayerUtils.player().getArmor(), args.get(1).toString())) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case POS_X -> {
                if (!IfCmd.evalIntegers((int)PlayerUtils.getPos().getX(), args.get(1).toString())) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case POS_Y -> {
                if (!IfCmd.evalIntegers((int)PlayerUtils.getPos().getY(), args.get(1).toString())) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case POS_Z -> {
                if (!IfCmd.evalIntegers((int)PlayerUtils.getPos().getZ(), args.get(1).toString())) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case MODULE_ENABLED -> {
                Module m = system.getModuleById(args.get(1).toString());
                if (m == null || !m.isEnabled()) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
            case BLOCK -> {
                LocationParser loc = new LocationParser(
                        args.get(1).toString(),
                        args.get(2).toString(),
                        args.get(3).toString(),
                        PlayerUtils.getPos()
                );
                Predicate<BlockState> pre = OnEventCmd.parseBlockPredicate(args.get(4).toString());
                if (!pre.test(loc.getBlock(PlayerUtils.getWorld()))) {
                    OnEventCmd.executeWithThen(args, 5);
                }
            }
            case DIMENSION -> {
                boolean bl = false;
                switch (args.get(1).toEnum(IfCmd.Dimensions.class, null)) {
                    case OVERWORLD -> bl = IfCmd.Dimensions.isOverworld();
                    case THE_NETHER -> bl = IfCmd.Dimensions.isNether();
                    case THE_END -> bl = IfCmd.Dimensions.isEnd();
                }
                if (!bl) {
                    OnEventCmd.executeWithThen(args, 2);
                }
            }
        }
    }
}
