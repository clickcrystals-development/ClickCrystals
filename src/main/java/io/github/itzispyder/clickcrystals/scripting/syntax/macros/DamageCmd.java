package io.github.itzispyder.clickcrystals.scripting.syntax.macros;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.syntax.TargetType;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.VectorParser;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

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
        if (mc.gameMode == null) {
            return;
        }

        var read = args.getReader();

        switch (read.next(TargetType.class)) {
            case NEAREST_ENTITY -> {
                Predicate<Entity> filter = ScriptParser.parseEntityPredicate(read.nextStr());
                PlayerUtils.runOnNearestEntity(128, filter, entity -> {
                    if (entity instanceof Player player && EntityUtils.shouldCancelCcsAttack(player)) {
                        return; // Skip attacking teammates
                    }
                    mc.gameMode.attack(mc.player, entity);
                });
                read.executeThenChain();
            }
            case ANY_ENTITY -> {
                PlayerUtils.runOnNearestEntity(128, ENTITY_EXISTS, entity -> {
                    if (entity instanceof Player player && EntityUtils.shouldCancelCcsAttack(player)) {
                        return; // Skip attacking teammates
                    }
                    mc.gameMode.attack(mc.player, entity);
                });
                read.executeThenChain();
            }
            case NEAREST_BLOCK -> {
                Predicate<BlockState> filter = ScriptParser.parseBlockPredicate(read.nextStr());
                PlayerUtils.runOnNearestBlock(32, filter, (pos, state) -> {
                    Vec3 vector = PlayerUtils.getEyes().subtract(pos.getCenter());
                    Direction face = Direction.getApproximateNearest(vector);
                    mc.gameMode.startDestroyBlock(pos, face);
                });
                read.executeThenChain();
            }
            case ANY_BLOCK -> {
                PlayerUtils.runOnNearestBlock(32, (pos, state) -> true, (pos, state) -> {
                    Vec3 vector = PlayerUtils.getEyes().subtract(pos.getCenter());
                    Direction face = Direction.getApproximateNearest(vector);
                    mc.gameMode.startDestroyBlock(pos, face);
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
                mc.gameMode.startDestroyBlock(pos, face);
                read.executeThenChain();
            }
            default -> throw new IllegalArgumentException("unsupported operation");
        }
    }
}