package io.github.itzispyder.clickcrystals.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.itzispyder.clickcrystals.client.ClickCrystalsSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.command.CommandManager;

public abstract class CustomCommand {

    protected static final int COMMAND_FAIL = -1;
    protected static final int COMMAND_PASS = 0;
    protected static final int SINGLE_SUCCESS = 1;
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    protected static final ClickCrystalsSystem system = ClickCrystalsSystem.getInstance();
    public static final RegistryWrapper.WrapperLookup WRAPPER = BuiltinRegistries.createWrapperLookup();
    public static final CommandRegistryAccess REGISTRY = CommandManager.createRegistryAccess(WRAPPER);
    public static final CommandDispatcher<CommandSource> DISPATCHER = new CommandDispatcher<>();
    public static final CommandSource SOURCE = new ClientCommandSource(null, mc);

    private final String name, description, usage;
    private final String[] aliases;

    protected CustomCommand(String name, String description, String usage, String... aliases) {
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

    public abstract void build(LiteralArgumentBuilder<CommandSource> builder);

    public void register() {
        registerToDispatcher(name);
        for (String alias : aliases) {
            registerToDispatcher(alias);
        }
    }

    public void registerToDispatcher(String name) {
        LiteralArgumentBuilder<CommandSource> builder = literal(name);
        build(builder);
        DISPATCHER.register(builder);
    }

    protected static LiteralArgumentBuilder<CommandSource> literal(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    protected static <T> RequiredArgumentBuilder<CommandSource, T> argument(String name, ArgumentType<T> argument) {
        return RequiredArgumentBuilder.argument(name, argument);
    }

    public static void dispatch(String line) throws CommandSyntaxException {
        ParseResults<CommandSource> results = DISPATCHER.parse(line, SOURCE);
        DISPATCHER.execute(results);
    }
}
