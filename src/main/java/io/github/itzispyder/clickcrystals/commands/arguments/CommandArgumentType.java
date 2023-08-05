package io.github.itzispyder.clickcrystals.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.itzispyder.clickcrystals.client.system.ClickCrystalsSystem;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.util.ArrayUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CommandArgumentType implements ArgumentType<Command> {

    private static final ClickCrystalsSystem system = ClickCrystalsSystem.getInstance();
    public static final DynamicCommandExceptionType commandNotFound = new DynamicCommandExceptionType(module -> Text.literal("Command " + module + " not found!"));
    public static final List<String> examples = List.of("debug", "help", "toggle");

    public CommandArgumentType() {

    }

    public static CommandArgumentType create() {
        return new CommandArgumentType();
    }

    @Override
    public Command parse(StringReader reader) throws CommandSyntaxException {
        String string = reader.readString();
        Command command = null;

        for (Command value : system.commands().values()) {
            if (value.getName().equalsIgnoreCase(string)) {
                command = value;
                break;
            }
        }

        if (command == null) throw commandNotFound.create(string);
        return command;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(ArrayUtils.toNewList(system.commands().values(), Command::getName), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return examples;
    }
}
