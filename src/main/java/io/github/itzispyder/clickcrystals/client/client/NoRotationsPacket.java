package io.github.itzispyder.clickcrystals.client.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class NoRotationsPacket {

    public static void tick(PlayerEntity player) {
        World world = player.getEntityWorld();
        if (world.isClient) {
            MinecraftClient client = MinecraftClient.getInstance();
            Vec3d lookVector = player.getRotationVector();
            double xzDistance = MathHelper.sqrt((float) (lookVector.x * lookVector.x + lookVector.z * lookVector.z));
            float yaw = (float) (Math.atan2(lookVector.z, lookVector.x) * 180.0D / Math.PI);
            float pitch = (float) (Math.atan2(lookVector.y, xzDistance) * 180.0D / Math.PI);
            PlayerMoveC2SPacket packet = new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, player.isOnGround());
            Objects.requireNonNull(client.getNetworkHandler()).sendPacket(packet);
        }
    }
}
