package io.github.itzispyder.clickcrystals.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.client.ClickCrystalsSystem;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;

import java.io.Serializable;

import static io.github.itzispyder.clickcrystals.ClickCrystals.starter;

/**
 * Represents a client command
 */
public abstract class Command implements ClientCommandRegistrationCallback, Serializable {

    protected static final int SINGLE_SUCCESS = 1, COMMAND_PASS = 0, COMMAND_FAIL = -1;
    protected static final MinecraftClient mc = ClickCrystals.mc;
    protected static final ClickCrystalsSystem system = ClickCrystals.system;
    private final String name, description, usage;
    private final String[] aliases;

    /**
     * Constructs a command
     * @param name command name
     * @param description command description
     * @param usage command usage
     * @param aliases command aliases
     */
    protected Command(String name, String description, String usage, String... aliases) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
    }

    /**
     * Constructs a command without aliases
     * @param name command name
     * @param description command description
     * @param usage command usage
     */
    protected Command(String name, String description, String usage) {
        this(name,description,usage, new String[0]);
    }

    /**
     * Build arguments from inheriting classes.
     * @param builder builder
     */
    public abstract void build(LiteralArgumentBuilder<FabricClientCommandSource> builder);

    /**
     * Helper method for brigadier
     * @param literal literal argument builder
     * @return argument builder
     */
    public LiteralArgumentBuilder<FabricClientCommandSource> literal(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    /**
     * Helper method for brigadier
     * @param name argument name
     * @param argumentType argument type
     * @returna argument builder
     * @param <T> argument of ?
     */
    public <T> RequiredArgumentBuilder<FabricClientCommandSource,T> argument(String name, ArgumentType<T> argumentType) {
        return RequiredArgumentBuilder.argument(name,argumentType);
    }

    /**
     * Register the command along with its aliases
     * @param dispatcher the command dispatcher to register commands to
     * @param registryAccess object exposing access to the game's registries
     */
    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        LiteralArgumentBuilder<FabricClientCommandSource> builder = literal(name);
        build(builder);
        dispatcher.register(builder);
        for (String alias : aliases) {
            builder = literal(alias);
            build(builder);
            dispatcher.register(builder);
        }
    }

    /**
     * Register to registration callback
     */
    public void registerThis() {
        ClientCommandRegistrationCallback.EVENT.register(this);
    }

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getUsage() {
        return usage;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Command help
     * @return help message
     */
    public String getHelp() {
        StringBuilder builder = new StringBuilder(
                " \n" + starter + "§f/" + name + "\n"
                + "§7" + description + "\n"
                + "§fUsage: §7" + usage +"\n"
                + "§fAliases:"
        );
        for (String alias : aliases) builder.append("\n§8 -§7 ").append(alias);
        return builder.append("\n ").toString();
    }
}
