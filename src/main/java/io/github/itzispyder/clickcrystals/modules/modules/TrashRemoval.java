package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.InventoryAddItemEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import io.github.itzispyder.clickcrystals.util.InventoryUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrashRemoval extends Module implements Listener {

    private static final List<Item> blacklist = new ArrayList<>(Arrays.asList(
            Items.COBBLESTONE,
            Items.DIRT,
            Items.GRAVEL,
            Items.GRANITE,
            Items.DIORITE,
            Items.SAND,
            Items.SANDSTONE,
            Items.CUT_SANDSTONE,
            Items.SMOOTH_SANDSTONE,
            Items.NETHERRACK,
            Items.DANDELION,
            Items.POPPY,
            Items.WARPED_FUNGUS,
            Items.COBBLED_DEEPSLATE,
            Items.WHEAT_SEEDS,
            Items.GRASS,
            Items.GRASS_BLOCK
    ));

    public TrashRemoval() {
        super("TrashRemoval", Categories.MISC, "Removes trash from your inventory when added.");
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
    }

    @EventHandler
    private void onAddStack(InventoryAddItemEvent e) {
        final ItemStack item = e.getItem();
        final Item type = item.getItem();
        final int slot = e.getSlot();

        ChatUtils.sendPrefixMessage("Found: " + type.getTranslationKey());

        if (!blacklist.contains(type)) return;

        ChatUtils.sendPrefixMessage("Discarding: " + type.getTranslationKey());

        InventoryUtils.dropStack(slot);
    }
}
