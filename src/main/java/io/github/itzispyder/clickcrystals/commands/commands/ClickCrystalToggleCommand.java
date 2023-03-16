package io.github.itzispyder.clickcrystals.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.ClickCrystals;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.ClientTickEvent;
import io.github.itzispyder.clickcrystals.util.BlockUtils;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

public class ClickCrystalToggleCommand extends Command implements Listener {

    public ClickCrystalToggleCommand() {
        super("clickcrystaltoggle","Allows you to crystal easier, by using left click to both place and break crystals.","/cctoggle [on|off]","cctoggle");
        system.addListener(this);
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {
        builder.executes(context -> {
            ClickCrystals.isEnabled = !ClickCrystals.isEnabled;
            sendUpdatedResult();
            return SINGLE_SUCCESS;
        });

        builder.then(literal("help").executes(context -> {
            ChatUtils.sendMessage(super.getHelp());
            sendInfo();
            return SINGLE_SUCCESS;
        }));

        builder.then(literal("on").executes(context -> {
            ClickCrystals.isEnabled = true;
            sendUpdatedResult();
            return SINGLE_SUCCESS;
        }));

        builder.then(literal("off").executes(context -> {
            ClickCrystals.isEnabled = false;
            sendUpdatedResult();
            return SINGLE_SUCCESS;
        }));
    }

    public void sendInfo() {
        if (ClickCrystals.isEnabled) ChatUtils.sendPrefixMessage("§bClick Crystal §3is toggled §aon");
        else ChatUtils.sendPrefixMessage("§bClick Crystal §3is toggled §coff");
        ChatUtils.sendPrefixMessage("§7Make sure to click the TOP of a crystallable block!");
    }

    public void sendUpdatedResult() {
        if (ClickCrystals.isEnabled) ChatUtils.sendPrefixMessage("§bClick Crystal §3is now toggled §aon");
        else ChatUtils.sendPrefixMessage("§bClick Crystal §3is now toggled §coff");
    }

    @EventHandler
    public void onTick(ClientTickEvent.End e) {
        if (!ClickCrystals.isEnabled) return;
        if (!mc.interactionManager.isBreakingBlock()) return;
        if (mc.crosshairTarget.getType() != HitResult.Type.BLOCK) return;
        Vec3d vec = mc.crosshairTarget.getPos().add(0,-0.5,0);
        ItemStack item = mc.player.getStackInHand(mc.player.getActiveHand());
        if (item == null || !item.isOf(Items.END_CRYSTAL)) return;
        BlockUtils.interact(vec);
    }
}
