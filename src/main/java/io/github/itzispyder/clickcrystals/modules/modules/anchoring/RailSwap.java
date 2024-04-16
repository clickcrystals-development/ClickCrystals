package io.github.itzispyder.clickcrystals.modules.modules.anchoring;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.List;

public class RailSwap extends Module implements Listener {

    private static final List<Item> RAILS = List.of(
            Items.RAIL,
            Items.ACTIVATOR_RAIL,
            Items.DETECTOR_RAIL,
            Items.POWERED_RAIL
    );

    public RailSwap() {
        super("rail-swap", Categories.PVP, "Swaps to rails after shooting bow");
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
    private void onShootBow(MouseClickEvent e) {
        if (e.getAction().isRelease() && e.isScreenNull() && HotbarUtils.isHoldingEitherHand(Items.BOW) && HotbarUtils.has(RailSwap::isRail)) {
            system.scheduler.runDelayedTask(() -> {
                HotbarUtils.search(RailSwap::isRail);
            }, 50);
        }
    }

    public static boolean isRail(ItemStack stack) {
        return RAILS.contains(stack.getItem());
    }

    public static boolean isRail(Item item) {
        return RAILS.contains(item);
    }
}
