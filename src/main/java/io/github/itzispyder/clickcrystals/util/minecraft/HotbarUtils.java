package io.github.itzispyder.clickcrystals.util.minecraft;

import io.github.itzispyder.clickcrystals.Global;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Client hot bar utils
 */
public final class HotbarUtils implements Global {

    /**
     * Search of the item in a player's hot bar.
     * Scrolls to the item.
     * @param item item to search
     */
    public static boolean search(Item item) {
        final PlayerInventory inv = mc.player.getInventory();
        for (int i = 0; i <= 8; i ++) {
            if (inv.getStack(i).isOf(item)) {
                inv.selectedSlot = i;
                return true;
            }
        }
        return false;
    }

    public static boolean search(Predicate<ItemStack> item) {
        final PlayerInventory inv = mc.player.getInventory();
        for (int i = 0; i <= 8; i ++) {
            if (item.test(inv.getStack(i))) {
                inv.selectedSlot = i;
                return true;
            }
        }
        return false;
    }

    /**
     * Checks of the hotbar has an item
     * @param item item
     * @return has item
     */
    public static boolean has(Item item) {
        final PlayerInventory inv = mc.player.getInventory();
        for (int i = 0; i <= 8; i ++) {
            if (inv.getStack(i).isOf(item)) return true;
        }
        return false;
    }

    /**
     * Checks of the hotbar has an item
     * @param item item
     * @return has item
     */
    public static boolean has(Predicate<ItemStack> item) {
        final PlayerInventory inv = mc.player.getInventory();
        for (int i = 0; i <= 8; i ++) {
            if (item.test(inv.getStack(i))) return true;
        }
        return false;
    }

    /**
     * If the player's held item matches the provided item type
     * @param item item type
     * @return match
     */
    public static boolean isHolding(Item item) {
        return isHolding(item,Hand.MAIN_HAND);
    }

    public static ItemStack getHand() {
        return getHand(Hand.MAIN_HAND);
    }

    public static ItemStack getHand(Hand hand) {
        ItemStack item = mc.player.getStackInHand(hand);
        return item != null ? item : ItemStack.EMPTY;
    }

    /**
     * If the players held item in the specified hand matches the provided type
     * @param item item type
     * @param hand hand to check
     * @return match
     */
    public static boolean isHolding(Item item, Hand hand) {
        return mc.player.getStackInHand(hand).isOf(item);
    }

    /**
     * If the player's held item's name contains the following string
     * @param contains string to check
     * @return match
     */
    public static boolean nameContains(String contains) {
        return nameContains(contains,Hand.MAIN_HAND);
    }

    /**
     * If the player's held item in thee specified hand's name contains the following string
     * @param contains string to check
     * @param hand hand to check
     * @return match
     */
    public static boolean nameContains(String contains, Hand hand) {
        ItemStack item = mc.player.getStackInHand(hand);
        return item != null && item.getTranslationKey().toLowerCase().contains(contains.toLowerCase());
    }

    public static void forEachItem(Consumer<ItemStack> run) {
        for (int i = 0; i < 9; i ++) {
            ItemStack item = mc.player.getInventory().getStack(i);
            if (item == null) continue;
            if (item.isEmpty()) continue;
            run.accept(item);
        }
    }

    public static void forEachItem(BiConsumer<Integer,ItemStack> run) {
        for (int i = 0; i < 9; i ++) {
            ItemStack item = mc.player.getInventory().getStack(i);
            if (item == null) continue;
            if (item.isEmpty()) continue;
            run.accept(i,item);
        }
    }

    public static ItemStack[] getContents() {
        PlayerInventory inv = mc.player.getInventory();
        ItemStack[] stacks = new ItemStack[]{};
        for (int i = 0; i < 9; i++) {
            stacks[i] = inv.getStack(i);
        }
        return stacks;
    }

    public static boolean isForClickCrystal() {
        return nameContains("sword")
                || isHolding(Items.END_CRYSTAL)
                || isHolding(Items.OBSIDIAN)
                || isHolding(Items.TOTEM_OF_UNDYING)
                || isHolding(Items.GLOWSTONE)
                || isHolding(Items.RESPAWN_ANCHOR);
    }

    public static boolean isHoldingTool() {
        return nameContains("_sword") ||
                nameContains("_pickaxe") ||
                nameContains("_axe") ||
                nameContains("_hoe") ||
                nameContains("_shovel") ||
                nameContains("trident");
    }

    public static void swapWithOffhand() {
        PlayerActionC2SPacket swap = new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, mc.player.getBlockPos(), Direction.UP);
        mc.player.networkHandler.sendPacket(swap);
    }

    public static double getFullHealthRatio() {
        final PlayerEntity p = mc.player;
        if (p == null) return 0.0;
        return p.getHealth() / p.getMaxHealth();
    }

    public static boolean isTotemed() {
        return isHoldingEitherHand(Items.TOTEM_OF_UNDYING);
    }

    public static boolean isHoldingEitherHand(Item item) {
        if (mc.player == null) return false;

        final ItemStack main = mc.player.getStackInHand(Hand.MAIN_HAND);
        final ItemStack off = mc.player.getStackInHand(Hand.OFF_HAND);

        return (main != null && main.getItem() == item) || (off != null && off.getItem() == item);
    }

    public static boolean isHoldingEitherHand(Predicate<ItemStack> item) {
        if (mc.player == null) return false;

        final ItemStack main = mc.player.getStackInHand(Hand.MAIN_HAND);
        final ItemStack off = mc.player.getStackInHand(Hand.OFF_HAND);

        return (main != null && item.test(main)) || (off != null && item.test(off));
    }
}
