package io.github.itzispyder.clickcrystals.modules.modules.clickcrystals;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.screen.sync.ItemStackHash;

public class ChestSorter extends ListenerModule {

    public ChestSorter() {
        super("chest-sorter", Categories.CLIENT, "Sorts chests that you open with this module on.");
    }

    @EventHandler
    private void onClick(MouseClickEvent e) {
        if (e.getButton() != 1 || !e.getAction().isRelease()) return;
        if (!(mc.currentScreen instanceof GenericContainerScreen container)) return;

        GenericContainerScreenHandler handler = container.getScreenHandler();
        int chestSize = handler.getRows() * 9;

        for (int i = 0; i < chestSize; i++) {
            for (int j = i + 1; j < chestSize; j++) {
                Slot slotI = handler.getSlot(i);
                Slot slotJ = handler.getSlot(j);

                ItemStack stackI = slotI.getStack();
                ItemStack stackJ = slotJ.getStack();

                if (stackI.isEmpty() && !stackJ.isEmpty()) {
                    swapSlots(handler, slotI, slotJ);
                    slotI.setStack(stackJ);
                    slotJ.setStack(stackI);
                    continue;
                }

                if (!stackI.isEmpty() && !stackJ.isEmpty()) {
                    String nameI = stackI.getName().getString();
                    String nameJ = stackJ.getName().getString();

                    if (nameI.compareTo(nameJ) > 0) {
                        swapSlots(handler, slotI, slotJ);
                        slotI.setStack(stackJ);
                        slotJ.setStack(stackI);
                    }
                }
            }
        }

        this.setEnabled(false, true);
    }

    private void swapSlots(GenericContainerScreenHandler handler, Slot slot1, Slot slot2) {
        sendSlotPacket(handler, slot1, 40, SlotActionType.SWAP);
        sendSlotPacket(handler, slot2, 40, SlotActionType.SWAP);
        sendSlotPacket(handler, slot1, 40, SlotActionType.SWAP);
    }

    private void sendSlotPacket(GenericContainerScreenHandler handler, Slot slot, int button, SlotActionType action) {
        ItemStack stack = slot.getStack();
        ItemStackHash hash = ItemStackHash.fromItemStack(stack, component -> slot.id);

        ClickSlotC2SPacket packet = new ClickSlotC2SPacket(
                handler.syncId,
                handler.getRevision(),
                (short) slot.id,
                (byte) button,
                action,
                Int2ObjectMaps.singleton(slot.id, hash),
                hash
        );

        PlayerUtils.sendPacket(packet);
    }
}
