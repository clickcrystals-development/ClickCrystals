package io.github.itzispyder.clickcrystals.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.itzispyder.clickcrystals.client.system.ClickCrystalsSystem;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.ArrayUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModuleArgumentType implements ArgumentType<Module> {

    private static final ClickCrystalsSystem system = ClickCrystalsSystem.getInstance();
    public static final DynamicCommandExceptionType moduleNotFound = new DynamicCommandExceptionType(module -> Text.literal("Module " + module + " not found!"));
    public static final List<String> examples = List.of("click-crystal", "anchor-switch");

    public ModuleArgumentType() {

    }

    public static ModuleArgumentType create() {
        return new ModuleArgumentType();
    }

    @Override
    public Module parse(StringReader reader) throws CommandSyntaxException {
        String string = reader.readString();
        Module module = null;

        for (Module value : system.collectModules()) {
            if (value.getId().equalsIgnoreCase(string)) {
                module = value;
                break;
            }
        }

        if (module == null) throw moduleNotFound.create(string);
        return module;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(ArrayUtils.toNewList(system.collectModules(), Module::getId), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return examples;
    }
}
