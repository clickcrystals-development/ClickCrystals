package io.github.itzispyder.clickcrystals.modules.scripts.client;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;

public class SayCmd extends ScriptCommand {

    public SayCmd() {
        super("say");
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
