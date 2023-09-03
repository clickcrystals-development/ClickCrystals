package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.util.misc.CameraRotator;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;

public class RotateCommand extends Command {

    private CameraRotator rotator = null;

    public RotateCommand() {
        super("rotate", "Sets player rotation.", ",rotate <pitch> <yaw>");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("exact")
                        .then(argument("pitch", IntegerArgumentType.integer(-90, 90))
                                .then(argument("yaw", IntegerArgumentType.integer(0, 360))
                                        .executes(context -> {
                                            int pitch = context.getArgument("pitch", Integer.class);
                                            int yaw = context.getArgument("yaw", Integer.class);

                                            rotator = CameraRotator.create()
                                                    .enableDebug()
                                                    .addGoal(new CameraRotator.Goal(pitch, yaw))
                                                    .onFinish((p, y, wasCancelled) -> rotator = null)
                                                    .build();

                                            rotator.start();
                                            return SINGLE_SUCCESS;
                                        }))))
                .then(literal("to")
                        .then(argument("target", EntityArgumentType.entity())))
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
