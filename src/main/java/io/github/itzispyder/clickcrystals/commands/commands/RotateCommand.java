package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.commands.arguments.PlayerArgumentType;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class RotateCommand extends Command {

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
                                            system.cameraRotator.ready()
                                                    .addTicket(pitch, yaw)
                                                    .lockCursor()
                                                    .openCurrentTicket();
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
                                    system.cameraRotator.ready()
                                            .addTicket(vec)
                                            .lockCursor()
                                            .openCurrentTicket();
                                    return SINGLE_SUCCESS;
                                })))
                .then(literal("cancel")
                        .executes(context -> {
                            if (!system.cameraRotator.isRunningTicket()) {
                                error("No active camera rotators!");
                            }
                            else {
                                system.cameraRotator.closeCurrentTicket();
                                info("Cancelled all jobs for current camera rotator!");
                            }
                            return SINGLE_SUCCESS;
                        }));
    }
}
