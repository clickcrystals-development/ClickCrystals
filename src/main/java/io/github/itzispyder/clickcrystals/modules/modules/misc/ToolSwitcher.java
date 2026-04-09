package io.github.itzispyder.clickcrystals.modules.modules.misc;

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
import io.github.itzispyder.clickcrystals.util.minecraft.NbtUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ToolSwitcher extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> onCrystal = scGeneral.add(BooleanSetting.create()
            .name("exclude-crystal")
            .description("Exclude on use of crystals.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> onObsidian = scGeneral.add(BooleanSetting.create()
            .name("exclude-obsidian")
            .description("Exclude on use of obsidian.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> onSword = scGeneral.add(BooleanSetting.create()
            .name("exclude-sword")
            .description("Exclude on use of swords.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> onTotem = scGeneral.add(BooleanSetting.create()
            .name("exclude-totem")
            .description("Exclude on use of totems.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> onGlowstone = scGeneral.add(BooleanSetting.create()
            .name("exclude-glowstone")
            .description("Exclude on use of glowstone.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> onAnchor = scGeneral.add(BooleanSetting.create()
            .name("exclude-anchor")
            .description("Exclude on use of anchors.")
            .def(true)
            .build()
    );

    public ToolSwitcher() {
        super("tool-switcher", Categories.MISC,"Switches to the right tool for mining a block");
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
    private void onPacketSend(PacketSendEvent e) {
        if (e.getPacket() instanceof ServerboundPlayerActionPacket packet) {
            if (packet.getAction() == ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) {
                final BlockPos pos = packet.getPos();
                final BlockState state = mc.player.level().getBlockState(pos);

                if (BlockUtils.canCrystalOn(pos)) return;
                if (itemExcluded()) return;

                final Map<Integer,Float> entries = new HashMap<>();
                HotbarUtils.forEachItem((slot,item) -> {
                    entries.put(slot,calcWantedLvl(item,state));
                });

                int slot = entries.keySet()
                        .stream()
                        .max(Comparator.comparing(entries::get))
                        .get();

                if (entries.get(slot) != 1) mc.player.getInventory().setSelectedSlot(slot);
            }
        }
    }

    private float calcWantedLvl(ItemStack item, BlockState state) {
        float lvl = 0;
        lvl += item.getDestroySpeed(state);
        lvl += NbtUtils.getEnchantLvL(item, Enchantments.EFFICIENCY);
        return lvl;
    }

    public boolean itemExcluded() {
        boolean useSword = HotbarUtils.nameContains("sword") && onSword.getVal();
        boolean useCrystal = HotbarUtils.isHolding(Items.END_CRYSTAL) && onCrystal.getVal();
        boolean useTotem = HotbarUtils.nameContains("totem") && onTotem.getVal();
        boolean useGlowstone = HotbarUtils.isHolding(Items.GLOWSTONE) && onGlowstone.getVal();
        boolean useAnchor = HotbarUtils.isHolding(Items.RESPAWN_ANCHOR) && onAnchor.getVal();
        boolean useObsidian = HotbarUtils.isHolding(Items.OBSIDIAN) && onObsidian.getVal();

        return useSword || useCrystal || useTotem || useGlowstone || useAnchor || useObsidian;
    }
}
