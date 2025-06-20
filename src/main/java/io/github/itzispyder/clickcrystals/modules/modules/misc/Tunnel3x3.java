package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.events.events.world.RenderWorldEvent;
import io.github.itzispyder.clickcrystals.gui.screens.ModuleEditScreen;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils3d;
import io.github.itzispyder.clickcrystals.util.misc.CameraRotator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Tunnel3x3 extends ListenerModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> reopenOnDisable = scGeneral.add(createBoolSetting()
            .name("reopen-on-disable")
            .description("Come back to this screen when module is disabled.")
            .def(true)
            .build()
    );

    public static final List<Block> BLACKLIST = List.of(
            Blocks.BEDROCK,
            Blocks.OBSIDIAN
    );

    private Direction dir;
    private BlockPos playerPos;
    private final List<BlockPos> targets = new ArrayList<>();
    private int index;

    public Tunnel3x3() {
        super("tunnel3x3", Categories.MISC, "Enable to tunnel a 3x3x5 tunnel in your direction");
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        targets.clear();

        if (PlayerUtils.invalid()) {
            this.setEnabled(false, false);
            return;
        }
        if (mc.currentScreen != null)
            mc.currentScreen.close();

        ClientPlayerEntity p = PlayerUtils.player();
        dir = p.getMovementDirection();
        playerPos = p.getBlockPos();
        index = 0;

        recalculateTargets();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        targets.clear();
        dir = null;
        playerPos = null;
        index = 0;

        if (mc != null && mc.options != null) {
            mc.options.attackKey.setPressed(false);
            if (reopenOnDisable.getVal() && mc.currentScreen == null) {
                mc.setScreen(new ModuleEditScreen(this));
            }
        }
    }

    @EventHandler
    private void onMouseClick(MouseClickEvent e) {
        if (e.getButton() == 0 && mc.currentScreen == null)
            e.setCancelled(true);
    }

    public void recalculateTargets() {
        targets.clear();

        int px = playerPos.getX();
        int py = playerPos.getY();
        int pz = playerPos.getZ();

        switch (dir) {
            case NORTH -> {
                for (int z = -1; z >= -5; z--) {
                    for (int y = 0; y <= 2; y++) {
                        for (int x = -1; x <= 1; x++) {
                            BlockPos target = new BlockPos(px + x, py + y, pz + z);
                            targets.add(target);
                        }
                    }
                }
            }
            case SOUTH -> {
                for (int z = 1; z <= 5; z++) {
                    for (int y = 0; y <= 2; y++) {
                        for (int x = -1; x <= 1; x++) {
                            BlockPos target = new BlockPos(px + x, py + y, pz + z);
                            targets.add(target);
                        }
                    }
                }
            }
            case EAST -> {
                for (int x = 1; x <= 5; x++) {
                    for (int y = 0; y <= 2; y++) {
                        for (int z = -1; z <= 1; z++) {
                            BlockPos target = new BlockPos(px + x, py + y, pz + z);
                            targets.add(target);
                        }
                    }
                }
            }
            case WEST -> {
                for (int x = -1; x >= -5; x--) {
                    for (int y = 0; y <= 2; y++) {
                        for (int z = -1; z <= 1; z++) {
                            BlockPos target = new BlockPos(px + x, py + y, pz + z);
                            targets.add(target);
                        }
                    }
                }
            }
        }

        for (int i = targets.size() - 1; i >= 0; i--)
            if (!validForBreaking(i))
                targets.remove(i);
    }

    private boolean validForBreaking(int targetIndex) {
        if (targetIndex < 0 || targetIndex >= targets.size())
            return false;
        if (PlayerUtils.getEyes().distanceTo(targets.get(targetIndex).toCenterPos()) > PlayerUtils.player().getBlockInteractionRange())
            return false;
        World w = PlayerUtils.getWorld();
        BlockPos pos = targets.get(targetIndex);
        BlockState block = w.getBlockState(pos);
        return block.isFullCube(w, pos) && !BLACKLIST.contains(block.getBlock());
    }

    @EventHandler
    public void render(RenderWorldEvent e) {
        for (int i = targets.size() - 1; i >= index + 1; i--)
            RenderUtils3d.renderBlock(e.getMatrices(), e.getOffsetPos(Vec3d.of(targets.get(i))), 0x10FFFFFF);
        RenderUtils3d.renderBlock(e.getMatrices(), e.getOffsetPos(Vec3d.of(targets.get(index))), 0x10FF2020);
    }

    @EventHandler
    private void onTick(ClientTickEndEvent e) {
        if (PlayerUtils.invalid() || index >= targets.size()) {
            this.setEnabled(false, true);
            return;
        }
        if (!PlayerUtils.player().getBlockPos().equals(playerPos)) {
            this.setEnabled(false, true);
            ChatUtils.sendPrefixMessage("You moved!");
            return;
        }
        if (CameraRotator.isCameraRunning())
            return;

        if (!(mc.crosshairTarget instanceof BlockHitResult result)) {
            target();
            return;
        }

        if (validForBreaking(index)) {
            mc.options.attackKey.setPressed(true);
            if (!targets.get(index).equals(result.getBlockPos()))
                target();
        }
        else {
            index++;
        }
    }

    private void target() {
        mc.options.attackKey.setPressed(false);
        Vec3d target = targets.get(index).toCenterPos().subtract(PlayerUtils.getEyes());
        CameraRotator.cancelCurrentRotator();
        CameraRotator.create()
                .addGoal(new CameraRotator.Goal(target))
                .enableCursorLock()
                .onFinish((pitch, yaw, rotator) -> mc.execute(() -> mc.options.attackKey.setPressed(true)))
                .build()
                .start();
    }
}
