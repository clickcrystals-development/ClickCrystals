package io.github.itzispyder.clickcrystals.mixins;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.commands.CustomCommand;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.command.CommandSource;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.concurrent.CompletableFuture;

@Mixin(ChatInputSuggestor.class)
public abstract class ChatInputSuggestorMixin {

    @Shadow @Nullable private ParseResults<CommandSource> parse;
    @Shadow @Nullable private CompletableFuture<Suggestions> pendingSuggestions;
    @Shadow private boolean completingSuggestions;
    @Shadow @Final private TextFieldWidget textField;
    @Shadow protected abstract void showCommandSuggestions();

    @Inject(method = "refresh", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/StringReader;canRead()Z", remap = false), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void refresh(CallbackInfo ci, String string, StringReader reader) {
        String prefix = ClickCrystals.commandPrefix.getKeyName();
        int len = prefix.length();

        if (reader.canRead(len) && reader.getString().startsWith(prefix)) {
            reader.setCursor(reader.getCursor() + len);

            if (parse == null) {
                parse = CustomCommand.DISPATCHER.parse(reader, CustomCommand.SOURCE);
            }

            int cursor = textField.getCursor();
            if (!completingSuggestions) {
                pendingSuggestions = CustomCommand.DISPATCHER.getCompletionSuggestions(parse, cursor);
                pendingSuggestions.thenRun(() -> {
                    if (pendingSuggestions.isDone()) {
                        showCommandSuggestions();
                    }
                });
            }
            ci.cancel();
        }
    }
}
