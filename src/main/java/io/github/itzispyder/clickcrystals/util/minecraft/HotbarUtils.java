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

public final class HotbarUtils implements Global {

    public static boolean search(Item item) {
        if (PlayerUtils.invalid()) {
            return false;
        }

        PlayerInventory inv = PlayerUtils.player().getInventory();

        for (int i = 0; i <= 8; i ++) {
            if (inv.getStack(i).isOf(item)) {
                inv.setSelectedSlot(i);
                return true;
            }
        }
        return false;
    }

    public static boolean search(Predicate<ItemStack> item) {
        if (PlayerUtils.invalid()) {
            return false;
        }

        PlayerInventory inv = PlayerUtils.player().getInventory();

        for (int i = 0; i <= 8; i ++) {
            if (item.test(inv.getStack(i))) {
                inv.setSelectedSlot(i);
                return true;
            }
        }
        return false;
    }

    public static int searchSlot(Predicate<ItemStack> item) {
        if (PlayerUtils.invalid())
            return -1;

        PlayerInventory inv = PlayerUtils.player().getInventory();

        for (int i = 0; i <= 8; i ++)
            if (item.test(inv.getStack(i)))
                return i;
        return -1;
    }

    public static boolean has(Item item) {
        if (PlayerUtils.invalid()) {
            return false;
        }

        PlayerInventory inv = PlayerUtils.player().getInventory();

        for (int i = 0; i <= 8; i ++) {
            if (inv.getStack(i).isOf(item)) return true;
        }
        return false;
    }

    public static boolean has(Predicate<ItemStack> item) {
        if (PlayerUtils.invalid()) {
            return false;
        }

        PlayerInventory inv = PlayerUtils.player().getInventory();

        for (int i = 0; i <= 8; i ++) {
            if (item.test(inv.getStack(i))) return true;
        }
        return false;
    }

    public static boolean hasButNotHolding(Predicate<ItemStack> item) {
        if (PlayerUtils.invalid())
            return false;

        for (int i = 0; i <= 8; i++)
            if (item.test(InvUtils.inv().getStack(i)) && i != InvUtils.selected())
                return true;
        return false;
    }

    public static boolean isHolding(Item item) {
        return isHolding(item,Hand.MAIN_HAND);
    }

    public static ItemStack getHand() {
        return getHand(Hand.MAIN_HAND);
    }

    public static ItemStack getHand(Hand hand) {
        if (PlayerUtils.invalid()) {
            return ItemStack.EMPTY;
        }

        ItemStack item = PlayerUtils.player().getStackInHand(hand);
        return item != null ? item : ItemStack.EMPTY;
    }

    public static boolean isHolding(Item item, Hand hand) {
        if (PlayerUtils.invalid()) {
            return false;
        }
        return PlayerUtils.player().getStackInHand(hand).isOf(item);
    }

    public static boolean nameContains(String contains) {
        return nameContains(contains,Hand.MAIN_HAND);
    }

    public static boolean nameContains(String contains, Hand hand) {
        if (PlayerUtils.invalid()) {
            return false;
        }

        ItemStack item = PlayerUtils.player().getStackInHand(hand);
        return item != null && item.getItem().getTranslationKey().toLowerCase().contains(contains.toLowerCase());
    }

    public static void forEachItem(Consumer<ItemStack> run) {
        if (PlayerUtils.invalid()) {
            return;
        }

        for (int i = 0; i < 9; i ++) {
            ItemStack item = PlayerUtils.player().getInventory().getStack(i);
            if (item == null) continue;
            if (item.isEmpty()) continue;
            run.accept(item);
        }
    }

    public static void forEachItem(BiConsumer<Integer,ItemStack> run) {
        if (PlayerUtils.invalid()) {
            return;
        }

        for (int i = 0; i < 9; i ++) {
            ItemStack item = PlayerUtils.player().getInventory().getStack(i);
            if (item == null) continue;
            if (item.isEmpty()) continue;
            run.accept(i,item);
        }
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
        if (PlayerUtils.invalid()) {
            return;
        }

        PlayerActionC2SPacket.Action action = PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND;
        PlayerActionC2SPacket swap = new PlayerActionC2SPacket(action, PlayerUtils.player().getBlockPos(), Direction.UP);
        PlayerUtils.player().networkHandler.sendPacket(swap);
    }

    public static double getFullHealthRatio() {
        PlayerEntity p = PlayerUtils.player();
        return p == null ? 0.0 : p.getHealth() / p.getMaxHealth();
    }

    public static boolean isTotemed() {
        return isHoldingEitherHand(Items.TOTEM_OF_UNDYING);
    }

    public static boolean isHoldingEitherHand(Item item) {
        if (PlayerUtils.invalid()) {
            return false;
        }

        ItemStack main = PlayerUtils.player().getStackInHand(Hand.MAIN_HAND);
        ItemStack off = PlayerUtils.player().getStackInHand(Hand.OFF_HAND);

        return (main != null && main.getItem() == item) || (off != null && off.getItem() == item);
    }

    public static boolean isHoldingEitherHand(Predicate<ItemStack> item) {
        if (PlayerUtils.invalid()) {
            return false;
        }

        ItemStack main = PlayerUtils.player().getStackInHand(Hand.MAIN_HAND);
        ItemStack off = PlayerUtils.player().getStackInHand(Hand.OFF_HAND);

        return (main != null && item.test(main)) || (off != null && item.test(off));
    }
}
