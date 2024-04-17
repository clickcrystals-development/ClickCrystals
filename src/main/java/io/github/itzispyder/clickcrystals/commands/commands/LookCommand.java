package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.misc.CameraRotator;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class LookCommand extends Command {

    private final CameraRotator cameraRotator = CameraRotator.create().build();

    public LookCommand() {
        super("look", "Looks at a location.", ",look <mode> <value>");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
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
                                    PlayerEntity player = PlayerUtils.getNearestPlayer(128, Entity::isAlive);
                                    if (player != null) {
                                        info("Rotating to " + player.getGameProfile().getName() + " from " + dist(player) + " blocks away");
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
                                        info("Rotating to " + entity.getType().getName().getString() + " from " + dist(entity) + " blocks away");
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
            Vec3d dir = pos.toCenterPos().subtract(PlayerUtils.player().getEyePos());

            cameraRotator.clearGoals();
            cameraRotator.addGoal(new CameraRotator.Goal(dir));
            cameraRotator.start();
        }
    }

    private void rotateTo(int pitch, int yaw) {
        if (PlayerUtils.valid()) {
            cameraRotator.clearGoals();
            cameraRotator.addGoal(new CameraRotator.Goal(pitch, yaw));
            cameraRotator.start();
        }
    }

    private void rotateTo(Entity ent) {
        rotateTo(ent.getBlockX(), ent.getBlockY(), ent.getBlockZ());
    }

    private int dist(Entity ent) {
        if (PlayerUtils.invalid() || ent == null) {
            return 0;
        }
        return (int)ent.getPos().distanceTo(PlayerUtils.player().getPos());
    }
}
