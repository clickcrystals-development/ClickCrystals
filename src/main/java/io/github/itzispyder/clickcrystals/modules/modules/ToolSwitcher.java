package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.BlockUtils;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.NbtUtils;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * ToolSwitcher module
 */
public class ToolSwitcher extends Module implements Listener {

    public ToolSwitcher() {
        super("ToolSwitcher", Categories.MISC,"Auto switches to the efficient tool when you try to break a block.");
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
        if (e.getPacket() instanceof PlayerActionC2SPacket packet) {
            if (packet.getAction() == PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) {
                BlockPos pos = packet.getPos();
                BlockState state = mc.player.getWorld().getBlockState(pos);
                if (BlockUtils.isCrystallabe(pos)) return;
                if (HotbarUtils.nameContains("sword") || HotbarUtils.nameContains("totem") || HotbarUtils.nameContains("crystal") || HotbarUtils.nameContains("anchor") || HotbarUtils.isHolding(Items.GLOWSTONE)) return;

                Map<Integer,Float> entries = new HashMap<>();
                HotbarUtils.forEachItem((slot,item) -> {
                    if (item.getItem().getTranslationKey().contains("sword")) return;
                    entries.put(slot,calcWantedLvl(item,state));
                });
                int slot = entries.keySet()
                        .stream()
                        .max(Comparator.comparing(entries::get))
                        .get();
                if (entries.get(slot) != 1) mc.player.getInventory().selectedSlot = slot;
            }
        }
    }

    private float calcWantedLvl(ItemStack item, BlockState state) {
        float lvl = 0;
        lvl += item.getMiningSpeedMultiplier(state);
        lvl += NbtUtils.getEnchantLvL(item,Enchantments.EFFICIENCY);
        return lvl;
    }
}
