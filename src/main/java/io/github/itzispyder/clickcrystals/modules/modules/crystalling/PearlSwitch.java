package io.github.itzispyder.clickcrystals.modules.modules.crystalling;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.anchoring.ShieldSwitch;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;

public class PearlSwitch extends Module implements Listener {

    private static long cooldown;

    /**
     * Module constructor
     */
    public PearlSwitch() {
        super("PearlSwitch", Categories.CRYSTALLING,"Right click your sword or totem to switch to your pearl slot.");
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
    }

    /**
     * Module function
     * @param e packet send event
     */
    @EventHandler
    private void onRightClick(PacketSendEvent e) {
        if (e.getPacket() instanceof PlayerInteractItemC2SPacket) {
            final Module shieldSwitch = Module.get(ShieldSwitch.class);

            if (HotbarUtils.nameContains("sword")) {
                if (shieldSwitch != null && shieldSwitch.isEnabled()) return;
                if (!HotbarUtils.has(Items.ENDER_PEARL)) return;

                if (cooldown > System.currentTimeMillis()) return;
                cooldown = System.currentTimeMillis() + (50 * 4);

                e.setCancelled(true);
                HotbarUtils.search(Items.ENDER_PEARL);
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            } else {
                if (HotbarUtils.nameContains("totem")) {
                    if (!HotbarUtils.has(Items.ENDER_PEARL)) return;

                    if (cooldown > System.currentTimeMillis()) return;
                    cooldown = System.currentTimeMillis() + (50 * 4);

                    e.setCancelled(true);
                    HotbarUtils.search(Items.ENDER_PEARL);
                    mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                }
            }
        }
    }
}
