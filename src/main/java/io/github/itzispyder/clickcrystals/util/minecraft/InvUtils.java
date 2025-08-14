package io.github.itzispyder.clickcrystals.util.minecraft;

import io.github.itzispyder.clickcrystals.Global;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.screen.sync.ItemStackHash;
import net.minecraft.util.Hand;

import java.util.function.Predicate;

public final class InvUtils implements Global {

    public static PlayerInventory inv() {
        return mc.player.getInventory();
    }

    public static int selected() {
        return inv().getSelectedSlot();
    }

    public static void select(int slot) {
        inv().setSelectedSlot(slot);
    }

    public static ItemStack selectedStack() {
        return inv().getStack(selected());
    }

    public static boolean swapOffhand(int slot) {
        return sendSlotPacket(slot, 40, SlotActionType.SWAP);
    }

    public static boolean quickMove(int slot) {
        return sendSlotPacket(slot, 0, SlotActionType.QUICK_MOVE);
    }

    public static boolean dropSlot(int slot, boolean full) {
        return sendSlotPacket(slot, full ? 1 : 0, SlotActionType.THROW);
    }

    public static boolean isHotbar(int slot) {
        return slot <= 8;
    }

    public static boolean isOffhand(int slot) {
        return slot == 45;
    }

    public static int search(Item item) {
        return search(stack -> stack.isOf(item));
    }

    public static int search(Predicate<ItemStack> item) {
        if (item == null) return -1;

        for (int i = 0; i < inv().getMainStacks().size(); i++) {
            ItemStack stack = inv().getStack(i);
            if (stack == null || stack.isEmpty()) continue;
            if (item.test(stack)) return i;
        }

        return -1;
    }

    public static int searchInsideOnly(Item item) {
        return searchInsideOnly(stack -> stack.isOf(item));
    }

    public static int searchInsideOnly(Predicate<ItemStack> item) {
        if (item == null)
            return -1;

        for (int i = 9; i < 36; i++) {
            ItemStack stack = inv().getStack(i);
            if (stack == null || stack.isEmpty())
                continue;
            if (item.test(stack))
                return i;
        }

        return -1;
    }

    public static int count(Item item) {
        return count(stack -> stack.isOf(item));
    }

    public static int count(Predicate<ItemStack> item) {
        int count = 0;

        if (item == null) {
            return count;
        }

        for (int i = 0; i < inv().getMainStacks().size(); i++) {
            ItemStack stack = inv().getStack(i);
            if (stack == null || stack.isEmpty()) continue;
            if (item.test(stack)) count += stack.getCount();
        }

        ItemStack off = HotbarUtils.getHand(Hand.OFF_HAND);
        if (item.test(off)) {
            count += off.getCount();
        }

        return count;
    }

    public static int count(Item item, PlayerInventory other) {
        int count = 0;

        if (item == null || other == null) {
            return count;
        }

        for (int i = 0; i < other.size(); i++) {
            ItemStack stack = other.getStack(i);
            if (stack == null || stack.isEmpty()) continue;
            if (stack.isOf(item)) count += stack.getCount();
        }

        return count;
    }

    public static ItemStack getEquipmentSlot(EquipmentSlot equipmentSlot){
        if (equipmentSlot == null) return ItemStack.EMPTY;
        return inv().player.getEquippedStack(equipmentSlot).getItem().getDefaultStack();
    }

    public static boolean isWearing(Item item) {
        if (item == null)
            return false;

        for (EquipmentSlot slot : EquipmentSlot.values())
            if (getEquipmentSlot(slot).isOf(item))
                return true;
        return false;
    }

    public static boolean isWearing(Predicate<ItemStack> item) {
        if (item == null) return false;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = getEquipmentSlot(slot);
            if (stack == null || stack.isEmpty()) continue;
            if (item.test(stack)) return true;
        }

        return false;
    }

    public static boolean has(Item item) {
        if (item == null) return false;

        for (int i = 0; i < inv().getMainStacks().size(); i++) {
            ItemStack stack = inv().getStack(i);
            if (stack == null || stack.isEmpty()) continue;
            if (stack.isOf(item)) return true;
        }

        return false;
    }

    public static boolean has(Predicate<ItemStack> item) {
        if (item == null) return false;

        for (int i = 0; i < inv().getMainStacks().size(); i++) {
            ItemStack stack = inv().getStack(i);
            if (stack == null || stack.isEmpty()) continue;
            if (item.test(stack)) return true;
        }

        return false;
    }

    public static boolean sendSlotPacket(int slot, int button, SlotActionType action) {
        ItemStack stack = inv().getStack(slot);

        if (slot <= 8) {
            slot += 36;
        }

        if (stack == null || PlayerUtils.invalid()) return false;

        int hashSlot = slot;
        ItemStackHash hash = ItemStackHash.fromItemStack(stack, component -> hashSlot);
        ClickSlotC2SPacket swap = new ClickSlotC2SPacket(0, 1, (short) slot, (byte) button, action, Int2ObjectMaps.singleton(slot, hash), hash);
        PlayerUtils.sendPacket(swap);
        return true;
    }
}
