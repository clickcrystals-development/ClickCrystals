package io.github.itzispyder.clickcrystals.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.events.EventBus;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;

import static io.github.itzispyder.clickcrystals.ClickCrystals.starter;
import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

/**
 * Represents a client command
 */
public abstract class Command implements ClientCommandRegistrationCallback {

    protected static final int SINGLE_SUCCESS = 1, COMMAND_PASS = 0, COMMAND_FAIL = -1;
    protected static final MinecraftClient mc = ClickCrystals.mc;
    protected static final EventBus eventBus = system.eventBus;
    private final String name, description, usage;
    private final String[] aliases;

    protected Command(String name, String description, String usage, String... aliases) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
    }

    protected Command(String name, String description, String usage) {
        this(name,description,usage, new String[0]);
    }

    /**
     * Build arguments from inheriting classes.
     * @param builder builder
     */
    public abstract void build(LiteralArgumentBuilder<FabricClientCommandSource> builder);

    public LiteralArgumentBuilder<FabricClientCommandSource> literal(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    public <T extends ArgumentType<?>> RequiredArgumentBuilder<FabricClientCommandSource,T> argument(String name, ArgumentType<T> argumentType) {
        return RequiredArgumentBuilder.argument(name,argumentType);
    }

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
