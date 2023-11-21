package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.commands.arguments.PlayerArgumentType;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.misc.CameraRotator;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class RotateCommand extends Command {

    private CameraRotator rotator = null;

    public RotateCommand() {
        super("rotate", "Sets player rotation.", ",rotate <pitch> <yaw>");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("polar")
                        .then(argument("pitch", IntegerArgumentType.integer(-90, 90))
                                .then(argument("yaw", IntegerArgumentType.integer(-360, 360))
                                        .executes(context -> {
                                            int pitch = context.getArgument("pitch", Integer.class);
                                            int yaw = context.getArgument("yaw", Integer.class);

                                            rotator = CameraRotator.create()
                                                    .enableDebug()
                                                    .enableCursorLock()
                                                    .addGoal(new CameraRotator.Goal(pitch, yaw))
                                                    .onFinish((p, y, cameraRotator) -> rotator = null)
                                                    .build();

                                            rotator.start();
                                            return SINGLE_SUCCESS;
                                        }))))
                .then(literal("towards")
                        .then(argument("player", PlayerArgumentType.create())
                                .executes(context -> {
                                    PlayerListEntry entry = context.getArgument("player", PlayerListEntry.class);
                                    String name = entry.getProfile().getName();
                                    ClientPlayerEntity p = PlayerUtils.player();
                                    PlayerEntity target = null;

                                    for (PlayerEntity player : p.getWorld().getPlayers()) {
                                        if (player.getGameProfile().getName().equalsIgnoreCase(name)) {
                                            target = player;
                                            break;
                                        }
                                    }

                                    if (target == null) {
                                        error("Player not found.");
                                        return SINGLE_SUCCESS;
                                    }

                                    Vec3d vec = target.getPos().subtract(p.getEyePos()).normalize();
                                    rotator = CameraRotator.create()
                                            .enableDebug()
                                            .addGoal(new CameraRotator.Goal(vec))
                                            .onFinish((pitch, yaw, cameraRotator) -> rotator = null)
                                            .build();

                                    rotator.start();
                                    return SINGLE_SUCCESS;
                                })))
                .then(literal("skip")
                        .executes(context -> {
                            if (rotator == null) {
                                error("No active camera rotators!");
                            }
                            else {
                                rotator.skip();
                                info("Skipped current rotation goal!");
                            }
                            return SINGLE_SUCCESS;
                        }))
                .then(literal("cancel")
                        .executes(context -> {
                            if (rotator == null) {
                                error("No active camera rotators!");
                            }
                            else {
                                rotator.cancel();
                                info("Cancelled all jobs for current camera rotator!");
                                rotator = null;
                            }
                            return SINGLE_SUCCESS;
                        }));
    }
}
