package io.github.itzispyder.clickcrystals.scripting.syntax.macros;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgsReader;
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
        ScriptArgsReader sar = args.getReader();

        switch (sar.next(TargetType.class)) {
            case NEAREST_BLOCK -> {
                Predicate<BlockState> filter = ScriptParser.parseBlockPredicate(sar.nextStr());
                PlayerUtils.runOnNearestBlock(32, filter, (pos, state) -> turn(2, pos.toCenterPos(), eyes, args));
            }
            case NEAREST_ENTITY -> {
                Predicate<Entity> filter = ScriptParser.parseEntityPredicate(sar.nextStr());
                PlayerUtils.runOnNearestEntity(128, filter, entity -> {
                    if (!(entity instanceof PlayerEntity) || !EntityUtils.isTeammate((PlayerEntity) entity))
                        turn(2, entity instanceof LivingEntity le ? le.getEyePos() : entity.getPos(), eyes, args);
                });
            }

            case ANY_BLOCK -> PlayerUtils.runOnNearestBlock(32, (pos, state) -> true, (pos, state) -> turn(1, pos.toCenterPos(), eyes, args));
            case ANY_ENTITY -> PlayerUtils.runOnNearestEntity(128, Entity::isAlive, entity -> {
                if (!(entity instanceof PlayerEntity) || !EntityUtils.isTeammate(((PlayerEntity) entity)))
                    turn(1, entity instanceof LivingEntity le ? le.getEyePos() : entity.getPos(), eyes, args);
            });

            case POSITION -> {
                VectorParser parser = new VectorParser(
                        sar.nextStr(),
                        sar.nextStr(),
                        sar.nextStr(),
                        PlayerUtils.player()
                );
                turn(4, parser.getVector(), eyes, args);
            }
            case POLAR -> {
                PolarParser parser = new PolarParser(
                        sar.nextStr(),
                        sar.nextStr(),
                        PlayerUtils.player()
                );
                turn(3, eyes.add(parser.getVector()), eyes, args);
            }

            default -> throw new IllegalArgumentException("unsupported operation");
        }
    }

    private void turn(int zeroCursor, Vec3d dest, Vec3d camPos, ScriptArgs args) {
        if (!system.cameraRotator.isRunningTicket()) {
            Vec3d target = dest.subtract(camPos).normalize();

            system.cameraRotator.ready()
                    .addTicket(target, 10, 10, true)
                    .openCurrentTicket();

            args.zeroCursor(zeroCursor);
            if (args.match(0, "then"))
                system.cameraRotator.setFinishCallback((pitch, yaw, cameraRotator) -> args.executeAll(1));
        }
    }
}
