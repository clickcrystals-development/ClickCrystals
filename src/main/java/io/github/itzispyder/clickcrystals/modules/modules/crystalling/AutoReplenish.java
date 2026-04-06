package io.github.itzispyder.clickcrystals.modules.modules.crystalling;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.events.events.world.BlockPlaceEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ItemConsumeEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ItemUseEvent;
import io.github.itzispyder.clickcrystals.modrinth.ModrinthNoNo;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@ModrinthNoNo
public class AutoReplenish extends ListenerModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Double> replenishPercentage = scGeneral.add(createDoubleSetting()
            .name("replenish-percentage")
            .description("Maximum threshold before replenishing the item.")
            .max(0.5)
            .min(0.01)
            .decimalPlaces(2)
            .def(0.01)
            .build()
    );

    private Item lastUseItem;
    private int lastCount;

    public AutoReplenish() {
        super("auto-replenish", Categories.CRYSTAL, "Automatically replenishes items from your inventory into your hotbar");
    }

    @EventHandler
    public void onItemUse(ItemUseEvent e) {
        if (PlayerUtils.valid())
            set(e.getItem(), HotbarUtils.getHand(e.getHand()).getCount());
    }

    @EventHandler
    public void onBlockUse(BlockPlaceEvent e) {
        if (PlayerUtils.invalid())
            return;

        Item item = e.getItem();
        if (HotbarUtils.getHand(InteractionHand.MAIN_HAND).is(item))
            set(item, HotbarUtils.getHand(InteractionHand.MAIN_HAND).getCount());
        else if (HotbarUtils.getHand(InteractionHand.OFF_HAND).is(item))
            set(item, HotbarUtils.getHand(InteractionHand.OFF_HAND).getCount());
    }

    @EventHandler
    public void onConsume(ItemConsumeEvent e) {
        if (PlayerUtils.invalid())
            return;

        Item item = e.getItem().getItem();
        if (HotbarUtils.getHand(InteractionHand.MAIN_HAND).is(item))
            set(item, e.getItem().getCount());
        else if (HotbarUtils.getHand(InteractionHand.OFF_HAND).is(item))
            set(item, e.getItem().getCount());
    }

    @EventHandler
    public void onMouseRelease(MouseClickEvent e) {
        if (PlayerUtils.invalid())
            return;
        if (!e.isScreenNull() || e.getButton() != 1)
            return;

        if (e.getAction().isDown()) {
            if (!HotbarUtils.getHand(InteractionHand.MAIN_HAND).isEmpty()) {
                ItemStack item = HotbarUtils.getHand(InteractionHand.MAIN_HAND);
                set(item.getItem(), item.getCount());
            }
            else if (!HotbarUtils.getHand(InteractionHand.OFF_HAND).isEmpty()) {
                ItemStack item = HotbarUtils.getHand(InteractionHand.OFF_HAND);
                set(item.getItem(), item.getCount());
            }
        }
        else if (e.getAction().isRelease() && lastUseItem != null) {
            onItemClick(lastUseItem, Math.max(1, lastCount));
        }
    }

    private void set(Item item, int count) {
        this.lastUseItem = item;
        this.lastCount = count;
    }

    private void onItemClick(Item item, int count) {
        set(null, 0);

        if (item.getDefaultMaxStackSize() <= 1)
            return;
        if (count > 1 && count > item.getDefaultMaxStackSize() * replenishPercentage.getVal())
            return;

        // if hotbar already has the item, then prioritize that
        if (HotbarUtils.hasButNotHolding(target -> target.is(item))) {
            HotbarUtils.search(item);
            return;
        }

        // else, search the entire inventory for more
        int slot = InvUtils.searchInsideOnly(item);
        if (slot == -1)
            return;

        if (count <= 1)
            InvUtils.sendSlotPacket(slot, InvUtils.selected(), ClickType.SWAP);
        else
            InvUtils.quickMove(slot);
    }
}
