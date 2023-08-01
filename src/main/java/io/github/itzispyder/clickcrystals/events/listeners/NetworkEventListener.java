package io.github.itzispyder.clickcrystals.events.listeners;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;

public class NetworkEventListener implements Listener {

    @EventHandler
    public void onPacketSend(PacketSendEvent e) {
        try {

        }
        catch (Exception ignore) {}
    }

    @EventHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        try {

        } catch (Exception ignore) {}
    }
}
