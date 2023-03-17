package io.github.itzispyder.clickcrystals.util;

import net.minecraft.item.Item;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

/**
 * Hot bar player utils
 */
public abstract class HotbarUtils {

    /**
     * Search of the item in a player's hot bar
     * @param item item to search
     */
    public static boolean search(Item item) {
        for (int i = 0; i < 9; i ++) {
            mc.player.getInventory().scrollInHotbar(1);
            if (mc.player.getInventory().getStack(mc.player.getInventory().selectedSlot).isOf(item)) {
                return true;
            }
        }
        return false;
    }
}
