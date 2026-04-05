package io.github.itzispyder.clickcrystals.client.commands.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.commands.Command;
import io.github.itzispyder.clickcrystals.client.commands.arguments.PlayerArgumentType;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class RotateCommand extends Command {

    public RotateCommand() {
        super("rotate", "Sets player rotation.", ",rotate <pitch> <yaw>");
    }

    @Override
    public void build(LiteralArgumentBuilder<SharedSuggestionProvider> builder) {
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
                                    PlayerInfo entry = context.getArgument("player", PlayerInfo.class);
                                    String name = entry.getProfile().name();
                                    LocalPlayer p = PlayerUtils.player();
                                    Player target = null;

                                    for (Player player : p.level().players()) {
                                        if (player.getGameProfile().name().equalsIgnoreCase(name)) {
                                            target = player;
                                            break;
                                        }
                                    }

                                    if (target == null) {
                                        error("Player not found.");
                                        return SINGLE_SUCCESS;
                                    }

                                    Vec3 vec = target.position().subtract(p.getEyePosition()).normalize();
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
