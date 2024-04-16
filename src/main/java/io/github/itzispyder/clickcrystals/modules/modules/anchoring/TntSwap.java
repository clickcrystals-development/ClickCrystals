package io.github.itzispyder.clickcrystals.modules.modules.anchoring;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.PostActionable;
import io.github.itzispyder.clickcrystals.events.events.world.BlockPlaceEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.BlockUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class TntSwap extends Module implements Listener, PostActionable {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> instant = scGeneral.add(BooleanSetting.create()
            .name("instant")
            .description("Allows of a more instant swapping.")
            .def(false)
            .build()
    );
    public static final List<PostAction> actions = new ArrayList<>();

    public TntSwap() {
        super("tnt-swap", Categories.PVP, "Swaps to tnt after placing rails");
    }

    @Override
    public List<PostAction> getActions() {
        return actions;
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
    private void onPlace(BlockPlaceEvent e) {
        if (RailSwap.isRail(e.getItem()) && HotbarUtils.has(Items.TNT_MINECART)) {
            HotbarUtils.search(Items.TNT_MINECART);

            if (instant.getVal()) {
                system.scheduler.runDelayedTask(() -> {
                    BlockUtils.interact(e.getPos(), Direction.UP);
                    this.action();
                }, 50);
            }
        }
    }
}
