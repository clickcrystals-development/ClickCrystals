package io.github.itzispyder.clickcrystals.modules.modules.crystalling;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.BlockUtils;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;

public class CrystSwitch extends Module implements Listener {

    public CrystSwitch() {
        super("CrystalSwitch", Categories.CRYSTALLING,"Whenever you punch bedrock or obsidian with a sword, it will switch to a crystal.");
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
    private void onSendPacket(PacketSendEvent e) {
        if (e.getPacket() instanceof PlayerActionC2SPacket packet) {
            final BlockPos pos = packet.getPos();

            if (packet.getAction() != PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) return;
            if (!BlockUtils.isCrystallabe(pos)) return;
            if (!HotbarUtils.has(Items.END_CRYSTAL)) return;

            if (HotbarUtils.isForClickCrystal()) {
                e.setCancelled(true);
                HotbarUtils.search(Items.END_CRYSTAL);
                BlockUtils.interact(pos,packet.getDirection());
            }
        }
    }
}
