package io.github.itzispyder.clickcrystals.util.minecraft;

import io.github.itzispyder.clickcrystals.Global;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.network.HashedStack;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public final class InvUtils implements Global {

    public static Inventory inv() {
        return mc.player.getInventory();
    }

    public static int selected() {
        return inv().getSelectedSlot();
    }

    public static void select(int slot) {
        inv().setSelectedSlot(slot);
    }

    public static ItemStack selectedStack() {
        return inv().getItem(selected());
    }

    public static boolean swapOffhand(int slot) {
        return sendSlotPacket(slot, 40, ContainerInput.SWAP);
    }

    public static boolean quickMove(int slot) {
        return sendSlotPacket(slot, 0, ContainerInput.QUICK_MOVE);
    }

    public static boolean dropSlot(int slot, boolean full) {
        return sendSlotPacket(slot, full ? 1 : 0, ContainerInput.THROW);
    }

    public static boolean isHotbar(int slot) {
        return slot <= 8;
    }

    public static boolean isOffhand(int slot) {
        return slot == 45;
    }

    public static int search(Item item) {
        return search(stack -> stack.is(item));
    }

    public static int search(Predicate<ItemStack> item) {
        if (item == null) return -1;

        for (int i = 0; i < inv().getNonEquipmentItems().size(); i++) {
            ItemStack stack = inv().getItem(i);
            if (stack == null || stack.isEmpty()) continue;
            if (item.test(stack)) return i;
        }

        return -1;
    }

    public static int searchInsideOnly(Item item) {
        return searchInsideOnly(stack -> stack.is(item));
    }

    public static int searchInsideOnly(Predicate<ItemStack> item) {
        if (item == null)
            return -1;

        for (int i = 9; i < 36; i++) {
            ItemStack stack = inv().getItem(i);
            if (stack == null || stack.isEmpty())
                continue;
            if (item.test(stack))
                return i;
        }

        return -1;
    }

    public static int count(Item item) {
        return count(stack -> stack.is(item));
    }

    public static int count(Predicate<ItemStack> item) {
        int count = 0;

        if (item == null) {
            return count;
        }

        for (int i = 0; i < inv().getNonEquipmentItems().size(); i++) {
            ItemStack stack = inv().getItem(i);
            if (stack != null && item.test(stack))
                count += stack.getCount();
        }

        ItemStack off = HotbarUtils.getHand(InteractionHand.OFF_HAND);
        if (item.test(off)) {
            count += off.getCount();
        }

        return count;
    }

    public static ItemStack getEquipmentSlot(EquipmentSlot equipmentSlot){
        if (equipmentSlot == null) return ItemStack.EMPTY;
        return inv().player.getItemBySlot(equipmentSlot).getItem().getDefaultInstance();
    }

    public static boolean isWearing(Item item) {
        if (item == null)
            return false;

        for (EquipmentSlot slot : EquipmentSlot.values())
            if (getEquipmentSlot(slot).is(item))
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

        for (int i = 0; i < inv().getNonEquipmentItems().size(); i++) {
            ItemStack stack = inv().getItem(i);
            if (stack.is(item))
                return true;
        }

        return false;
    }

    public static boolean has(Predicate<ItemStack> item) {
        if (item == null) return false;

        for (int i = 0; i < inv().getNonEquipmentItems().size(); i++) {
            ItemStack stack = inv().getItem(i);
            if (item.test(stack))
                return true;
        }

        return false;
    }

    public static boolean sendSlotPacket(int slot, int button, ContainerInput action) {
        ItemStack stack = inv().getItem(slot);

        if (slot <= 8) {
            slot += 36;
        }

        if (PlayerUtils.invalid()) return false;

        int hashSlot = slot;
        HashedStack hash = HashedStack.create(stack, component -> hashSlot);
        ServerboundContainerClickPacket swap = new ServerboundContainerClickPacket(0, 1, (short) slot, (byte) button, action, Int2ObjectMaps.singleton(slot, hash), hash);
        PlayerUtils.sendPacket(swap);
        return true;
    }
}
