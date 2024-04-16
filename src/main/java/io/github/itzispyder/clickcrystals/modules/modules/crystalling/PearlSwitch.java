package io.github.itzispyder.clickcrystals.modules.modules.crystalling;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.anchoring.ShieldSwitch;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;

public class PearlSwitch extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> onTotem = scGeneral.add(BooleanSetting.create()
            .name("on-totem")
            .description("Activate on use of Totem Of Undying.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> onSword = scGeneral.add(BooleanSetting.create()
            .name("on-sword")
            .description("Activate on use of Swords.")
            .def(true)
            .build()
    );

    private static long cooldown;

    public PearlSwitch() {
        super("pearl-switch", Categories.CRYSTAL,"Right click your sword or totem to switch to your pearl slot");
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

            if (shieldSwitch != null && shieldSwitch.isEnabled()) return;
            if (!HotbarUtils.has(Items.ENDER_PEARL)) return;

            boolean useTotem = onTotem.getVal() && HotbarUtils.nameContains("totem");
            boolean useSword = onSword.getVal() && HotbarUtils.nameContains("sword");

            if (useSword || useTotem) {
                if (cooldown > System.currentTimeMillis()) return;
                cooldown = System.currentTimeMillis() + (50 * 4);

                e.setCancelled(true);
                HotbarUtils.search(Items.ENDER_PEARL);
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            }
        }
    }
}
