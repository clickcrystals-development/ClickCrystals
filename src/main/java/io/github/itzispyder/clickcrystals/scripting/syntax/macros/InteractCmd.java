package io.github.itzispyder.clickcrystals.scripting.syntax.macros;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.syntax.TargetType;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.VectorParser;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

// @Format interact (nearest_entity|nearest_block) <identifier>
// @Format interact (any_entity|target_entity|any_block)
// @Format interact position <x> <y> <z>
public class InteractCmd extends ScriptCommand implements ThenChainable {

    public InteractCmd() {
        super("interact");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (mc.interactionManager == null) {
            return;
        }

        switch (args.get(0).toEnum(TargetType.class, null)) {
            case NEAREST_ENTITY -> {
                Predicate<Entity> filter = ScriptParser.parseEntityPredicate(args.get(1).toString());
                PlayerUtils.runOnNearestEntity(128, filter, entity -> mc.interactionManager.interactEntity(mc.player, entity, Hand.MAIN_HAND));
                executeWithThen(args, 2);
            }
            case ANY_ENTITY -> {
                PlayerUtils.runOnNearestEntity(128, DamageCmd.ENTITY_EXISTS, entity -> mc.interactionManager.interactEntity(mc.player, entity, Hand.MAIN_HAND));
                executeWithThen(args, 1);
            }
            case NEAREST_BLOCK -> {
                Predicate<BlockState> filter = ScriptParser.parseBlockPredicate(args.get(1).toString());
                PlayerUtils.runOnNearestBlock(32, filter, (pos, state) -> {
                    Vec3d vector = PlayerUtils.getEyes().subtract(pos.toCenterPos());
                    Direction face = Direction.getFacing(vector);
                    BlockHitResult hit = new BlockHitResult(pos.toCenterPos(), face, pos, false);
                    mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                });
                executeWithThen(args, 2);
            }
            case ANY_BLOCK -> {
                PlayerUtils.runOnNearestBlock(32, (pos, state) -> true, (pos, state) -> {
                    Vec3d vector = PlayerUtils.getEyes().subtract(pos.toCenterPos());
                    Direction face = Direction.getFacing(vector);
                    BlockHitResult hit = new BlockHitResult(pos.toCenterPos(), face, pos, false);
                    mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                });
                executeWithThen(args, 1);
            }
            case POSITION -> {
                VectorParser parser = new VectorParser(
                        args.get(1).toString(),
                        args.get(2).toString(),
                        args.get(3).toString(),
                        PlayerUtils.player()
                );
                BlockPos pos = BlockPos.ofFloored(parser.getVector());
                Vec3d vector = PlayerUtils.getEyes().subtract(pos.toCenterPos());
                Direction face = Direction.getFacing(vector);
                BlockHitResult hit = new BlockHitResult(pos.toCenterPos(), face, pos, false);
                mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                executeWithThen(args, 4);
            }
            default -> throw new IllegalArgumentException("unsupported operation");
        }
    }
}