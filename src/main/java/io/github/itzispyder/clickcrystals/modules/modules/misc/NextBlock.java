package io.github.itzispyder.clickcrystals.modules.modules.misc;

import com.google.common.util.concurrent.AtomicDouble;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.events.events.world.BlockBreakEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickStartEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.misc.CameraRotator;
import io.github.itzispyder.clickcrystals.util.misc.Raytracer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.concurrent.atomic.AtomicReference;

public class NextBlock extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    private final SettingSection scDetails = createSettingSection("details-and-precision");
    public final ModuleSetting<Boolean> shouldRaytrace = scDetails.add(createBoolSetting()
            .name("should-raytrace")
            .description("Should filter out blocks you cannot see.")
            .def(true)
            .build()
    );
    private Block lastTouched;
    private BlockPos lastTouchedPosition;
    private boolean wasAborted;
    private final CameraRotator.EndAction REPOSITION_TARGET = (pitch, yaw, cameraRotator) -> {
        system.scheduler.runDelayedTask(() -> {
            if (mc.crosshairTarget == null || mc.crosshairTarget.getType() == HitResult.Type.MISS) {
                targetNextBlock();
            }
        }, 3 * 50);
    };

    public NextBlock() {
        super("next-block", Categories.MISC, "Targets next same block that you're mining. (for farming, not pvp, useless in pvp)");
        lastTouched = null;
        lastTouchedPosition = null;
        wasAborted = false;
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
        lastTouched = null;
        lastTouchedPosition = null;
        wasAborted = false;
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
        lastTouched = null;
        lastTouchedPosition = null;
        wasAborted = false;
    }

    @EventHandler
    private void onAction(PacketSendEvent e) {
        if (e.getPacket() instanceof PlayerActionC2SPacket packet) {
            if (packet.getAction() == PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) {
                if (wasAborted) {
                    wasAborted = false;
                }
                if (lastTouched == null) {
                    setNextBlock();
                }
                targetNextBlock();
            }
            else if (packet.getAction() == PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK) {
                wasAborted = true;
            }
        }
    }

    @EventHandler
    private void onBreak(BlockBreakEvent e) {
        if (lastTouched != null) {
            targetNextBlock();
        }
    }

    @EventHandler
    private void onMouse(MouseClickEvent e) {
        if (e.getButton() == 0 && e.isScreenNull()) {
            if (e.getAction().isRelease()) {
                if (!wasAborted && lastTouched != null) {
                    targetNextBlock();
                }
            }
            if (e.getAction().isDown()) {
                setNextBlock();
            }
        }
    }

    @EventHandler
    private void onTick(ClientTickStartEvent e) {
        if (PlayerUtils.valid() && shouldRaytrace.getVal() && mc.options.attackKey.isPressed()) {
            if (!CameraRotator.isCameraRunning() && mc.crosshairTarget instanceof BlockHitResult hit) {
                BlockState state = PlayerUtils.getWorld().getBlockState(hit.getBlockPos());
                if (!state.isOf(lastTouched)) {
                    targetNextBlock();
                }
            }
        }
    }

    private void setNextBlock() {
        wasAborted = false;
        ClientPlayerEntity p = PlayerUtils.player();
        World w = p.getWorld();

        if (mc.crosshairTarget instanceof BlockHitResult hit) {
            BlockState b = w.getBlockState(hit.getBlockPos());

            if (!b.isAir()) {
                lastTouched = b.getBlock();
                lastTouchedPosition = hit.getBlockPos();
            }
        }
    }

    private void targetNextBlock() {
        wasAborted = false;
        ClientPlayerEntity p = PlayerUtils.player();
        Vec3d target = null;

        if (lastTouchedPosition != null) {
            target = getNearestToTarget(lastTouchedPosition);
        }
        if (target == null) {
            if ((target = getNearestToPlayer(p)) == null) {
                return;
            }
        }

        Vec3d rot = target.subtract(p.getEyePos()).normalize(); // slowly rotate towards target block smoothly
        CameraRotator.Builder builder = CameraRotator.create();
        builder.enableCursorLock();
        builder.addGoal(new CameraRotator.Goal(rot));
        builder.onFinish(REPOSITION_TARGET);
        builder.build().start();
    }

    public Vec3d getNearestToPlayer(ClientPlayerEntity p) {
        World w = p.getWorld();
        Box box = new Box(p.getBlockPos()).expand(5);
        AtomicDouble nearest = new AtomicDouble(10.0);
        AtomicReference<Vec3d> target = new AtomicReference<>(null);
        double max = p.getBlockInteractionRange();

        PlayerUtils.boxIterator(w, box, (pos, state) -> {
            Vec3d posVec = pos.toCenterPos();
            double dist = posVec.distanceTo(p.getEyePos());

            if (dist > max) {
                return;
            }

            if (shouldRaytrace.getVal()) { // filter out blocks you cannot see
                Vec3d dir = posVec.subtract(p.getEyePos()).normalize();
                var hit = Raytracer.trace(w, p.getEyePos(), dir, dist, (blockPos, point) -> {
                    return w.getBlockState(blockPos).isFullCube(w, blockPos);
                });

                if (!hit.left.equals(pos)) {
                    return;
                }
            }

            if (state.isOf(lastTouched) && dist < nearest.get()) {
                target.set(posVec);
                nearest.set(dist);
            }
        });
        return target.get();
    }

    public Vec3d getNearestToTarget(BlockPos pos) {
        Vec3d start = pos.toCenterPos();
        ClientPlayerEntity p = PlayerUtils.player();
        World w = PlayerUtils.getWorld();
        Box box = new Box(pos).expand(5);
        AtomicDouble nearest = new AtomicDouble(10.0);
        AtomicReference<Vec3d> target = new AtomicReference<>(null);
        double max = PlayerUtils.player().getBlockInteractionRange();

        PlayerUtils.boxIterator(w, box, (blockPos, state) -> {
            Vec3d posVec = blockPos.toCenterPos();
            double dist = posVec.distanceTo(start);
            double reach = posVec.distanceTo(p.getEyePos());

            if (reach > max) {
                return;
            }

            if (shouldRaytrace.getVal()) { // filter out blocks you cannot see
                Vec3d dir = posVec.subtract(p.getEyePos()).normalize();
                var hit = Raytracer.trace(w, p.getEyePos(), dir, dist, (block, point) -> {
                    return w.getBlockState(blockPos).isFullCube(w, blockPos);
                });

                if (!hit.left.equals(blockPos)) {
                    return;
                }
            }

            if (state.isOf(lastTouched) && dist < nearest.get()) {
                target.set(posVec);
                nearest.set(dist);
            }
        });
        return target.get();
    }
}
