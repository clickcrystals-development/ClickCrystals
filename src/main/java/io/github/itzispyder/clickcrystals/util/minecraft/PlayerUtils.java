package io.github.itzispyder.clickcrystals.util.minecraft;

import com.mojang.authlib.GameProfile;
import io.github.itzispyder.clickcrystals.Global;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAttachmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class PlayerUtils implements Global {

    public static boolean invalid() {
        return mc == null || mc.player == null || mc.world == null || mc.interactionManager == null || mc.options == null;
    }

    public static boolean valid() {
        return !invalid();
    }

    public static boolean playerValid(PlayerEntity player) {
        if (invalid() || player == null) {
            return false;
        }

        ClientPlayerEntity p = mc.player;
        GameProfile profile = player.getGameProfile();
        PlayerListEntry entry = p.networkHandler.getPlayerListEntry(profile.getId());

        return entry != null;
    }

    public static ClientPlayerEntity player() {
        return mc.player;
    }

    public static ClientPlayerInteractionManager getInteractions() {
        return mc.interactionManager;
    }

    public static World getWorld() {
        return player().getWorld();
    }

    public static Vec3d getPos() {
        return player().getPos();
    }

    public static Vec3d getEyes() {
        return player().getEyePos();
    }

    public static ClientPlayerInteractionManager getInteractionManager() {
        return mc.interactionManager;
    }

    public static float getEntityNameLabelHeight(Entity entity, float tickDelta) {
        float yaw = entity.getYaw(tickDelta);
        Vec3d vec = entity.getAttachments().getPointNullable(EntityAttachmentType.NAME_TAG, 0, yaw);
        return (float) (vec == null ? 0.5 : vec.y + 0.5);
    }

    public static void sendPacket(Packet<?> packet) {
        if (!invalid()) {
            player().networkHandler.sendPacket(packet);
        }
    }

    public static long getPing() {
        if (invalid()) {
            return -1;
        }

        GameProfile p = player().getGameProfile();
        PlayerListEntry entry = player().networkHandler.getPlayerListEntry(p.getId());

        if (entry == null) {
            return -1;
        }

        return entry.getLatency();
    }

    public static int getFps() {
        return mc.getCurrentFps();
    }

    public static boolean isMoving() {
        if (invalid())
            return false;

        ClientPlayerEntity p = player();
        return p.sidewaysSpeed != 0 || p.forwardSpeed != 0;
    }

    public static boolean isColliding() {
        if (invalid())
            return false;

        ClientPlayerEntity p = player();
        return p.horizontalCollision || p.verticalCollision;
    }

    public static boolean isCollidingHorizontally() {
        if (invalid())
            return false;

        ClientPlayerEntity p = player();
        return p.horizontalCollision;
    }

    public static boolean isCollidingVertically() {
        if (invalid())
            return false;

        ClientPlayerEntity p = player();
        return p.verticalCollision;
    }

    public static void boxIterator(World world, Box box, BiConsumer<BlockPos, BlockState> function) {
        for (double x = box.minX; x <= box.maxX; x++) {
            for (double y = box.minY; y <= box.maxY; y++) {
                for (double z = box.minZ; z <= box.maxZ; z++) {
                    BlockPos pos = new BlockPos((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
                    BlockState state = world.getBlockState(pos);

                    if (state == null || state.isAir()) {
                        continue;
                    }
                    function.accept(pos, state);
                }
            }
        }
    }

    public static Entity getNearestEntity(World world, Entity exclude, Vec3d at, double range, Predicate<Entity> filter) {
        List<Entity> candidates = world.getOtherEntities(exclude, Box.from(at).expand(range), filter).stream()
                .sorted(Comparator.comparing(entity -> entity.getPos().distanceTo(at)))
                .toList();

        if (candidates.isEmpty()) {
            return null;
        }
        return candidates.get(0);
    }

    public static boolean hasEffects() {
        return valid() && !player().getStatusEffects().isEmpty();
    }

    public static Entity getNearestEntity(double range, Predicate<Entity> filter) {
        if (invalid()) return null;
        return getNearestEntity(getWorld(), player(), player().getPos(), range, filter);
    }

    public static PlayerEntity getNearestPlayer(double range, Predicate<Entity> filter) {
        if (invalid()) return null;
        return (PlayerEntity)getNearestEntity(getWorld(), player(), player().getPos(), range, entity -> entity instanceof PlayerEntity && filter.test(entity));
    }

    public static boolean runOnNearestBlock(double range, BiPredicate<BlockPos, BlockState> filter, BiConsumer<BlockPos, BlockState> function) {
        if (invalid()) {
            return false;
        }

        AtomicReference<Double> nearestDist = new AtomicReference<>(64.0);
        AtomicReference<BlockPos> nearestPos = new AtomicReference<>();
        AtomicReference<BlockState> nearestState = new AtomicReference<>();
        Box box = player().getBoundingBox().expand(range);
        Vec3d player = player().getPos();
        World world = getWorld();

        PlayerUtils.boxIterator(world, box, (pos, state) -> {
            if (filter.test(pos, state) && pos.isWithinDistance(player, nearestDist.get())) {
                nearestDist.set(Math.sqrt(pos.getSquaredDistance(player)));
                nearestPos.set(pos);
                nearestState.set(state);
            }
        });

        if (nearestState.get() != null && nearestPos.get() != null) {
            function.accept(nearestPos.get(), nearestState.get());
            return true;
        }
        return false;
    }

    public static boolean runOnNearestBlock(double range, Predicate<BlockState> filter, BiConsumer<BlockPos, BlockState> function) {
        return runOnNearestBlock(range, (pos, state) -> filter.test(state), function);
    }

    public static boolean runOnNearestEntity(double range, Predicate<Entity> filter, Consumer<Entity> function) {
        if (invalid()) {
            return false;
        }

        Entity ent = getNearestEntity(range, filter);

        if (ent != null) {
            function.accept(ent);
            return true;
        }
        return false;
    }

    public static BlockPos getNearestBlock(double range, Predicate<BlockState> filter) {
        if (invalid()) {
            return null;
        }

        AtomicReference<Double> nearestDist = new AtomicReference<>(range);
        AtomicReference<BlockPos> nearestPos = new AtomicReference<>();
        Box box = player().getBoundingBox().expand(range);
        Vec3d playerPos = player().getPos();
        World world = getWorld();

        boxIterator(world, box, (pos, state) -> {
            if (filter.test(state) && pos.isWithinDistance(playerPos, nearestDist.get())) {
                double distance = Math.sqrt(pos.getSquaredDistance(playerPos));
                if (distance < nearestDist.get()) {
                    nearestDist.set(distance);
                    nearestPos.set(pos);
                }
            }
        });

        return nearestPos.get();
    }
}
