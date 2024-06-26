package io.github.itzispyder.clickcrystals.modules.scripts.macros;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptParser;
import io.github.itzispyder.clickcrystals.modules.scripts.TargetType;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.misc.CameraRotator;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

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

        switch (args.get(0).toEnum(TargetType.class, null)) {
            case NEAREST_BLOCK -> {
                Predicate<BlockState> filter = ScriptParser.parseBlockPredicate(args.get(1).toString());
                PlayerUtils.runOnNearestBlock(32, filter, (pos, state) -> specifiedTurn(pos.toCenterPos(), eyes, args));
            }
            case NEAREST_ENTITY -> {
                Predicate<Entity> filter = ScriptParser.parseEntityPredicate(args.get(1).toString());
                PlayerUtils.runOnNearestEntity(32, filter, entity -> specifiedTurn(entity instanceof LivingEntity le ? le.getEyePos() : entity.getPos(), eyes, args));
            }

            case ANY_BLOCK -> PlayerUtils.runOnNearestBlock(32, (pos, state) -> true, (pos, state) -> singleTurn(pos.toCenterPos(), eyes, args));
            case ANY_ENTITY -> PlayerUtils.runOnNearestEntity(32, Entity::isAlive, entity -> {
                singleTurn(entity instanceof LivingEntity le ? le.getEyePos() : entity.getPos(), eyes, args);
            });

            default -> throw new IllegalArgumentException("unsupported operation");
        }
    }

    private void singleTurn(Vec3d dest, Vec3d camPos, ScriptArgs args) {
        if (!CameraRotator.isCameraRunning()) {
            Vec3d target = dest.subtract(camPos).normalize();
            CameraRotator.Builder cam = CameraRotator.create();

            cam.enableCursorLock();
            cam.addGoal(new CameraRotator.Goal(target));

            if (args.match(1, "then")) {
                cam.onFinish((pitch, yaw, cameraRotator) -> args.executeAll(2));
            }
            cam.build().start();
        }
    }

    private void specifiedTurn(Vec3d dest, Vec3d camPos, ScriptArgs args) {
        if (!CameraRotator.isCameraRunning()) {
            Vec3d target = dest.subtract(camPos).normalize();
            CameraRotator.Builder cam = CameraRotator.create();

            cam.enableCursorLock();
            cam.addGoal(new CameraRotator.Goal(target));

            if (args.match(2, "then")) {
                cam.onFinish((pitch, yaw, cameraRotator) -> args.executeAll(3));
            }
            cam.build().start();
        }
    }
}
