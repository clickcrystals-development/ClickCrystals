package io.github.itzispyder.clickcrystals.modules.modules.anchoring;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.EventPriority;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modrinth.ModrinthNoNo;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.item.Items;

@ModrinthNoNo
public class ShieldSwitch extends Module implements Listener {

    public ShieldSwitch() {
        super("shield-switch", Categories.PVP, "Hotkey to shield after clicking sword");
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onClick(PacketSendEvent e) {
        if (e.getPacket() instanceof ServerboundUseItemPacket packet) {
            if (!holdingWeapon()) return;
            HotbarUtils.search(Items.SHIELD);
        }
    }

    private boolean holdingWeapon() {
        return HotbarUtils.nameContains("sword") || (!HotbarUtils.nameContains("pickaxe") && HotbarUtils.nameContains("axe"));
    }
}
