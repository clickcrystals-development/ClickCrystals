package io.github.itzispyder.clickcrystals.mixins;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.client.commands.Command;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.commands.SharedSuggestionProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.concurrent.CompletableFuture;

@Mixin(CommandSuggestions.class)
public abstract class MixinChatInputSuggestor {

    @Shadow @Nullable private ParseResults<SharedSuggestionProvider> currentParse;
    @Shadow @Nullable private CompletableFuture<Suggestions> pendingSuggestions;
    @Shadow private boolean keepSuggestions;
    @Shadow @Final private EditBox input;
    @Shadow public abstract void updateCommandInfo();

    @Inject(method = "updateCommandInfo", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/StringReader;canRead()Z", remap = false), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void refresh(CallbackInfo ci, String string, StringReader reader) {
        String prefix = ClickCrystals.commandPrefix.getKeyName();
        int len = prefix.length();

        if (reader.canRead(len) && reader.getString().startsWith(prefix)) {
            reader.setCursor(reader.getCursor() + len);

            if (currentParse == null) {
                currentParse = Command.DISPATCHER.parse(reader, Command.SOURCE);
            }

            int cursor = input.getCursorPosition();
            if (!keepSuggestions && cursor >= 1) {
                pendingSuggestions = Command.DISPATCHER.getCompletionSuggestions(currentParse, cursor);
                pendingSuggestions.thenRun(() -> {
                    if (pendingSuggestions.isDone()) {
                        updateCommandInfo();
                    }
                });
            }
            ci.cancel();
        }
    }
}
