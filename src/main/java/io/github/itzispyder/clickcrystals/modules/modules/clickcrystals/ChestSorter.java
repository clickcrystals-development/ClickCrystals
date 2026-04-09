package io.github.itzispyder.clickcrystals.modules.modules.clickcrystals;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.HashedStack;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ChestSorter extends ListenerModule {

    public ChestSorter() {
        super("chest-sorter", Categories.CLIENT, "Sorts chests that you open with this module on.");
    }

    @EventHandler
    private void onClick(MouseClickEvent e) {
        if (e.getButton() != 1 || !e.getAction().isRelease()) return;
        if (!(mc.screen instanceof ContainerScreen container)) return;

        ChestMenu handler = container.getMenu();
        int chestSize = handler.getRowCount() * 9;

        for (int i = 0; i < chestSize; i++) {
            for (int j = i + 1; j < chestSize; j++) {
                Slot slotI = handler.getSlot(i);
                Slot slotJ = handler.getSlot(j);

                ItemStack stackI = slotI.getItem();
                ItemStack stackJ = slotJ.getItem();

                if (stackI.isEmpty() && !stackJ.isEmpty()) {
                    swapSlots(handler, slotI, slotJ);
                    slotI.setByPlayer(stackJ);
                    slotJ.setByPlayer(stackI);
                    continue;
                }

                if (!stackI.isEmpty() && !stackJ.isEmpty()) {
                    String nameI = stackI.getHoverName().getString();
                    String nameJ = stackJ.getHoverName().getString();

                    if (nameI.compareTo(nameJ) > 0) {
                        swapSlots(handler, slotI, slotJ);
                        slotI.setByPlayer(stackJ);
                        slotJ.setByPlayer(stackI);
                    }
                }
            }
        }

        this.setEnabled(false, true);
    }

    private void swapSlots(ChestMenu handler, Slot slot1, Slot slot2) {
        sendSlotPacket(handler, slot1, 40, ClickType.SWAP);
        sendSlotPacket(handler, slot2, 40, ClickType.SWAP);
        sendSlotPacket(handler, slot1, 40, ClickType.SWAP);
    }

    private void sendSlotPacket(ChestMenu handler, Slot slot, int button, ClickType action) {
        ItemStack stack = slot.getItem();
        HashedStack hash = HashedStack.create(stack, component -> slot.index);

        ServerboundContainerClickPacket packet = new ServerboundContainerClickPacket(
                handler.containerId,
                handler.getStateId(),
                (short) slot.index,
                (byte) button,
                action,
                Int2ObjectMaps.singleton(slot.index, hash),
                hash
        );

        PlayerUtils.sendPacket(packet);
    }
}
