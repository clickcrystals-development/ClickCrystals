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
import net.minecraft.entity.LivingEntity;
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

        switch (args.get(0).toEnum(TargetType.class, null)) {
            case NEAREST_ENTITY -> {
                Predicate<Entity> filter = ScriptParser.parseEntityPredicate(args.get(1).toString());
                PlayerUtils.runOnNearestEntity(128, filter, entity -> mc.interactionManager.attackEntity(mc.player, entity));
                executeWithThen(args, 2);
            }
            case ANY_ENTITY -> {
                PlayerUtils.runOnNearestEntity(128, ENTITY_EXISTS, entity -> mc.interactionManager.attackEntity(mc.player, entity));
                executeWithThen(args, 1);
            }
            case NEAREST_BLOCK -> {
                Predicate<BlockState> filter = ScriptParser.parseBlockPredicate(args.get(1).toString());
                PlayerUtils.runOnNearestBlock(32, filter, (pos, state) -> {
                    Vec3d vector = PlayerUtils.getEyes().subtract(pos.toCenterPos());
                    Direction face = Direction.getFacing(vector);
                    mc.interactionManager.attackBlock(pos, face);
                });
                executeWithThen(args, 2);
            }
            case ANY_BLOCK -> {
                PlayerUtils.runOnNearestBlock(32, (pos, state) -> true, (pos, state) -> {
                    Vec3d vector = PlayerUtils.getEyes().subtract(pos.toCenterPos());
                    Direction face = Direction.getFacing(vector);
                    mc.interactionManager.attackBlock(pos, face);
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
                mc.interactionManager.attackBlock(pos, face);
                executeWithThen(args, 4);
            }
            default -> throw new IllegalArgumentException("unsupported operation");
        }
    }
}