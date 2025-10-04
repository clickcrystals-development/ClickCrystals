package io.github.itzispyder.clickcrystals.scripting.syntax.macros;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.syntax.TargetType;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PolarParser;
import io.github.itzispyder.clickcrystals.util.minecraft.VectorParser;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

// @Format turn_to (nearest_entity|nearest_block) <identifier> then? {}?
// @Format turn_to (any_entity|target_entity|any_block) then? {}?
// @Format turn_to position <x> <y> <z> then? {}?
// @Format turn_to polar <pitch> <yaw> then? {}?
public class TurnToCmd extends ScriptCommand {

    public TurnToCmd() {
        super("turn_to");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (PlayerUtils.invalid()) {
            return;
        }

        // ex.      turn_to nearest_entity :creeper then say Yo
        Vec3d eyes = PlayerUtils.player().getEyePos();
        var read = args.getReader();

        switch (read.next(TargetType.class)) {
            case NEAREST_BLOCK -> {
                Predicate<BlockState> filter = ScriptParser.parseBlockPredicate(read.nextStr());
                PlayerUtils.runOnNearestBlock(32, filter, (pos, state) -> turn(pos.toCenterPos(), eyes, args));
            }
            case NEAREST_ENTITY -> {
                Predicate<Entity> filter = ScriptParser.parseEntityPredicate(read.nextStr());
                PlayerUtils.runOnNearestEntity(128, filter, entity -> {
                    if (!(entity instanceof PlayerEntity) || !EntityUtils.isTeammate((PlayerEntity) entity))
                        turn(entity instanceof LivingEntity le ? le.getEyePos() : entity.getPos(), eyes, args);
                });
            }

            case ANY_BLOCK -> PlayerUtils.runOnNearestBlock(32, (pos, state) -> true, (pos, state) -> turn(pos.toCenterPos(), eyes, args));
            case ANY_ENTITY -> PlayerUtils.runOnNearestEntity(128, Entity::isAlive, entity -> {
                if (!(entity instanceof PlayerEntity) || !EntityUtils.isTeammate(((PlayerEntity) entity)))
                    turn(entity instanceof LivingEntity le ? le.getEyePos() : entity.getPos(), eyes, args);
            });

            case POSITION -> {
                VectorParser parser = new VectorParser(
                        read.nextStr(),
                        read.nextStr(),
                        read.nextStr(),
                        PlayerUtils.player()
                );
                turn(parser.getVector(), eyes, args);
            }
            case POLAR -> {
                PolarParser parser = new PolarParser(
                        read.nextStr(),
                        read.nextStr(),
                        PlayerUtils.player()
                );
                turn(eyes.add(parser.getVector()), eyes, args);
            }

            default -> throw new IllegalArgumentException("unsupported operation");
        }
    }

    private void turn(Vec3d dest, Vec3d camPos, ScriptArgs args) {
        if (!system.cameraRotator.isRunningTicket()) {
            Vec3d target = dest.subtract(camPos).normalize();

            system.cameraRotator.ready()
                    .addTicket(target, 10, 10, true)
                    .setFinishCallback((pitch, yaw, rotator) -> args.getReader().executeThenChain())
                    .openCurrentTicket();
        }
    }
}
