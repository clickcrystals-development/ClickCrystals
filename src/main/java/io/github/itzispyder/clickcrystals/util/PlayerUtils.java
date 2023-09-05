package io.github.itzispyder.clickcrystals.util;

import com.mojang.authlib.GameProfile;
import io.github.itzispyder.clickcrystals.data.Delta3d;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public final class PlayerUtils {

    public static boolean playerNull() {
        return mc.player == null;
    }

    public static boolean playerNotNull() {
        return !playerNull();
    }

    public static boolean playerValid(PlayerEntity player) {
        if (playerNull() || player == null) return false;

        ClientPlayerEntity p = mc.player;
        GameProfile profile = player.getGameProfile();
        PlayerListEntry entry = p.networkHandler.getPlayerListEntry(profile.getId());

        return entry != null;
    }

    public static ClientPlayerEntity player() {
        return mc.player;
    }

    public static World getWorld() {
        return player().getWorld();
    }

    public static ClientPlayerInteractionManager getInteractionManager() {
        return mc.interactionManager;
    }

    public static void sendPacket(Packet<?> packet) {
        if (!playerNull()) {
            player().networkHandler.sendPacket(packet);
        }
    }

    public static long getPing() {
        if (playerNull()) {
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
        if (playerNull()) return false;
        ClientPlayerEntity p = player();

        return p.sidewaysSpeed != 0 || p.forwardSpeed != 0;
    }

    public static Delta3d toDelta3d(Vec3d del) {
        return new Delta3d(del.x, del.y, del.z);
    }

    public static Vec3d fromDelta3d(Delta3d del) {
        return new Vec3d(del.x(), del.y(), del.z());
    }

    public static void boxIterator(World world, Box box, BiConsumer<BlockPos, BlockState> function) {
        for (double x = box.minX; x <= box.maxX; x++) {
            for (double y = box.minY; y <= box.maxY; y++) {
                for (double z = box.minZ; z <= box.maxZ; z++) {
                    BlockPos pos = new BlockPos((int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z));
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

    public static Entity getNearestEntity(double range, Predicate<Entity> filter) {
        if (playerNull()) return null;
        return getNearestEntity(getWorld(), player(), player().getPos(), range, filter);
    }

    public static PlayerEntity getNearestPlayer(double range, Predicate<Entity> filter) {
        if (playerNull()) return null;
        return (PlayerEntity)getNearestEntity(getWorld(), player(), player().getPos(), range, entity -> entity instanceof PlayerEntity && filter.test(entity));
    }
}
