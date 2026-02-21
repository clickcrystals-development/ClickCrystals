package io.github.itzispyder.clickcrystals.client.commands.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.commands.Command;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.misc.TeamDetector;
import net.minecraft.command.CommandSource;

public class TeamCommand extends Command {

    public TeamCommand() {
        super("team", "§7Manage your team list for TeamDetector module", ",team <add|remove|list|clear> [player]");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("add")
                .then(argument("player", StringArgumentType.string())
                        .executes(context -> {
                            String playerName = StringArgumentType.getString(context, "player");
                            TeamDetector detector = Module.get(TeamDetector.class);
                            String current = detector.playerNames.getVal();
                            
                            if (current.toLowerCase().contains(playerName.toLowerCase())) {
                                info("&c" + playerName + " is already in your team");
                            } else {
                                String updated = current.isEmpty() ? playerName : current + "," + playerName;
                                detector.playerNames.setVal(updated);
                                info("&a✓ Added " + playerName + " to team");
                            }
                            return SINGLE_SUCCESS;
                        })))
                .then(literal("remove")
                        .then(argument("player", StringArgumentType.string())
                                .executes(context -> {
                                    String playerName = StringArgumentType.getString(context, "player");
                                    TeamDetector detector = Module.get(TeamDetector.class);
                                    String current = detector.playerNames.getVal();
                                    
                                    if (!current.toLowerCase().contains(playerName.toLowerCase())) {
                                        info("&c" + playerName + " is not in your team");
                                    } else {
                                        String updated = current.replace(playerName + ",", "")
                                                               .replace("," + playerName, "")
                                                               .replace(playerName, "");
                                        detector.playerNames.setVal(updated);
                                        info("&c✖ Removed " + playerName + " from team");
                                    }
                                    return SINGLE_SUCCESS;
                                })))
                .then(literal("list")
                        .executes(context -> {
                            TeamDetector detector = Module.get(TeamDetector.class);
                            String names = detector.playerNames.getVal();
                            if (names.isEmpty()) {
                                info("&7Your team list is empty");
                            } else {
                                info("&bTeam members: &f" + names);
                            }
                            return SINGLE_SUCCESS;
                        }))
                .then(literal("clear")
                        .executes(context -> {
                            TeamDetector detector = Module.get(TeamDetector.class);
                            detector.playerNames.setVal("");
                            info("&7Cleared team list");
                            return SINGLE_SUCCESS;
                        }))
                .executes(context -> {
                    info("&7Usage: ,team <add|remove|list|clear> [player]");
                    return SINGLE_SUCCESS;
                });
    }
}