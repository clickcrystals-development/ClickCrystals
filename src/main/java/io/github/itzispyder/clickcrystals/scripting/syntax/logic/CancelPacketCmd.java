package io.github.itzispyder.clickcrystals.scripting.syntax.logic;

import io.github.itzispyder.clickcrystals.client.networking.PacketMapper;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.syntax.client.ModuleCmd;

// @Format cancel_packet s2c <server-packet>
// @Format cancel_packet c2s <client-packet>
public class CancelPacketCmd extends ScriptCommand {

    public CancelPacketCmd() {
        super("cancel_packet");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        String id = args.get(1).toString();
        switch (args.get(0).toEnum(Mode.class)) {
            case C2S -> ModuleCmd.runOnCurrentScriptModule(m -> m.packetSendCancelQueue.add(getC2S(id)));
            case S2C -> ModuleCmd.runOnCurrentScriptModule(m -> m.packetReadCancelQueue.add(getSC2(id)));
            default -> throw new IllegalArgumentException("packet type should be either c2s or s2c");
        }
    }

    public static PacketMapper.Info getC2S(String id) {
        for (PacketMapper.Info info: PacketMapper.C2S.values())
            if (id.equalsIgnoreCase(info.id()))
                return info;
        throw new IllegalArgumentException("packet '%s' does not exist!".formatted(id));
    }

    public static PacketMapper.Info getSC2(String id) {
        for (PacketMapper.Info info: PacketMapper.S2C.values())
            if (id.equalsIgnoreCase(info.id()))
                return info;
        throw new IllegalArgumentException("packet '%s' does not exist!".formatted(id));
    }

    public enum Mode {
        C2S,
        S2C
    }
}
