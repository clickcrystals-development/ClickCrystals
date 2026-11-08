package io.github.itzispyder.clickcrystals.util.minecraft;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.scripting.runtime.PlayerInventorySlotMap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
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
        return slot >= 0 && slot <= 8;
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
            if (stack != null && item.test(stack))
                count += stack.getCount();
        }

        ItemStack off = HotbarUtils.getHand(Hand.OFF_HAND);
        if (item.test(off)) {
            count += off.getCount();
        }

        return count;
    }

    public static ItemStack getEquipmentSlot(EquipmentSlot equipmentSlot){
        if (equipmentSlot == null) return ItemStack.EMPTY;
        return inv().player.getEquippedStack(equipmentSlot);
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
            if (stack != null && stack.isOf(item))
                return true;
        }

        return false;
    }

    public static boolean has(Predicate<ItemStack> item) {
        if (item == null) return false;

        for (int i = 0; i < inv().getMainStacks().size(); i++) {
            ItemStack stack = inv().getStack(i);
            if (stack != null && item.test(stack))
                return true;
        }

        return false;
    }

    /**
     * Request an inventory operation through Minecraft's interaction manager.
     * Unlike a hand-built ClickSlot packet, this uses the current handler
     * revision and maintains the client's expected slot state.
     *
     * Only player inventory coordinates are accepted. For a chest or other
     * open screen, slot numbers have different meanings; fail closed rather
     * than corrupting an unrelated container slot.
     *
     * A true result means the client requested the click, NOT that the
     * remote server has confirmed the inventory change.
     */
    public static boolean sendSlotPacket(int slot, int button, SlotActionType action) {
        if (PlayerUtils.invalid() || mc.interactionManager == null || !mc.isOnThread())
            return false;
        if (slot < 0 || slot >= 36 || action == null)
            return false;
        if (mc.player.currentScreenHandler != mc.player.playerScreenHandler)
            return false;

        ItemStack stack = inv().getStack(slot);
        if (stack == null || stack.isEmpty())
            return false;

        int screenSlot = PlayerInventorySlotMap.screenSlot(slot);
        mc.interactionManager.clickSlot(
                mc.player.playerScreenHandler.syncId, screenSlot, button, action, mc.player);
        return true;
    }
}
