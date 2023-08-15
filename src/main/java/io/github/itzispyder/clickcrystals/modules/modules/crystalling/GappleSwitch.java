package io.github.itzispyder.clickcrystals.modules.modules.crystalling;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.anchoring.ShieldSwitch;
import io.github.itzispyder.clickcrystals.modules.modules.crystalling.PearlSwitch;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;

public class GappleSwitch extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();

    private static long cooldown;

    public GappleSwitch() {
        super("Gapple-switch", Categories.CRYSTALLING,"Right click your sword to switch to your Gapple slot.");
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
    private void onRightClick(PacketSendEvent e) {
        if (e.getPacket() instanceof PlayerInteractItemC2SPacket) {
            final Module shieldSwitch = Module.get(ShieldSwitch.class);
            final Module pearlSwitch = Module.get(PearlSwitch.class);
            if (shieldSwitch != null && shieldSwitch.isEnabled()) return;
            if (pearlSwitch != null && PearlSwitch.isEnabled()) return;
            if (!HotbarUtils.has(Items.GOLDEN_APPLE)) return;
            
            boolean useSword = HotbarUtils.nameContains("sword");
            if (useSword){
                if (cooldown > System.currentTimeMillis()) return;
                cooldown = System.currentTimeMillis() + (50 * 4);
    
                e.setCancelled(true);
                HotbarUtils.search(Items.GOLDEN_APPLE);
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            }
            
            
        }
    }
}
