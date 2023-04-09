package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.scheduler.ScheduledTask;
import io.github.itzispyder.clickcrystals.util.BlockUtils;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.InteractionUtils;
import io.github.itzispyder.clickcrystals.util.Randomizer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;

public class CrystalSearch extends Module implements Listener {

    public CrystalSearch() {
        super("CrystalSearch","The opposite of ObsidianSearch.");
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
            if (packet.getAction() != PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) return;
            BlockPos pos = packet.getPos();
            if (!BlockUtils.isCrystallabe(pos)) return;
            if (!HotbarUtils.has(Items.END_CRYSTAL)) return;

            if (HotbarUtils.nameContains("obsidian") || HotbarUtils.nameContains("totem") || HotbarUtils.nameContains("sword")) {
                ItemStack item = mc.player.getStackInHand(mc.player.getActiveHand());
                Item type = item.getItem();

                e.setCancelled(true);
                HotbarUtils.search(Items.END_CRYSTAL);
                BlockUtils.interact(pos,packet.getDirection());
                HotbarUtils.search(type);

                // DO NOT USE THIS AREA, ONLY ENABLE THIS WHEN HACKING IS ALLOWED!
                // ANTICHEATS WILL ALSO GET YOU IF YOU ENABLE THIS LOWER PART
                Module auto = Module.get(ClickCrystalAuto.class);
                if (!auto.isEnabled()) return;
                new ScheduledTask(() -> {
                    InteractionUtils.doAttack();
                }).runDelayedTask(Randomizer.rand(50,100));
            }
        }
    }
}
