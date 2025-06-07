package io.github.itzispyder.clickcrystals.modules.modules.clickcrystals;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;

public class InvPackets extends ListenerModule {

    private long sequence;

    public InvPackets() {
        super("inv-packet-sniffer", Categories.CLIENT, "Observe inventory packets you are sending to the server");
    }

    @EventHandler
    public void onPacketSend(PacketSendEvent e) {
        if (!(e.getPacket() instanceof ClickSlotC2SPacket packet))
            return;

        ChatUtils.sendPrefixMessage(StringUtils.color("""
                &fClickSlotPacket (%s):
                    &7syncId:&f %s
                    &7revision:&f %s
                    &7slot:&f %s
                    &7button:&f %s
                    &7actionType:&f %s
                    &7itemStack:&f %s
                    &7modifiedItems:&f %s
                """.formatted(++sequence, packet.syncId(), packet.revision(), packet.slot(), packet.button(), packet.actionType(), packet.cursor(), packet.modifiedStacks())));
    }
}
