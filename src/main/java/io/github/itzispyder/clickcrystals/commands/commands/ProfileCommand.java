package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.commands.arguments.ProfileArgumentType;
import io.github.itzispyder.clickcrystals.gui.screens.profiles.ProfilesScreen;
import io.github.itzispyder.clickcrystals.util.ArrayUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import net.minecraft.command.CommandSource;

import java.util.Arrays;
import java.util.List;

public class ProfileCommand extends Command {

    public ProfileCommand() {
        super("profile", "Manages config profiles for the client.", ",profile [default|switch|delete|list|create]");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(cxt -> {
                    system.scheduler.runDelayedTask(() -> {
                        mc.execute(() -> mc.setScreen(new ProfilesScreen()));
                    }, 5 * 50);
                    return SINGLE_SUCCESS;
                })
                .then(literal("default")
                        .executes(cxt -> {
                            info("Switching to profile 'main-config' ...");
                            system.profiles.switchDefaultProfile();
                            info("Profile set 'Main Config'");
                            return SINGLE_SUCCESS;
                        }))
                .then(literal("list")
                        .executes(cxt -> {
                            List<String> profiles = Arrays.asList(system.profiles.getCustomProfiles());

                            ChatUtils.sendBlank(2);
                            ChatUtils.sendPrefixMessage("All Custom Profiles:");
                            ChatUtils.sendBlank(1);
                            ChatUtils.sendMessage("Current profile: §e" + system.profiles.profileConfig.getCurrentProfileName());
                            ChatUtils.sendMessage("Config profiles (" + profiles.size() + "): " + ArrayUtils.list2string(profiles));
                            ChatUtils.sendBlank(2);
                            return SINGLE_SUCCESS;
                        }))
                .then(literal("delete")
                        .then(argument("name", ProfileArgumentType.create())
                                .executes(cxt -> {
                                    String name = cxt.getArgument("name", String.class);
                                    info("Attempting to delete profile '%s' ...".formatted(name));
                                    system.profiles.deleteProfile(name);
                                    return SINGLE_SUCCESS;
                                })))
                .then(literal("switch")
                        .then(argument("name", ProfileArgumentType.create())
                                .executes(cxt -> {
                                    String name = cxt.getArgument("name", String.class);
                                    info("Switching to profile '%s' ...".formatted(name));
                                    system.profiles.switchProfile(name);
                                    info("Profile set '%s'".formatted(system.profiles.profileConfig.getCurrentProfileName()));
                                    return SINGLE_SUCCESS;
                                })))
                .then(literal("create")
                        .then(argument("name", StringArgumentType.string())
                                .executes(cxt -> {
                                    String name = cxt.getArgument("name", String.class);
                                    info("Creating and switching to new profile '%s' ...".formatted(name));
                                    system.profiles.switchProfile(name);
                                    info("Profile set '%s'".formatted(system.profiles.profileConfig.getCurrentProfileName()));
                                    return SINGLE_SUCCESS;
                                })));
    }
}
