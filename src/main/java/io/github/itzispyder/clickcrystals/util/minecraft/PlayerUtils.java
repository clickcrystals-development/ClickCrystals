package io.github.itzispyder.clickcrystals.util.minecraft;

import com.mojang.authlib.GameProfile;
import io.github.itzispyder.clickcrystals.Global;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class PlayerUtils implements Global {

    public static boolean invalid() {
        return mc == null || mc.player == null || mc.level == null || mc.gameMode == null || mc.options == null;
    }

    public static boolean valid() {
        return !invalid();
    }

    public static boolean playerValid(Player player) {
        if (invalid() || player == null) {
            return false;
        }

        LocalPlayer p = mc.player;
        GameProfile profile = player.getGameProfile();
        PlayerInfo entry = p.connection.getPlayerInfo(profile.id());

        return entry != null;
    }

    public static LocalPlayer player() {
        return mc.player;
    }

    public static MultiPlayerGameMode getInteractions() {
        return mc.gameMode;
    }

    public static Level getWorld() {
        return player().level();
    }

    public static ClientLevel getClientWorld() {
        return mc.level;
    }

    public static Vec3 getPos() {
        return player().position();
    }

    public static Vec3 getEyes() {
        return player().getEyePosition();
    }

    public static Vec3 getDir() {
        return player().getViewVector(1.0F);
    }

    public static MultiPlayerGameMode getInteractionManager() {
        return mc.gameMode;
    }

    public static float getEntityNameLabelHeight(Entity entity, float tickDelta) {
        Vec3 vec = entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getViewYRot(tickDelta));
        return (float) (vec == null ? 0.5 : vec.y + 0.5);
    }

    public static void sendPacket(Packet<?> packet) {
        if (!invalid()) {
            player().connection.send(packet);
        }
    }

    public static long getPing() {
        if (invalid()) {
            return -1;
        }

        GameProfile p = player().getGameProfile();
        PlayerInfo entry = player().connection.getPlayerInfo(p.id());

        if (entry == null) {
            return -1;
        }

        return entry.getLatency();
    }

    public static int getFps() {
        return mc.getFps();
    }

    public static boolean isMoving() {
        if (invalid())
            return false;

        LocalPlayer p = player();
        return p.xxa != 0 || p.zza != 0;
    }

    public static boolean isColliding() {
        if (invalid())
            return false;

        LocalPlayer p = player();
        return p.horizontalCollision || p.verticalCollision;
    }

    public static boolean isCollidingHorizontally() {
        if (invalid())
            return false;

        LocalPlayer p = player();
        return p.horizontalCollision;
    }

    public static boolean isCollidingVertically() {
        if (invalid())
            return false;

        LocalPlayer p = player();
        return p.verticalCollision;
    }

    public static void boxIterator(Level world, AABB box, BiConsumer<BlockPos, BlockState> function) {
        for (double x = box.minX; x <= box.maxX; x++) {
            for (double y = box.minY; y <= box.maxY; y++) {
                for (double z = box.minZ; z <= box.maxZ; z++) {
                    BlockPos pos = BlockPos.containing(x, y, z);
                    BlockState state = world.getBlockState(pos);

                    if (state == null || state.isAir()) {
                        continue;
                    }
                    function.accept(pos, state);
                }
            }
        }
    }

    public static Entity getNearestEntity(Level world, Entity exclude, Vec3 at, double range, Predicate<Entity> filter) {
        List<Entity> candidates = world.getEntities(exclude, new AABB(at, at).inflate(range), filter).stream()
                .sorted(Comparator.comparing(entity -> entity.position().distanceTo(at)))
                .toList();

        if (candidates.isEmpty()) {
            return null;
        }
        return candidates.get(0);
    }

    public static boolean hasEffects() {
        return valid() && !player().getActiveEffects().isEmpty();
    }

    public static Entity getNearestEntity(double range, Predicate<Entity> filter) {
        if (invalid()) return null;
        return getNearestEntity(getWorld(), player(), player().position(), range, filter);
    }

    public static Player getNearestPlayer(double range, Predicate<Entity> filter) {
        if (invalid()) return null;
        return (Player)getNearestEntity(getWorld(), player(), player().position(), range, entity -> entity instanceof Player && filter.test(entity));
    }

    public static boolean runOnNearestBlock(double range, BiPredicate<BlockPos, BlockState> filter, BiConsumer<BlockPos, BlockState> function) {
        if (invalid()) {
            return false;
        }

        AtomicReference<Double> nearestDist = new AtomicReference<>(64.0);
        AtomicReference<BlockPos> nearestPos = new AtomicReference<>();
        AtomicReference<BlockState> nearestState = new AtomicReference<>();
        AABB box = player().getBoundingBox().inflate(range);
        Vec3 player = player().position();
        Level world = getWorld();

        PlayerUtils.boxIterator(world, box, (pos, state) -> {
            if (filter.test(pos, state) && pos.distToCenterSqr(player) < nearestDist.get() * nearestDist.get()) {
                nearestDist.set(Math.sqrt(pos.distToCenterSqr(player)));
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
        AABB box = player().getBoundingBox().inflate(range);
        Vec3 playerPos = player().position();
        Level world = getWorld();

        boxIterator(world, box, (pos, state) -> {
            if (filter.test(state) && pos.distToCenterSqr(playerPos) < nearestDist.get() * nearestDist.get()) {
                double distance = Math.sqrt(pos.distToCenterSqr(playerPos));
                if (distance < nearestDist.get()) {
                    nearestDist.set(distance);
                    nearestPos.set(pos);
                }
            }
        });

        return nearestPos.get();
    }
}