package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.PlayerUtils;
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
        if (PlayerUtils.playerNull()) {
            return;
        }

        // ex.      turn_to nearest_entity :creeper on_finish say Yo
        Vec3d eyes = PlayerUtils.player().getEyePos();

        switch (args.get(0).enumValue(Mode.class, null)) {
            case NEAREST_BLOCK -> {
                Predicate<BlockState> filter = OnEventCmd.parseBlockPredicate(args.get(1).stringValue());
                PlayerUtils.runOnNearestBlock(16, filter, (pos, state) -> buildCam(pos.toCenterPos(), eyes, args));
            }
            case NEAREST_ENTITY -> {
                Predicate<Entity> filter = OnEventCmd.parseEntityPredicate(args.get(1).stringValue());
                PlayerUtils.runOnNearestEntity(16, filter, entity -> buildCam(entity instanceof LivingEntity le ? le.getEyePos() : entity.getPos(), eyes, args));
            }
        }
    }


    private void buildCam(Vec3d dest, Vec3d camPos, ScriptArgs args) {
        if (!CameraRotator.isCameraRunning()) {
            Vec3d target = dest.subtract(camPos).normalize();
            CameraRotator.Builder cam = CameraRotator.create();

            cam.enableCursorLock();
            cam.addGoal(new CameraRotator.Goal(target));

            if (args.getSize() >= 4) {
                switch (args.get(2).enumValue(EndActions.class, null)) {
                    case ON_FINISH -> cam.onFinish((pitch, yaw, cameraRotator) -> ClickScript.executeOneLine(args.getAll(3).stringValue()));
                }
            }
            cam.build().start();
        }
    }

    public enum EndActions {
        ON_FINISH
    }

    public enum Mode {
        NEAREST_ENTITY,
        NEAREST_BLOCK
    }
}
