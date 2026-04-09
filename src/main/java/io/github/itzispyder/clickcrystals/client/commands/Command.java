package io.github.itzispyder.clickcrystals.client.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.Commands;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.server.permissions.PermissionSet;

public abstract class Command implements Global {

    protected static final int COMMAND_FAIL = -1;
    protected static final int COMMAND_PASS = 0;
    protected static final int SINGLE_SUCCESS = 1;
    public static final HolderLookup.Provider WRAPPER = VanillaRegistries.createLookup();
    public static final CommandBuildContext REGISTRY = Commands.createValidationContext(WRAPPER);
    public static final CommandDispatcher<ClientSuggestionProvider> DISPATCHER = new CommandDispatcher<>();
    public static final ClientSuggestionProvider SOURCE = new ClientSuggestionProvider(null, mc, PermissionSet.NO_PERMISSIONS);

    private final String name, description, usage;
    private final String[] aliases;

    protected Command(String name, String description, String usage, String... aliases) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public String[] getAliases() {
        return aliases;
    }

    public abstract void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder);

    public void register() {
        registerToDispatcher(name);
        for (String alias : aliases) {
            registerToDispatcher(alias);
        }
    }

    public void registerToDispatcher(String name) {
        LiteralArgumentBuilder<ClientSuggestionProvider> builder = literal(name);
        build(builder);
        DISPATCHER.register(builder);
    }

    protected static LiteralArgumentBuilder<ClientSuggestionProvider> literal(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    protected static <T> RequiredArgumentBuilder<ClientSuggestionProvider, T> argument(String name, ArgumentType<T> argument) {
        return RequiredArgumentBuilder.argument(name, argument);
    }

    public static void dispatch(String line) throws CommandSyntaxException {
        ParseResults<ClientSuggestionProvider> results = DISPATCHER.parse(line, SOURCE);
        DISPATCHER.execute(results);
    }

    public static void error(String msg) {
        ChatUtils.sendPrefixMessage(StringUtils.color("&c" + msg));
    }

    public static void warning(String msg) {
        ChatUtils.sendPrefixMessage(StringUtils.color("&e" + msg));
    }

    public static void info(String msg) {
        ChatUtils.sendPrefixMessage(StringUtils.color(msg));
    }

    public static void infoRaw(String msg) {
        ChatUtils.sendMessage(StringUtils.color(msg));
    }

    public static void help(Command cmd) {
        info(cmd.getHelp());
    }

    public String getHelp() {
        String prefix = ClickCrystals.commandPrefix.getKeyName();

        StringBuilder builder = new StringBuilder("""
                
        %s§f%s%s
        §7%s
        §fUsage: §7%s
        §fAliases:
        """.formatted(starter, prefix, name, description, usage));

        for (String alias : aliases) {
            builder.append("§8 -§7%s\n ".formatted(alias));
        }
        return builder.toString();
    }
}
