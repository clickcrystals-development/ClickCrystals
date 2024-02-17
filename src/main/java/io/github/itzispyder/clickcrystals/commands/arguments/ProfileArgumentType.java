package io.github.itzispyder.clickcrystals.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.itzispyder.clickcrystals.Global;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ProfileArgumentType implements Global, ArgumentType<String> {

    public static final DynamicCommandExceptionType profileNotFound = new DynamicCommandExceptionType(name -> Text.literal("Profile not found: " + name));

    public static ProfileArgumentType create() {
        return new ProfileArgumentType();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String string = reader.readString();
        boolean found = Arrays.asList(system.profiles.getCustomProfiles()).contains(string);

        if (!found) {
            throw profileNotFound.create(string);
        }
        return string;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        List<String> list = Arrays.asList(system.profiles.getCustomProfiles());
        return CommandSource.suggestMatching(list, builder);
    }
}
