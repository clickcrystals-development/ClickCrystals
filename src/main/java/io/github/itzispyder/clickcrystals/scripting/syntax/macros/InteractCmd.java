package io.github.itzispyder.clickcrystals.scripting.syntax.macros;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.syntax.TargetType;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.VectorParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

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
        if (mc.gameMode == null) {
            return;
        }

        var read = args.getReader();

        switch (read.next(TargetType.class)) {
            case NEAREST_ENTITY -> {
                Predicate<Entity> filter = ScriptParser.parseEntityPredicate(read.nextStr());
                PlayerUtils.runOnNearestEntity(128, filter, entity -> {
                    if (entity instanceof Player player && EntityUtils.shouldCancelCcsAttack(player)) {
                        return; // Skip interacting with teammates
                    }
                    EntityHitResult hitResult = new EntityHitResult(entity, entity.getBoundingBox().getCenter());
                    mc.gameMode.interactAt(mc.player, entity, hitResult, InteractionHand.MAIN_HAND);
                    mc.gameMode.interact(mc.player, entity, InteractionHand.MAIN_HAND);
                });
                read.executeThenChain();
            }
            case ANY_ENTITY -> {
                PlayerUtils.runOnNearestEntity(128, DamageCmd.ENTITY_EXISTS, entity -> {
                    if (entity instanceof Player player && EntityUtils.shouldCancelCcsAttack(player)) {
                        return; // Skip interacting with teammates
                    }
                    EntityHitResult hitResult = new EntityHitResult(entity, entity.getBoundingBox().getCenter());
                    mc.gameMode.interactAt(mc.player, entity, hitResult, InteractionHand.MAIN_HAND);
                    mc.gameMode.interact(mc.player, entity, InteractionHand.MAIN_HAND);
                });
                read.executeThenChain();
            }
            case NEAREST_BLOCK -> {
                Predicate<BlockState> filter = ScriptParser.parseBlockPredicate(read.nextStr());
                PlayerUtils.runOnNearestBlock(32, filter, (pos, state) -> {
                    Vec3 vector = PlayerUtils.getEyes().subtract(pos.getCenter());
                    Direction face = Direction.getApproximateNearest(vector);
                    BlockHitResult hit = new BlockHitResult(pos.getCenter(), face, pos, false);
                    mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, hit);
                });
                read.executeThenChain();
            }
            case ANY_BLOCK -> {
                PlayerUtils.runOnNearestBlock(32, (pos, state) -> true, (pos, state) -> {
                    Vec3 vector = PlayerUtils.getEyes().subtract(pos.getCenter());
                    Direction face = Direction.getApproximateNearest(vector);
                    BlockHitResult hit = new BlockHitResult(pos.getCenter(), face, pos, false);
                    mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, hit);
                });
                read.executeThenChain();
            }
            case POSITION -> {
                VectorParser parser = new VectorParser(
                        read.nextStr(),
                        read.nextStr(),
                        read.nextStr(),
                        PlayerUtils.player()
                );
                BlockPos pos = BlockPos.containing(parser.getVector());
                Vec3 vector = PlayerUtils.getEyes().subtract(pos.getCenter());
                Direction face = Direction.getApproximateNearest(vector);
                BlockHitResult hit = new BlockHitResult(pos.getCenter(), face, pos, false);
                mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, hit);
                read.executeThenChain();
            }
            default -> throw new IllegalArgumentException("unsupported operation");
        }
    }
}