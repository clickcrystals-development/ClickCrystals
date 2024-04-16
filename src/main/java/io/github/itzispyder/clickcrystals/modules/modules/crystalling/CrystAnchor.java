package io.github.itzispyder.clickcrystals.modules.modules.crystalling;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.BlockUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.math.BlockPos;

public class CrystAnchor extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> onCrystal = scGeneral.add(createBoolSetting()
            .name("on-crystal")
            .description("Trigger on use of crystals")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> onSword = scGeneral.add(createBoolSetting()
            .name("on-sword")
            .description("Trigger on use of swords")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> onPickaxe = scGeneral.add(createBoolSetting()
            .name("on-pickaxe")
            .description("Trigger on use of pickaxes")
            .def(false)
            .build()
    );

    public CrystAnchor() {
        super("crystal-anchor", Categories.CRYSTAL,"Right click the ground with a crystal to switch to your respawn anchor");
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
    private void onClickBlock(PacketSendEvent e) {
        if (e.getPacket() instanceof PlayerInteractBlockC2SPacket packet) {
            final BlockPos pos = packet.getBlockHitResult().getBlockPos();

            if (BlockUtils.canCrystalOn(pos)) return;
            if (!HotbarUtils.has(Items.RESPAWN_ANCHOR)) return;

            boolean crystal = onCrystal.getVal() && HotbarUtils.isHolding(Items.END_CRYSTAL);
            boolean sword = onSword.getVal() && HotbarUtils.nameContains("sword");
            boolean pick = onPickaxe.getVal() && HotbarUtils.nameContains("pickaxe");

            if (crystal | sword | pick) {
                e.setCancelled(true);
                HotbarUtils.search(Items.RESPAWN_ANCHOR);
                BlockUtils.interact(packet.getBlockHitResult());
                HotbarUtils.search(Items.GLOWSTONE);
            }
        }
    }
}
