package io.github.itzispyder.clickcrystals.modules.modules.crystalling;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.BlockUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;

public class CrystSwitch extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> onCrystal = scGeneral.add(BooleanSetting.create()
            .name("on-crystal")
            .description("On use of crystals.")
            .def(false)
            .build()
    );
    public final ModuleSetting<Boolean> onObsidian = scGeneral.add(BooleanSetting.create()
            .name("on-obsidian")
            .description("On use of obsidian.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> onSword = scGeneral.add(BooleanSetting.create()
            .name("on-sword")
            .description("On use of swords.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> onTotem = scGeneral.add(BooleanSetting.create()
            .name("on-totem")
            .description("On use of totems.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> onGlowstone = scGeneral.add(BooleanSetting.create()
            .name("on-glowstone")
            .description("On use of glowstone.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> onAnchor = scGeneral.add(BooleanSetting.create()
            .name("on-anchor")
            .description("On use of anchors.")
            .def(true)
            .build()
    );
    private final SettingSection scDetails = createSettingSection("details");
    public final ModuleSetting<Boolean> excludeBedrock = scDetails.add(createBoolSetting()
            .name("exclude-bedrock")
            .description("Prevents triggers when targeting bedrock.")
            .def(false)
            .build()
    );

    public CrystSwitch() {
        super("crystal-switch", Categories.CRYSTAL,"Whenever you punch bedrock or obsidian with a sword, it will switch to a crystal");
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
    private void onSendPacket(PacketSendEvent e) {
        if (e.getPacket() instanceof PlayerActionC2SPacket packet) {
            final BlockPos pos = packet.getPos();

            if (packet.getAction() != PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) return;
            if (!HotbarUtils.has(Items.END_CRYSTAL)) return;

            if (canUse() && isCrystallable(pos)) {
                e.setCancelled(true);
                HotbarUtils.search(Items.END_CRYSTAL);
                BlockUtils.interact(pos,packet.getDirection());
            }
        }
    }

    public boolean canUse() {
        boolean useSword = HotbarUtils.nameContains("sword") && onSword.getVal();
        boolean useCrystal = HotbarUtils.isHolding(Items.END_CRYSTAL) && onCrystal.getVal();
        boolean useTotem = HotbarUtils.nameContains("totem") && onTotem.getVal();
        boolean useGlowstone = HotbarUtils.isHolding(Items.GLOWSTONE) && onGlowstone.getVal();
        boolean useAnchor = HotbarUtils.isHolding(Items.RESPAWN_ANCHOR) && onAnchor.getVal();
        boolean useObsidian = HotbarUtils.isHolding(Items.OBSIDIAN) && onObsidian.getVal();

        return useSword || useCrystal || useTotem || useGlowstone || useAnchor || useObsidian;
    }

    public boolean isCrystallable(BlockPos pos) {
        return BlockUtils.matchBlock(pos, Blocks.OBSIDIAN) || (!excludeBedrock.getVal() && BlockUtils.matchBlock(pos, Blocks.BEDROCK));
    }
}
