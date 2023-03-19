package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.ClientTickEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.scheduler.ScheduledTask;
import io.github.itzispyder.clickcrystals.util.BlockUtils;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.Randomizer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
    private void onTick(ClientTickEvent.End e) {
        if (!super.isEnabled()) return;
        if (!mc.interactionManager.isBreakingBlock()) return;
        if (mc.crosshairTarget.getType() != HitResult.Type.BLOCK) return;
        World world = mc.player.getWorld();
        Vec3d vec = mc.crosshairTarget.getPos().add(0,-0.5,0);
        BlockPos pos = new BlockPos(vec);
        BlockState state = world.getBlockState(pos);
        if (state == null || !(state.isOf(Blocks.OBSIDIAN) || state.isOf(Blocks.BEDROCK))) return;
        ItemStack item = mc.player.getStackInHand(mc.player.getActiveHand());
        if (item == null) return;
        new ScheduledTask(() -> {
            if (item.isOf(Items.END_CRYSTAL)) {
                BlockUtils.interact(vec);
            } else if (item.isOf(Items.OBSIDIAN)) {
                if (mc.player.isSneaking()) return;
                HotbarUtils.search(Items.END_CRYSTAL);
            }
        }).runDelayedTask(Randomizer.rand(50));
    }
}
