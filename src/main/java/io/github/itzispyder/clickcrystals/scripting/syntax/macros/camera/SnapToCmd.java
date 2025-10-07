package io.github.itzispyder.clickcrystals.scripting.syntax.macros.camera;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgsReader;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.ScriptParser;
import io.github.itzispyder.clickcrystals.scripting.syntax.TargetType;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PolarParser;
import io.github.itzispyder.clickcrystals.util.minecraft.VectorParser;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

// @Format snap_to (nearest_entity|nearest_block) <identifier> then? {}?
// @Format snap_to (any_entity|target_entity|any_block) then? {}?
// @Format snap_to position <x> <y> <z> then? {}?
// @Format snap_to polar <pitch> <yaw> then? {}?

// @Format snap_to (nearest_entity|nearest_block) <identifier> aim <aim-anchor> then? {}?
// @Format snap_to (any_entity|target_entity|any_block) aim <aim-anchor> then? {}?
// @Format snap_to position <x> <y> <z> aim <aim-anchor> then? {}?
// @Format snap_to polar <pitch> <yaw> aim <aim-anchor> then? {}?
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
        var read = args.getReader();

        switch (read.next(TargetType.class)) {
            case NEAREST_BLOCK -> {
                Predicate<BlockState> filter = ScriptParser.parseBlockPredicate(read.nextStr());
                PlayerUtils.runOnNearestBlock(32, filter, (pos, state) -> snap(pos.toCenterPos(), eyes, args));
            }

            case NEAREST_ENTITY -> {
                Predicate<Entity> filter = ScriptParser.parseEntityPredicate(read.nextStr());
                PlayerUtils.runOnNearestEntity(128, filter, entity -> {
                    if (!(entity instanceof PlayerEntity) || !EntityUtils.isTeammate((PlayerEntity) entity))
                        snap(entity.getEntityPos(), eyes, args);
                });
            }

            case ANY_BLOCK -> PlayerUtils.runOnNearestBlock(32, (pos, state) -> true, (pos, state) -> snap(pos.toCenterPos(), eyes, args));
            case ANY_ENTITY -> PlayerUtils.runOnNearestEntity(128, Entity::isAlive, entity -> {
                if (!(entity instanceof PlayerEntity) || !EntityUtils.isTeammate(((PlayerEntity) entity)))
                    snap(entity.getEntityPos(), eyes, args);
            });

            case POSITION -> {
                VectorParser parser = new VectorParser(
                        read.nextStr(),
                        read.nextStr(),
                        read.nextStr(),
                        PlayerUtils.player()
                );
                snap(parser.getVector(), eyes, args);
            }
            case POLAR -> {
                PolarParser parser = new PolarParser(
                        read.nextStr(),
                        read.nextStr(),
                        PlayerUtils.player()
                );
                snap(eyes.add(parser.getVector()), eyes, args);
            }

            default -> throw new IllegalArgumentException("unsupported operation");
        }
    }

    private void snap(Vec3d dest, Vec3d camPos, ScriptArgs args) {
        TurnOptions options = new TurnOptions();
        ScriptArgsReader read = args.getReader();

        options.configure(read);

        Vec3d target = options.getCameraTicketPos(camPos, dest);
        float[] rot = MathUtils.toPolar(target.x, target.y, target.z);
        float pitch = (float) MathUtils.wrapDegrees(rot[0]);
        float yaw = (float) MathUtils.wrapDegrees(rot[1]);

        PlayerUtils.player().setPitch(pitch);
        PlayerUtils.player().setYaw(yaw);

        read.executeThenChain();
    }
}
