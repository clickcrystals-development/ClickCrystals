package io.github.itzispyder.clickcrystals.util.minecraft;

import io.github.itzispyder.clickcrystals.Global;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class HotbarUtils implements Global {

    public static boolean search(Item item) {
        if (PlayerUtils.invalid()) {
            return false;
        }

        Inventory inv = PlayerUtils.player().getInventory();

        for (int i = 0; i <= 8; i ++) {
            if (inv.getItem(i).is(item)) {
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

        Inventory inv = PlayerUtils.player().getInventory();

        for (int i = 0; i <= 8; i ++) {
            if (item.test(inv.getItem(i))) {
                inv.setSelectedSlot(i);
                return true;
            }
        }
        return false;
    }

    public static int count(Predicate<ItemStack> item) {
        if (PlayerUtils.invalid())
            return 0;

        Inventory inv = InvUtils.inv();
        int count = 0;
        for (int i = 0; i < 9; i++)
            if (item.test(inv.getItem(i)))
                count++;
        return count;
    }

    public static int searchSlot(Predicate<ItemStack> item) {
        if (PlayerUtils.invalid())
            return -1;

        Inventory inv = PlayerUtils.player().getInventory();

        for (int i = 0; i <= 8; i ++)
            if (item.test(inv.getItem(i)))
                return i;
        return -1;
    }

    public static ItemStack searchItem(Predicate<ItemStack> item) {
        if (PlayerUtils.invalid())
            return ItemStack.EMPTY;

        Inventory inv = PlayerUtils.player().getInventory();
        for (int i = 0; i <= 8; i ++) {
            ItemStack currStack = inv.getItem(i);
            if (item.test(currStack))
                return currStack;
        }
        return ItemStack.EMPTY;
    }

    public static boolean has(Item item) {
        if (PlayerUtils.invalid()) {
            return false;
        }

        Inventory inv = PlayerUtils.player().getInventory();

        for (int i = 0; i <= 8; i ++) {
            if (inv.getItem(i).is(item)) return true;
        }
        return false;
    }

    public static boolean has(Predicate<ItemStack> item) {
        if (PlayerUtils.invalid()) {
            return false;
        }

        Inventory inv = PlayerUtils.player().getInventory();

        for (int i = 0; i <= 8; i ++) {
            if (item.test(inv.getItem(i))) return true;
        }
        return false;
    }

    public static boolean hasButNotHolding(Predicate<ItemStack> item) {
        if (PlayerUtils.invalid())
            return false;

        for (int i = 0; i <= 8; i++)
            if (item.test(InvUtils.inv().getItem(i)) && i != InvUtils.selected())
                return true;
        return false;
    }

    public static boolean isHolding(Item item) {
        return isHolding(item,InteractionHand.MAIN_HAND);
    }

    public static ItemStack getHand() {
        return getHand(InteractionHand.MAIN_HAND);
    }

    public static ItemStack getHand(InteractionHand hand) {
        if (PlayerUtils.invalid()) {
            return ItemStack.EMPTY;
        }

        ItemStack item = PlayerUtils.player().getItemInHand(hand);
        return item != null ? item : ItemStack.EMPTY;
    }

    public static boolean isHolding(Item item, InteractionHand hand) {
        if (PlayerUtils.invalid()) {
            return false;
        }
        return PlayerUtils.player().getItemInHand(hand).is(item);
    }

    public static boolean nameContains(String contains) {
        return nameContains(contains,InteractionHand.MAIN_HAND);
    }

    public static boolean nameContains(String contains, InteractionHand hand) {
        if (PlayerUtils.invalid()) {
            return false;
        }

        ItemStack item = PlayerUtils.player().getItemInHand(hand);
        return item != null && item.getItem().getDescriptionId().toLowerCase().contains(contains.toLowerCase());
    }

    public static void forEachItem(Consumer<ItemStack> run) {
        if (PlayerUtils.invalid()) {
            return;
        }

        for (int i = 0; i < 9; i ++) {
            ItemStack item = PlayerUtils.player().getInventory().getItem(i);
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
            ItemStack item = PlayerUtils.player().getInventory().getItem(i);
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

        ServerboundPlayerActionPacket.Action action = ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND;
        ServerboundPlayerActionPacket swap = new ServerboundPlayerActionPacket(action, PlayerUtils.player().blockPosition(), Direction.UP);
        PlayerUtils.player().connection.send(swap);
    }

    public static double getFullHealthRatio() {
        Player p = PlayerUtils.player();
        return p == null ? 0.0 : p.getHealth() / p.getMaxHealth();
    }

    public static boolean isTotemed() {
        return isHoldingEitherHand(Items.TOTEM_OF_UNDYING);
    }

    public static boolean isHoldingEitherHand(Item item) {
        if (PlayerUtils.invalid()) {
            return false;
        }

        ItemStack main = PlayerUtils.player().getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack off = PlayerUtils.player().getItemInHand(InteractionHand.OFF_HAND);

        return (main != null && main.getItem() == item) || (off != null && off.getItem() == item);
    }

    public static boolean isHoldingEitherHand(Predicate<ItemStack> item) {
        if (PlayerUtils.invalid()) {
            return false;
        }

        ItemStack main = PlayerUtils.player().getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack off = PlayerUtils.player().getItemInHand(InteractionHand.OFF_HAND);

        return (main != null && item.test(main)) || (off != null && item.test(off));
    }
}
