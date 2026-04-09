package io.github.itzispyder.clickcrystals.client.commands.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.commands.Command;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class LookCommand extends Command {

    public LookCommand() {
        super("look", "Looks at a location.", ",look <mode> <value>");
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        builder.then(literal("to")
                .then(argument("x", IntegerArgumentType.integer())
                        .then(argument("y", IntegerArgumentType.integer())
                                .then(argument("z", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            int x = context.getArgument("x", Integer.class);
                                            int y = context.getArgument("y", Integer.class);
                                            int z = context.getArgument("z", Integer.class);
                                            rotateTo(x, y, z);
                                            return SINGLE_SUCCESS;
                                        })))))
                .then(literal("rot")
                        .then(argument("pitch", IntegerArgumentType.integer(-90, 90))
                                .then(argument("yaw", IntegerArgumentType.integer(-360, 360))
                                        .executes(context -> {
                                            int pitch = context.getArgument("pitch", Integer.class);
                                            int yaw = context.getArgument("yaw", Integer.class);
                                            rotateTo(pitch, yaw);
                                            return SINGLE_SUCCESS;
                                        }))))
                .then(literal("at")
                        .then(literal("nearest-player")
                                .executes(context -> {
                                    Player player = PlayerUtils.getNearestPlayer(128, Entity::isAlive);
                                    if (player != null) {
                                        info("Rotating to " + player.getGameProfile().name() + " from " + dist(player) + " blocks away");
                                        rotateTo(player);
                                        return SINGLE_SUCCESS;
                                    }
                                    error("No nearby players.");
                                    return SINGLE_SUCCESS;
                                }))
                        .then(literal("nearest-entity")
                                .executes(context -> {
                                    Entity entity = PlayerUtils.getNearestEntity(128, Entity::isAlive);
                                    if (entity != null) {
                                        info("Rotating to " + entity.getType().getDescription().getString() + " from " + dist(entity) + " blocks away");
                                        rotateTo(entity);
                                        return SINGLE_SUCCESS;
                                    }
                                    error("No nearby entities.");
                                    return SINGLE_SUCCESS;
                                })));
    }

    private void rotateTo(int x, int y, int z) {
        if (PlayerUtils.valid()) {
            BlockPos pos = new BlockPos(x, y, z);
            Vec3 dir = pos.getCenter().subtract(PlayerUtils.player().getEyePosition());

            system.cameraRotator.ready()
                    .addTicket(dir)
                    .lockCursor()
                    .openCurrentTicket();
        }
    }

    private void rotateTo(int pitch, int yaw) {
        if (PlayerUtils.valid()) {
            system.cameraRotator.ready()
                    .addTicket(pitch, yaw)
                    .lockCursor()
                    .openCurrentTicket();
        }
    }

    private void rotateTo(Entity ent) {
        rotateTo(ent.getBlockX(), ent.getBlockY(), ent.getBlockZ());
    }

    private int dist(Entity ent) {
        if (PlayerUtils.invalid() || ent == null) {
            return 0;
        }
        return (int)ent.position().distanceTo(PlayerUtils.player().position());
    }
}
