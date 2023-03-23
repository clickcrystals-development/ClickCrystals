package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.scheduler.ScheduledTask;
import io.github.itzispyder.clickcrystals.util.BlockUtils;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.Randomizer;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * Module for click crystal
 */
public class ClickCrystal extends Module implements Listener {

    public ClickCrystal() {
        super("ClickCrystal","Allows you to crystal easier, by using left click to both place and break crystals.");
        system.addListener(this);
        super.enabled = true;
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
        ChatUtils.sendPrefixMessage("§7Make sure to click the TOP of a crystallable block!");
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
    }

    @EventHandler
    private void onSendPacket(PacketSendEvent e) {
        if (!super.isEnabled()) return;
        if (!mc.options.attackKey.isPressed()) return;
        if (e.getPacket() instanceof PlayerActionC2SPacket packet) {
            if (packet.getAction() != PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) return;;
            BlockPos pos = packet.getPos();
            if (!(BlockUtils.matchBlock(pos, Blocks.OBSIDIAN) || BlockUtils.matchBlock(pos, Blocks.BEDROCK))) return;

            new ScheduledTask(() -> {
                if (HotbarUtils.isHolding(Items.END_CRYSTAL)) {
                    e.setCancelled(true);
                    BlockUtils.interact(pos, Direction.UP);
                }
                else if (HotbarUtils.isHolding(Items.OBSIDIAN)) {
                    e.setCancelled(true);
                    HotbarUtils.search(Items.END_CRYSTAL);
                }
            }).runDelayedTask(Randomizer.rand(50));
        }
    }
}
