package io.github.itzispyder.clickcrystals.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

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

    public static boolean isHolding(Item item) {
        return isHolding(item,Hand.MAIN_HAND);
    }

    public static boolean isHolding(Item item, Hand hand) {
        return mc.player.getStackInHand(hand).isOf(item);
    }

    public static boolean nameContains(String contains) {
        return nameContains(contains,Hand.MAIN_HAND);
    }

    public static boolean nameContains(String contains, Hand hand) {
        ItemStack item = mc.player.getStackInHand(hand);
        return item != null && item.getTranslationKey().toLowerCase().contains(contains.toLowerCase());
    }
}
