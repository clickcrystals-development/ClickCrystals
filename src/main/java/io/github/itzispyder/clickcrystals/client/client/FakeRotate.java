package io.github.itzispyder.clickcrystals.client.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class FakeRotate {

    public static void tick(PlayerEntity player) {
        World world = player.getEntityWorld();
        if (world.isClient) {
            MinecraftClient client = MinecraftClient.getInstance();
            Vec3d rotation = player.getRotationVector();
            PlayerMoveC2SPacket packet = new PlayerMoveC2SPacket.LookAndOnGround((float) rotation.y, (float) rotation.x, player.isOnGround());
            Objects.requireNonNull(client.getNetworkHandler()).sendPacket(packet);
        }
    }
}