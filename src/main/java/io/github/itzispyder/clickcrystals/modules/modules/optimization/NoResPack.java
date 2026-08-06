package io.github.itzispyder.clickcrystals.modules.modules.optimization;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket.Action;

import java.util.UUID;

public class NoResPack extends Module implements Listener {

    public NoResPack() {
        super("no-server-packs", Categories.LAG,"Prevents servers from forcing you to download their bad resource pack");
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
    }

    @EventHandler
    private void onResourceReceive(PacketReceiveEvent e) {
        // packs are pushed while configuring too, so the listener is answered, not the player
        if (!(e.getPacket() instanceof ClientboundResourcePackPushPacket packet)
                || !(e.getListener() instanceof ClientCommonPacketListenerImpl listener))
            return;

        // drop the pack (never download it), but spoof the full accept/load handshake so the
        // server thinks we loaded it and doesn't hang or kick us off forced packs
        e.setCancelled(true);
        UUID id = packet.id();
        respond(listener, id, Action.ACCEPTED);
        respond(listener, id, Action.DOWNLOADED);
        respond(listener, id, Action.SUCCESSFULLY_LOADED);

        // packets are read on the network thread, so the chat line waits for the client one
        String status = packet.required() ? "forced" : "suggested";
        mc.execute(() -> ChatUtils.sendPrefixMessage("Blocked 1 " + status + " resource pack"));
    }

    private void respond(ClientCommonPacketListenerImpl listener, UUID id, Action action) {
        listener.send(new ServerboundResourcePackPacket(id, action));
    }
}
