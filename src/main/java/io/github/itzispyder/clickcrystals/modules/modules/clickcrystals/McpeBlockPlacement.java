package io.github.itzispyder.clickcrystals.modules.modules.clickcrystals;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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

        ClientPlayerEntity client = PlayerUtils.player();
        for (Hand hand : Hand.values()) {
            ActionResult result = PlayerUtils.getInteractions().interactBlock(client, hand, hit);
            if (result instanceof ActionResult.Success success && success.swingSource() == ActionResult.SwingSource.CLIENT) {
                client.swingHand(hand);
                return;
            }
            if (result instanceof ActionResult.Fail)
                return;
        }
    }

    public BlockHitResult tryUpdate() {
        if (PlayerUtils.invalid())
            return null;
        if (mc.crosshairTarget == null || mc.crosshairTarget.getType() != HitResult.Type.MISS)
            return null;

        ClientPlayerEntity p = PlayerUtils.player();
        Direction dir = p.getMovementDirection();
        BlockPos foot = p.getBlockPos().add(0, -1, 0);
        BlockPos target = this.getDirectionalFootBlock(dir, p.getBlockPos());
        World world = p.getWorld();

        if (target.toCenterPos().distanceTo(foot.toCenterPos()) > 1.1) // can't place diagonally with a 10% error range
            return null;
        if (world.getBlockState(foot).isAir())
            target = foot;
        if (!p.isOnGround() || !world.getBlockState(target).isAir())
            return null;
        if (!raycastHit(p.getEyePos(), p.getRotationVecClient(), target, 2, 0.1))
            return null;

        return new BlockHitResult(target.toCenterPos(), dir.getOpposite(), target, false);
    }

    private boolean raycastHit(Vec3d start, Vec3d dir, BlockPos target, double len, double step) {
        Box box = Box.from(Vec3d.of(target));
        for (double i = 0; i <= len; i += step) {
            Vec3d pos = start.add(dir.multiply(i));
            if (box.contains(pos))
                return true;
        }
        return false;
    }

    private BlockPos getDirectionalFootBlock(Direction dir, BlockPos clientPos) {
        return BlockPos.ofFloored(clientPos.toCenterPos()
                .add(0, -1, 0)
                .add(dir.getDoubleVector()));
    }

    private void drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, World world, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos pos, BlockState state, int color) {
        VertexRendering.drawOutline(matrices, vertexConsumer, state.getOutlineShape(world, pos, ShapeContext.of(entity)), pos.getX() - cameraX, pos.getY() - cameraY, pos.getZ() - cameraZ, color);
    }
}
