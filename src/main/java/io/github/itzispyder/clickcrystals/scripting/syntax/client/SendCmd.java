package io.github.itzispyder.clickcrystals.scripting.syntax.client;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;

// @Format send "..."
public class SendCmd extends ScriptCommand {

    public SendCmd() {
        super("send");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        ChatUtils.sendPrefixMessage(args.getQuoteAndRemove());

        if (args.match(0, "then")) {
            args.executeAll(1);
        }
    }
}
