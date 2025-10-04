package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.syntax.client.ModuleCmd;

import static io.github.itzispyder.clickcrystals.scripting.syntax.logic.CancelPacketCmd.getC2S;
import static io.github.itzispyder.clickcrystals.scripting.syntax.logic.CancelPacketCmd.getSC2;

// @Format uncancel_packet s2c <server-packet>
// @Format uncancel_packet c2s <client-packet>
public class UncancelPacketCmd extends ScriptCommand {

    public UncancelPacketCmd() {
        super("uncancel_packet");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        var read = args.getReader();
        switch (read.next(CancelPacketCmd.Mode.class)) {
            case C2S -> ModuleCmd.runOnCurrentScriptModule(m -> m.packetSendCancelQueue.remove(getC2S(read.nextStr())));
            case S2C -> ModuleCmd.runOnCurrentScriptModule(m -> m.packetReadCancelQueue.remove(getSC2(read.nextStr())));
            default -> throw new IllegalArgumentException("packet type should be either c2s or s2c");
        }
    }
}
