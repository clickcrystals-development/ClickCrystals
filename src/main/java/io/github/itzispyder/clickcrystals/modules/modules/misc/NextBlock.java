package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.misc.CameraRotator;
import io.github.itzispyder.clickcrystals.util.misc.Raytracer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class NextBlock extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    private final SettingSection scDetails = createSettingSection("details-and-precision");
    public final ModuleSetting<Boolean> verbose = scGeneral.add(createBoolSetting()
            .name("verbose")
            .description("Verbose for actions.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> shouldRaytrace = scDetails.add(createBoolSetting()
            .name("should-raytrace")
            .description("Should filter out blocks you cannot see.")
            .def(true)
            .build()
    );
    private Block lastTouched;
    private boolean wasAborted;

    public NextBlock() {
        super("next-block", Categories.MISC, "Goes to the next same block that you're mining.");
        lastTouched = null;
        wasAborted = false;
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
        lastTouched = null;
        wasAborted = false;
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
        lastTouched = null;
        wasAborted = false;
    }

    @EventHandler
    private void onAction(PacketSendEvent e) {
        if (e.getPacket() instanceof PlayerActionC2SPacket packet) {
            if (packet.getAction() == PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) {
                if (wasAborted) {
                    wasAborted = false;
                }
                else if (lastTouched != null) {
                    targetNextBlock();
                }
            }
            else if (packet.getAction() == PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK) {
                wasAborted = true;
            }
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

    private void setNextBlock() {
        wasAborted = false;
        ClientPlayerEntity p = PlayerUtils.player();
        World w = p.getWorld();

        if (mc.crosshairTarget instanceof BlockHitResult hit) {
            BlockState b = w.getBlockState(hit.getBlockPos());

            if (!b.isAir()) {
                lastTouched = b.getBlock();

                if (verbose.getVal()) {
                    Command.info("Set last touched block to " + lastTouched.getName().getString() + ".");
                }
            }
        }
    }

    private void targetNextBlock() {
        wasAborted = false;
        ClientPlayerEntity p = PlayerUtils.player();
        World w = p.getWorld();
        Box box = new Box(p.getBlockPos()).expand(5);
        double nearest = 10.0;
        List<String> iterated = new ArrayList<>();
        Vec3d target = null;

        if (verbose.getVal()) {
            Command.info("Looking for nearest " + lastTouched.getName().getString() + "...");
        }

        for (double x = box.minX; x <= box.maxX; x++) {
            for (double y = box.minY; y <= box.maxY; y++) {
                for (double z = box.minZ; z <= box.maxZ; z++) {
                    BlockPos pos = new BlockPos((int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z));
                    BlockState state = w.getBlockState(pos);

                    if (state == null || state.isAir()) {
                        continue;
                    }
                    iterated.add(state.getBlock().getName().getString());
                    Vec3d posVec = pos.toCenterPos();
                    double dist = posVec.distanceTo(p.getEyePos());

                    if (shouldRaytrace.getVal()) { // filter out blocks you cannot see
                        Vec3d dir = posVec.subtract(p.getEyePos()).normalize();
                        var hit = Raytracer.trace(w, p.getEyePos(), dir, dist, (blockPos, point) -> {
                            return w.getBlockState(blockPos).isFullCube(w, blockPos);
                        });

                        if (!hit.left.equals(pos)) {
                            continue;
                        }
                    }

                    if (state.isOf(lastTouched) && dist < nearest) {
                        target = posVec;
                        nearest = dist;
                    }
                }
            }
        }

        if (target == null) {
            if (verbose.getVal()) {
                Command.error("Cannot find " + lastTouched.getName().getString() + " withing 5 range distance.");
                Command.info("Searched in: §7" + iterated);
            }
            return;
        }

        if (verbose.getVal()) {
            Command.info("Found " + lastTouched.getName().getString() + " at " + target);
        }

        Vec3d rot = target.subtract(p.getEyePos()).normalize(); // slowly rotate towards target block smoothly
        CameraRotator.Builder builder = CameraRotator.create();

        if (verbose.getVal()) {
            builder.enableDebug();
        }

        builder.enableCursorLock();
        builder.addGoal(new CameraRotator.Goal(rot));
        builder.build().start();
    }
}
