package io.github.itzispyder.clickcrystals.modules.modules.clickcrystals;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.modrinth.ModrinthNoNo;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

@ModrinthNoNo
public class McpeBlockPlacement extends ListenerModule {

    public McpeBlockPlacement() {
        super("mcpe-block-placement", Categories.CLIENT, "Place blocks on the group in front of you even with no support blocks");
    }

    @EventHandler
    public void onMouseClick(MouseClickEvent e) {
        if (!e.getAction().isDown() || e.getButton() != 1 || !e.isScreenNull())
            return;
        BlockHitResult hit = tryUpdate();
        if (hit == null)
            return;

        e.cancel();

        LocalPlayer client = PlayerUtils.player();
        for (InteractionHand hand : InteractionHand.values()) {
            InteractionResult result = PlayerUtils.getInteractions().useItemOn(client, hand, hit);
            if (result instanceof InteractionResult.Success success && success.swingSource() == InteractionResult.SwingSource.CLIENT) {
                client.swing(hand);
                return;
            }
            if (result instanceof InteractionResult.Fail)
                return;
        }
    }

    public BlockHitResult tryUpdate() {
        if (PlayerUtils.invalid())
            return null;
        if (mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.MISS)
            return null;

        LocalPlayer p = PlayerUtils.player();
        Direction dir = p.getMotionDirection();
        BlockPos foot = p.blockPosition().offset(0, -1, 0);
        BlockPos target = this.getDirectionalFootBlock(dir, p.blockPosition());
        Level world = p.level();

        if (target.getCenter().distanceTo(foot.getCenter()) > 1.1) // can't place diagonally with a 10% error range
            return null;
        if (world.getBlockState(foot).isAir())
            target = foot;
        if (!p.onGround() || !world.getBlockState(target).isAir())
            return null;
        if (!raycastHit(p.getEyePosition(), p.getForward(), target, 2, 0.1))
            return null;

        return new BlockHitResult(target.getCenter(), dir.getOpposite(), target, false);
    }

    private boolean raycastHit(Vec3 start, Vec3 dir, BlockPos target, double len, double step) {
        AABB box = AABB.unitCubeFromLowerCorner(Vec3.atLowerCornerOf(target));
        for (double i = 0; i <= len; i += step) {
            Vec3 pos = start.add(dir.scale(i));
            if (box.contains(pos))
                return true;
        }
        return false;
    }

    private BlockPos getDirectionalFootBlock(Direction dir, BlockPos clientPos) {
        return BlockPos.containing(clientPos.getCenter()
                .add(0, -1, 0)
                .add(dir.getUnitVec3()));
    }
}
