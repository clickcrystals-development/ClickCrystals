package io.github.itzispyder.clickcrystals.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;

import java.util.Map;

import static io.github.itzispyder.clickcrystals.ClickCrystals.mc;

/**
 * Client inventory management utils
 */
public abstract class InventoryUtils {

    public static final PlayerInventory inv = mc.player.getInventory();
    public static final ClientPlayNetworkHandler nh = mc.player.networkHandler;

    /**
     * Sorts the player's inventory to ImproperIssues' style
     * Deprecated.
     */
    @Deprecated
    public static void sortInv() {
        Map<String,Integer> todo = new ManualMap<String,Integer>(
                "helmet", 5,
                "chestplate", 6,
                "leggings", 7,
                "boots", 8,
                "totem", 45,
                "crystal", 36,
                "obsidian", 37,
                "sword", 38,
                "glowstone", 39,
                "anchor", 40,
                "pickaxe", 41,
                "totem", 42,
                "golden_apple", 43,
                "pearl", 44
        ).getMap();

        todo.forEach((s,i) -> {
            moveSlot(search(s),i);
        });
    }

    /**
     * Moves an inventory slot to another inventory slot
     * @param from from slot
     * @param to to slot
     */
    public static void moveSlot(int from, int to) {
        if (from < 0 || to < 0) return;
        if (inv == null || inv.isEmpty()) return;
        final ItemStack fromStack = inv.getStack(from), toStack = inv.getStack(to);
        if (fromStack.isEmpty()) return;
        if (!toStack.isEmpty()) return;
        ClickSlotC2SPacket pick = new ClickSlotC2SPacket(0,2,from,0, SlotActionType.PICKUP,fromStack, Int2ObjectMaps.singleton(from,fromStack));
        nh.sendPacket(pick);
        ClickSlotC2SPacket set = new ClickSlotC2SPacket(0,2,to,0, SlotActionType.PICKUP,toStack, Int2ObjectMaps.singleton(to,toStack));
        nh.sendPacket(set);
        int free = inv.getEmptySlot();
        ClickSlotC2SPacket put = new ClickSlotC2SPacket(0,2,free,0, SlotActionType.PICKUP,toStack, Int2ObjectMaps.singleton(free,toStack));
        nh.sendPacket(put);
    }

    /**
     * Server for an item based on a string key
     * @param key key
     * @return return slot of item found, if none, returns -1
     */
    public static int search(String key) {
        for (int i = 0; i < 45; i++) {
            if (inv.getStack(i).getTranslationKey().toLowerCase().contains(key.toLowerCase()))
                return i;
        }
        return -1;
    }

    /**
     * Server for an item based on a string key
     * @param key key
     * @return return slot of item found, if none, returns -1
     */
    public static int searchMain(String key) {
        for (int i = 0; i < inv.main.size(); i++) {
            if (inv.getStack(i).getTranslationKey().toLowerCase().contains(key.toLowerCase()))
                return i;
        }
        return -1;
    }
}
