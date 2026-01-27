package io.github.itzispyder.clickcrystals.scripting.syntax.macros;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.syntax.TargetType;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.VectorParser;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

// @Format damage (nearest_entity|nearest_block) <identifier>
// @Format damage (any_entity|target_entity|any_block)
// @Format damage position <x> <y> <z>
public class DamageCmd extends ScriptCommand implements ThenChainable {

    public static final Predicate<Entity> ENTITY_EXISTS = ent -> ent instanceof LivingEntity && ent.isAlive();
  
    public DamageCmd() {
        super("damage");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (mc.interactionManager == null) {
            return;
        }

        var read = args.getReader();

        switch (read.next(TargetType.class)) {
            case NEAREST_ENTITY -> {
                Predicate<Entity> filter = ScriptParser.parseEntityPredicate(read.nextStr());
                PlayerUtils.runOnNearestEntity(128, filter, entity -> {
                    if (entity instanceof PlayerEntity player && EntityUtils.shouldCancelCcsAttack(player)) {
                        return; // Skip attacking teammates
                    }
                    mc.interactionManager.attackEntity(mc.player, entity);
                });
                read.executeThenChain();
            }
            case ANY_ENTITY -> {
                PlayerUtils.runOnNearestEntity(128, ENTITY_EXISTS, entity -> {
                    if (entity instanceof PlayerEntity player && EntityUtils.shouldCancelCcsAttack(player)) {
                        return; // Skip attacking teammates
                    }
                    mc.interactionManager.attackEntity(mc.player, entity);
                });
                read.executeThenChain();
            }
            case NEAREST_BLOCK -> {
                Predicate<BlockState> filter = ScriptParser.parseBlockPredicate(read.nextStr());
                PlayerUtils.runOnNearestBlock(32, filter, (pos, state) -> {
                    Vec3d vector = PlayerUtils.getEyes().subtract(pos.toCenterPos());
                    Direction face = Direction.getFacing(vector);
                    mc.interactionManager.attackBlock(pos, face);
                });
                read.executeThenChain();
            }
            case ANY_BLOCK -> {
                PlayerUtils.runOnNearestBlock(32, (pos, state) -> true, (pos, state) -> {
                    Vec3d vector = PlayerUtils.getEyes().subtract(pos.toCenterPos());
                    Direction face = Direction.getFacing(vector);
                    mc.interactionManager.attackBlock(pos, face);
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
                BlockPos pos = BlockPos.ofFloored(parser.getVector());
                Vec3d vector = PlayerUtils.getEyes().subtract(pos.toCenterPos());
                Direction face = Direction.getFacing(vector);
                mc.interactionManager.attackBlock(pos, face);
                read.executeThenChain();
            }
            default -> throw new IllegalArgumentException("unsupported operation");
        }
    }
}