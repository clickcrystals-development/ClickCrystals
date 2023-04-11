package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.scheduler.ScheduledTask;
import io.github.itzispyder.clickcrystals.util.BlockUtils;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.InteractionUtils;
import io.github.itzispyder.clickcrystals.util.Randomizer;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;

/**
 * ClickCrystal module
 */
public class ClickCrystal extends Module implements Listener {

    /**
     * Module constructor
     */
    public ClickCrystal() {
        super("ClickCrystal", Categories.CRYSTALLING,"Punch an obsidian block with an end crystal to place the end crystal.");
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

            if (HotbarUtils.isHolding(Items.END_CRYSTAL)) {
                e.setCancelled(true);
                BlockUtils.interact(pos,packet.getDirection());

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
