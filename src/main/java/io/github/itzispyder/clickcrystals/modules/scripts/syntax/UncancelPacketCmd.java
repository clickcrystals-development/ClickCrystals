package io.github.itzispyder.clickcrystals.modules.scripts.syntax;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.modules.scripts.client.ModuleCmd;

import static io.github.itzispyder.clickcrystals.modules.scripts.syntax.CancelPacketCmd.getC2S;
import static io.github.itzispyder.clickcrystals.modules.scripts.syntax.CancelPacketCmd.getSC2;

public class UncancelPacketCmd extends ScriptCommand {

    public UncancelPacketCmd() {
        super("uncancel_packet");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        String id = args.get(1).toString();
        switch (args.get(0).toEnum(CancelPacketCmd.Mode.class)) {
            case C2S -> ModuleCmd.runOnCurrentScriptModule(m -> m.packetSendCancelQueue.remove(getC2S(id)));
            case S2C -> ModuleCmd.runOnCurrentScriptModule(m -> m.packetReadCancelQueue.remove(getSC2(id)));
            default -> throw new IllegalArgumentException("packet type should be either c2s or s2c");
        }
    }
}
