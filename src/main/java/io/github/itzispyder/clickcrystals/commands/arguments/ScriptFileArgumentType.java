package io.github.itzispyder.clickcrystals.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.scripting.ClickScript;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ScriptFileArgumentType implements Global, ArgumentType<String> {

    public static final DynamicCommandExceptionType fileNotFound = new DynamicCommandExceptionType(name -> Text.literal("File not found: " + name));

    public static ScriptFileArgumentType create() {
        return new ScriptFileArgumentType();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String string = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());

        for (String path : getPaths()) {
            if (path.equals(string)) {
                return path;
            }
        }
        throw fileNotFound.create(string);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(getPaths(), builder);
    }

    public List<String> getPaths() {
        List<String> paths = new ArrayList<>();
        for (File file : ClickScript.collectFiles()) {
            paths.add(file.getPath().replaceFirst("^.clickcrystals(/|\\\\)scripts(/|\\\\)", ""));
        }
        return paths;
    }
}
