package io.github.itzispyder.clickcrystals.util;

import com.mojang.authlib.GameProfile;
import io.github.itzispyder.clickcrystals.data.Delta3d;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.math.Vec3d;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public final class PlayerUtils {

    public static boolean playerNull() {
        return mc.player == null;
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

    public static void sendPacket(Packet<?> packet) {
        if (!playerNull()) {
            player().networkHandler.sendPacket(packet);
        }
    }

    public static Vec3d getRotation() {
        if (playerNull()) return new Vec3d(0, 0, 0);
        ClientPlayerEntity p = player();

        return Vec3d.fromPolar(p.getPitch(), p.getYaw());
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
}
