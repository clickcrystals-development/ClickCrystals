package io.github.itzispyder.clickcrystals.scripting.syntax.macros;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.syntax.TargetType;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PolarParser;
import io.github.itzispyder.clickcrystals.util.minecraft.VectorParser;
import io.github.itzispyder.clickcrystals.util.misc.CameraRotator;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

// @Format snap_to (nearest_entity|nearest_block) <identifier> then? {}?
// @Format snap_to (any_entity|target_entity|any_block) then? {}?
// @Format snap_to position <x> <y> <z> then? {}?
// @Format snap_to polar <pitch> <yaw> then? {}?
public class SnapToCmd extends ScriptCommand {

    public SnapToCmd() {
        super("snap_to");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (PlayerUtils.invalid()) {
            return;
        }

        // ex.      turn_to nearest_entity :creeper then say Yo
        Vec3d eyes = PlayerUtils.player().getEyePos();

        switch (args.get(0).toEnum(TargetType.class, null)) {
            case NEAREST_BLOCK -> {
                Predicate<BlockState> filter = ScriptParser.parseBlockPredicate(args.get(1).toString());
                PlayerUtils.runOnNearestBlock(32, filter, (pos, state) -> snap(2, pos.toCenterPos(), eyes, args));
            }

            case NEAREST_ENTITY -> {
                Predicate<Entity> filter = ScriptParser.parseEntityPredicate(args.get(1).toString());
                PlayerUtils.runOnNearestEntity(128, filter, entity -> {
                    if (!(entity instanceof PlayerEntity) || !EntityUtils.isTeammate((PlayerEntity) entity))
                        snap(2, entity instanceof LivingEntity le ? le.getEyePos() : entity.getPos(), eyes, args);
                });
            }

            case ANY_BLOCK -> PlayerUtils.runOnNearestBlock(32, (pos, state) -> true, (pos, state) -> snap(1, pos.toCenterPos(), eyes, args));
            case ANY_ENTITY -> PlayerUtils.runOnNearestEntity(128, Entity::isAlive, entity -> {
                if (!(entity instanceof PlayerEntity) || !EntityUtils.isTeammate(((PlayerEntity) entity)))
                    snap(1, entity instanceof LivingEntity le ? le.getEyePos() : entity.getPos(), eyes, args);
            });

            case POSITION -> {
                VectorParser parser = new VectorParser(
                        args.get(1).toString(),
                        args.get(2).toString(),
                        args.get(3).toString(),
                        PlayerUtils.player()
                );
                snap(4, parser.getVector(), eyes, args);
            }
            case POLAR -> {
                PolarParser parser = new PolarParser(
                        args.get(1).toString(),
                        args.get(2).toString(),
                        PlayerUtils.player()
                );
                snap(3, eyes.add(parser.getVector()), eyes, args);
            }

            default -> throw new IllegalArgumentException("unsupported operation");
        }
    }

    private void snap(int zeroCursor, Vec3d dest, Vec3d camPos, ScriptArgs args) {
        Vec3d target = dest.subtract(camPos).normalize();
        CameraRotator.Goal goal = new CameraRotator.Goal(target);

        PlayerUtils.player().setPitch(goal.getPitch());
        PlayerUtils.player().setYaw(goal.getYaw());

        args.zeroCursor(zeroCursor);
        if (args.match(0, "then")) {
            args.executeAll(1);
        }
    }
}
