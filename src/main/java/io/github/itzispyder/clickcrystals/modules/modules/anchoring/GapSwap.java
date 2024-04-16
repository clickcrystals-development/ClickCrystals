package io.github.itzispyder.clickcrystals.modules.modules.anchoring;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.modules.ListenerModule;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import net.minecraft.item.Items;

// CREDITS TO: TutlaMc
public class GapSwap extends ListenerModule {

    public GapSwap() {
        super("gap-swap", Categories.PVP, "Swaps to gap when you right click with sword");
    }

    @EventHandler
    private void onClick(MouseClickEvent e) {
        if (e.isScreenNull() && e.getAction().isDown() && e.getButton() == 1) {
            if (HotbarUtils.nameContains("sword")) {
                if (HotbarUtils.has(Items.ENCHANTED_GOLDEN_APPLE)) {
                    HotbarUtils.search(Items.ENCHANTED_GOLDEN_APPLE);
                }
                else if (HotbarUtils.has(Items.GOLDEN_APPLE)) {
                    HotbarUtils.search(Items.GOLDEN_APPLE);
                }
            }
        }
    }
}
