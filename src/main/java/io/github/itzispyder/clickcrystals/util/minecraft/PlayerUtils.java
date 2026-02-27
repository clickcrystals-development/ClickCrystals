package io.github.itzispyder.clickcrystals.util.minecraft;

import com.mojang.authlib.GameProfile;
import io.github.itzispyder.clickcrystals.Global;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.world.ClientWorld;
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
        PlayerListEntry entry = p.networkHandler.getPlayerListEntry(profile.id());

        return entry != null;
    }

    public static ClientPlayerEntity player() {
        return mc.player;
    }

    public static ClientPlayerInteractionManager getInteractions() {
        return mc.interactionManager;
    }

    public static World getWorld() {
        return player().getEntityWorld();
    }

    public static ClientWorld getClientWorld() {
        return mc.world;
    }

    public static Vec3d getPos() {
        return player().getEntityPos();
    }

    public static Vec3d getEyes() {
        return player().getEyePos();
    }

    public static Vec3d getDir() {
        return player().getRotationVector();
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
        PlayerListEntry entry = player().networkHandler.getPlayerListEntry(p.id());

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
        int minX = (int) Math.floor(box.minX);
        int minY = (int) Math.floor(box.minY);
        int minZ = (int) Math.floor(box.minZ);
        int maxX = (int) Math.floor(box.maxX);
        int maxY = (int) Math.floor(box.maxY);
        int maxZ = (int) Math.floor(box.maxZ);
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    pos.set(x, y, z);
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
                .sorted(Comparator.comparing(entity -> entity.getEntityPos().distanceTo(at)))
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
        return getNearestEntity(getWorld(), player(), player().getEntityPos(), range, filter);
    }

    public static PlayerEntity getNearestPlayer(double range, Predicate<Entity> filter) {
        if (invalid()) return null;
        return (PlayerEntity)getNearestEntity(getWorld(), player(), player().getEntityPos(), range, entity -> entity instanceof PlayerEntity && filter.test(entity));
    }

    public static boolean runOnNearestBlock(double range, BiPredicate<BlockPos, BlockState> filter, BiConsumer<BlockPos, BlockState> function) {
        if (invalid()) {
            return false;
        }

        Vec3d playerPos = player().getEntityPos();
        World world = getWorld();
        int centerX = (int) Math.floor(playerPos.x);
        int centerY = (int) Math.floor(playerPos.y);
        int centerZ = (int) Math.floor(playerPos.z);
        int maxRadius = (int) Math.ceil(range);
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        for (int radius = 0; radius <= maxRadius; radius++) {
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        if (Math.abs(x) != radius && Math.abs(y) != radius && Math.abs(z) != radius) {
                            continue;
                        }
                        
                        pos.set(centerX + x, centerY + y, centerZ + z);
                        if (pos.getSquaredDistance(playerPos) > range * range) {
                            continue;
                        }
                        
                        BlockState state = world.getBlockState(pos);
                        if (state == null || state.isAir()) {
                            continue;
                        }
                        
                        if (filter.test(pos, state)) {
                            function.accept(pos, state);
                            return true;
                        }
                    }
                }
            }
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

        Vec3d playerPos = player().getEntityPos();
        World world = getWorld();
        int centerX = (int) Math.floor(playerPos.x);
        int centerY = (int) Math.floor(playerPos.y);
        int centerZ = (int) Math.floor(playerPos.z);
        int maxRadius = (int) Math.ceil(range);
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        for (int radius = 0; radius <= maxRadius; radius++) {
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        if (Math.abs(x) != radius && Math.abs(y) != radius && Math.abs(z) != radius) {
                            continue;
                        }
                        
                        pos.set(centerX + x, centerY + y, centerZ + z);
                        if (pos.getSquaredDistance(playerPos) > range * range) {
                            continue;
                        }
                        
                        BlockState state = world.getBlockState(pos);
                        if (state == null || state.isAir()) {
                            continue;
                        }
                        
                        if (filter.test(state)) {
                            return pos.toImmutable();
                        }
                    }
                }
            }
        }
        return null;
    }
}
