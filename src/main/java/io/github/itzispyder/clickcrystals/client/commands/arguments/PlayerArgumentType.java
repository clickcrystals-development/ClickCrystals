package io.github.itzispyder.clickcrystals.client.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class PlayerArgumentType implements ArgumentType<PlayerInfo> {

    public static final DynamicCommandExceptionType playerNotFound = new DynamicCommandExceptionType(name -> Component.literal("Player \"" + name + "\" not found!"));
    public static final List<String> examples = List.of("ImproperIssues", "obvWolf");

    public PlayerArgumentType() {

    }

    public static PlayerArgumentType create() {
        return new PlayerArgumentType();
    }

    @Override
    public PlayerInfo parse(StringReader reader) throws CommandSyntaxException {
        String string = reader.readString();
        PlayerInfo result = null;

        for (PlayerInfo entry : PlayerUtils.player().connection.getOnlinePlayers()) {
            if (string.equalsIgnoreCase(entry.getProfile().name())) {
                result = entry;
            }
        }

        if (result == null) throw playerNotFound.create(string);
        return result;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        List<String> list = PlayerUtils.player().connection.getOnlinePlayers().stream().map(entry -> entry.getProfile().name()).toList();
        return SharedSuggestionProvider.suggest(list, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return examples;
    }
}
