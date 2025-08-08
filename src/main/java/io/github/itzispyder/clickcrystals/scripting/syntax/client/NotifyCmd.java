package io.github.itzispyder.clickcrystals.scripting.syntax.client;

import io.github.itzispyder.clickcrystals.client.system.Notification;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;

// @Format notify "..."
public class NotifyCmd extends ScriptCommand {

    public NotifyCmd() {
        super("notify");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        Notification.create()
                .ccsIcon()
                .stayTime((long)(args.get(0).toDouble() * 1000L))
                .id("clickscript-triggered-notification")
                .title("ClickCrystals System")
                .text(args.getQuoteAndRemove(1))
                .build()
                .sendToClient();

        if (args.match(0, "then")) {
            args.executeAll(1);
        }
    }
}
