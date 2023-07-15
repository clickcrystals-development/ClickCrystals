package io.github.itzispyder.clickcrystals.client;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import net.minecraft.network.packet.Packet;

import java.util.HashMap;
import java.util.Map;

public class PacketLogger implements Listener {

    private final Map<String, PacketInfo> log = new HashMap<>();
    private int tickTimer;

    public PacketLogger() {
        this.tickTimer = 0;
    }

    @EventHandler
    public void tick() {
        if (tickTimer++ >= 20) {
            for (PacketInfo info : log.values()) {
                info.incrementTime();
            }
            tickTimer = 0;
        }
    }

    public void log(Packet<?> packet, boolean print) {
        String name = PacketMapper.getNameColored(packet);
        PacketInfo info = log.getOrDefault(name, new PacketInfo(packet));
        info.onLogged();

        if (print) {
            info.print();
        }
    }

    public Map<String, PacketInfo> getLog() {
        return log;
    }
}
