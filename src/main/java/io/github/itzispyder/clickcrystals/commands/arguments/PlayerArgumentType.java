package io.github.itzispyder.clickcrystals.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlayerArgumentType implements ArgumentType<PlayerListEntry> {

    public static final DynamicCommandExceptionType playerNotFound = new DynamicCommandExceptionType(name -> Text.literal("Player \"" + name + "\" not found!"));
    public static final List<String> examples = List.of("ImproperIssues", "obvWolf");

    public PlayerArgumentType() {

    }

    public static PlayerArgumentType create() {
        return new PlayerArgumentType();
    }

    @Override
    public PlayerListEntry parse(StringReader reader) throws CommandSyntaxException {
        String string = reader.readString();
        PlayerListEntry result = null;

        for (PlayerListEntry entry : PlayerUtils.player().networkHandler.getPlayerList()) {
            if (string.equalsIgnoreCase(entry.getProfile().getName())) {
                result = entry;
            }
        }

        if (result == null) throw playerNotFound.create(string);
        return result;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        List<String> list = PlayerUtils.player().networkHandler.getPlayerList().stream().map(entry -> entry.getProfile().getName()).toList();
        return CommandSource.suggestMatching(list, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return examples;
    }
}
