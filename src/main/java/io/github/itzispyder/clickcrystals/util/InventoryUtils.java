package io.github.itzispyder.clickcrystals.util;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public final class InventoryUtils {

    public static PlayerInventory inv() {
        return mc.player.getInventory();
    }

    public static int selected() {
        return inv().selectedSlot;
    }

    public static ItemStack selectedStack() {
        return inv().getStack(selected());
    }

    public static void dropStack(int slot) {
        final ItemStack item = inv().getStack(slot);

        if (item == null) return;
        if (item.isEmpty()) return;

        mc.player.dropItem(item, true, true);
    }
}
