package io.github.itzispyder.clickcrystals.scripting.syntax.client;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;

// @Format (say|chat) "..."
public class SayCmd extends ScriptCommand {

    public SayCmd() {
        super("say", "chat");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        String msg = args.getQuoteAndRemove();

        if (msg != null && !msg.isEmpty()) {
            if (msg.startsWith("/") && msg.length() >= 2) {
                ChatUtils.sendChatCommand(msg.substring(1));
            }
            else {
                ChatUtils.sendChatMessage(msg);
            }
        }

        if (args.match(0, "then")) {
            args.executeAll(1);
        }
    }
}
