package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;

public class Totem2Pearl extends Module implements Listener {

    private static long cooldown;

    /**
     * Module constructor
     */
    public Totem2Pearl() {
        super("Totem2Pearl", Categories.CRYSTALLING,"TpBlade, but with a totem.");
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
            ItemStack item = mc.player.getStackInHand(mc.player.getActiveHand());
            Item type = item.getItem();
            if (!HotbarUtils.nameContains("totem")) return;
            if (!HotbarUtils.has(Items.ENDER_PEARL)) return;
            if (cooldown > System.currentTimeMillis()) return;
            cooldown = System.currentTimeMillis() + (50 * 4);

            e.setCancelled(true);
            HotbarUtils.search(Items.ENDER_PEARL);
            mc.interactionManager.interactItem(mc.player,mc.player.getActiveHand());
            HotbarUtils.search(type);
        }
    }
}
