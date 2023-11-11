package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.misc.CameraRotator;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.concurrent.atomic.AtomicReference;
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
        World world = PlayerUtils.getWorld();
        Box box = PlayerUtils.player().getBoundingBox().expand(16);
        Vec3d player = PlayerUtils.player().getPos();
        Vec3d eyes = PlayerUtils.player().getEyePos();

        switch (args.get(0).enumValue(Mode.class, null)) {
            case NEAREST_BLOCK -> {
                Predicate<BlockState> filter = OnEventCmd.parseBlockPredicate(args.get(1).stringValue());
                AtomicReference<Double> nearestDist = new AtomicReference<>(64.0);
                AtomicReference<BlockPos> nearestBlock = new AtomicReference<>();

                PlayerUtils.boxIterator(world, box, (pos, state) -> {
                    if (filter.test(state) && pos.isWithinDistance(player, nearestDist.get())) {
                        nearestDist.set(Math.sqrt(pos.getSquaredDistance(player)));
                        nearestBlock.set(pos);
                    }
                });

                if (nearestBlock.get() != null) {
                    buildCam(nearestBlock.get().toCenterPos(), eyes, args);
                }
            }
            case NEAREST_ENTITY -> {
                Predicate<Entity> filter = OnEventCmd.parseEntityPredicate(args.get(1).stringValue());
                Entity entity = PlayerUtils.getNearestEntity(16, filter);

                if (entity != null) {
                    buildCam(entity instanceof LivingEntity le ? le.getEyePos() : entity.getPos(), eyes, args);
                }
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
