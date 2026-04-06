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
import io.github.itzispyder.clickcrystals.util.misc.Raytracer;
import io.github.itzispyder.clickcrystals.util.misc.camera.CameraFinalizerCallback;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

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
    private final CameraFinalizerCallback REPOSITION_TARGET = (pitch, yaw, cameraRotator) -> {
        system.scheduler.runDelayedTask(() -> {
            if (mc.hitResult == null || mc.hitResult.getType() == HitResult.Type.MISS) {
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
        if (e.getPacket() instanceof ServerboundPlayerActionPacket packet) {
            if (packet.getAction() == ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) {
                if (wasAborted) {
                    wasAborted = false;
                }
                if (lastTouched == null) {
                    setNextBlock();
                }
                targetNextBlock();
            }
            else if (packet.getAction() == ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK) {
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
        if (PlayerUtils.valid() && shouldRaytrace.getVal() && mc.options.keyAttack.isDown()) {
            if (!system.cameraRotator.isRunningTicket() && mc.hitResult instanceof BlockHitResult hit) {
                BlockState state = PlayerUtils.getWorld().getBlockState(hit.getBlockPos());
                if (!state.is(lastTouched)) {
                    targetNextBlock();
                }
            }
        }
    }

    private void setNextBlock() {
        wasAborted = false;
        LocalPlayer p = PlayerUtils.player();
        Level w = p.level();

        if (mc.hitResult instanceof BlockHitResult hit) {
            BlockState b = w.getBlockState(hit.getBlockPos());

            if (!b.isAir()) {
                lastTouched = b.getBlock();
                lastTouchedPosition = hit.getBlockPos();
            }
        }
    }

    private void targetNextBlock() {
        if (system.cameraRotator.isRunningTicket())
            return;

        wasAborted = false;
        LocalPlayer p = PlayerUtils.player();
        Vec3 target = null;

        if (lastTouchedPosition != null) {
            target = getNearestToTarget(lastTouchedPosition);
        }
        if (target == null) {
            if ((target = getNearestToPlayer(p)) == null) {
                return;
            }
        }

        Vec3 rot = target.subtract(p.getEyePosition()).normalize(); // slowly rotate towards target block smoothly
        system.cameraRotator.ready()
                .addTicket(rot)
                .lockCursor()
                .setFinishCallback(REPOSITION_TARGET)
                .openCurrentTicket();
    }

    public Vec3 getNearestToPlayer(LocalPlayer p) {
        Level w = p.level();
        AABB box = new AABB(p.blockPosition()).inflate(5);
        AtomicDouble nearest = new AtomicDouble(10.0);
        AtomicReference<Vec3> target = new AtomicReference<>(null);
        double max = p.blockInteractionRange();

        PlayerUtils.boxIterator(w, box, (pos, state) -> {
            Vec3 posVec = pos.getCenter();
            double dist = posVec.distanceTo(p.getEyePosition());

            if (dist > max) {
                return;
            }

            if (shouldRaytrace.getVal()) { // filter out blocks you cannot see
                Vec3 dir = posVec.subtract(p.getEyePosition()).normalize();
                var hit = Raytracer.trace(w, p.getEyePosition(), dir, dist, (blockPos, point) -> {
                    return w.getBlockState(blockPos).isCollisionShapeFullBlock(w, blockPos);
                });

                if (!hit.left.equals(pos)) {
                    return;
                }
            }

            if (state.is(lastTouched) && dist < nearest.get()) {
                target.set(posVec);
                nearest.set(dist);
            }
        });
        return target.get();
    }

    public Vec3 getNearestToTarget(BlockPos pos) {
        Vec3 start = pos.getCenter();
        LocalPlayer p = PlayerUtils.player();
        Level w = PlayerUtils.getWorld();
        AABB box = new AABB(pos).inflate(5);
        AtomicDouble nearest = new AtomicDouble(10.0);
        AtomicReference<Vec3> target = new AtomicReference<>(null);
        double max = PlayerUtils.player().blockInteractionRange();

        PlayerUtils.boxIterator(w, box, (blockPos, state) -> {
            Vec3 posVec = blockPos.getCenter();
            double dist = posVec.distanceTo(start);
            double reach = posVec.distanceTo(p.getEyePosition());

            if (reach > max) {
                return;
            }

            if (shouldRaytrace.getVal()) { // filter out blocks you cannot see
                Vec3 dir = posVec.subtract(p.getEyePosition()).normalize();
                var hit = Raytracer.trace(w, p.getEyePosition(), dir, dist, (block, point) -> {
                    return w.getBlockState(blockPos).isCollisionShapeFullBlock(w, blockPos);
                });

                if (!hit.left.equals(blockPos)) {
                    return;
                }
            }

            if (state.is(lastTouched) && dist < nearest.get()) {
                target.set(posVec);
                nearest.set(dist);
            }
        });
        return target.get();
    }
}
