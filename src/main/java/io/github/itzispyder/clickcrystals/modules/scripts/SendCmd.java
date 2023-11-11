package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.ChatUtils;

public class SendCmd extends ScriptCommand {

    public SendCmd() {
        super("send");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        ChatUtils.sendPrefixMessage(args.getAll().stringValue());
    }
}
