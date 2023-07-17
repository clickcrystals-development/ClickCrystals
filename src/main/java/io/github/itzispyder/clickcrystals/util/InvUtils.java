package io.github.itzispyder.clickcrystals.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

public final class InvUtils {

    public static PlayerInventory inv() {
        return mc.player.getInventory();
    }

    public static int selected() {
        return inv().selectedSlot;
    }

    public static void select(int slot) {
        inv().selectedSlot = slot;
    }

    public static ItemStack selectedStack() {
        return inv().getStack(selected());
    }

    public static boolean swapOffhand(int slot) {
        return sendSlotPacket(slot, 40, SlotActionType.SWAP);
    }

    public static boolean dropSlot(int slot, boolean full) {
        return sendSlotPacket(slot, full ? 1 : 0, SlotActionType.THROW);
    }

    public static int search(Item item) {
        if (item == null) return -1;

        for (int i = 0; i < inv().main.size(); i++) {
            ItemStack stack = inv().getStack(i);
            if (stack == null || stack.isEmpty()) continue;
            if (stack.isOf(item)) return i;
        }

        return -1;
    }

    public static int count(Item item) {
        int count = 0;

        if (item == null) {
            return count;
        }

        for (int i = 0; i < inv().main.size(); i++) {
            ItemStack stack = inv().getStack(i);
            if (stack == null || stack.isEmpty()) continue;
            if (stack.isOf(item)) count += stack.getCount();
        }
        return count;
    }

    public static boolean has(Item item) {
        if (item == null) return false;

        for (int i = 0; i < inv().main.size(); i++) {
            ItemStack stack = inv().getStack(i);
            if (stack == null || stack.isEmpty()) continue;
            if (stack.isOf(item)) return true;
        }

        return false;
    }

    public static boolean sendSlotPacket(int slot, int button, SlotActionType action) {
        ItemStack stack = inv().getStack(slot);

        if (slot <= 8) {
            slot += 36;
        }

        if (stack == null || PlayerUtils.playerNull()) return false;

        ClickSlotC2SPacket swap = new ClickSlotC2SPacket(0, 1, slot, button, action, stack, Int2ObjectMaps.singleton(slot, stack));
        PlayerUtils.sendPacket(swap);
        return true;
    }
}
