package io.github.itzispyder.clickcrystals.modules.modules.crystalling;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modrinth.ModrinthNoNo;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.BlockUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;

@ModrinthNoNo
public class CrystAnchor extends Module implements Listener {
    private final SettingSection settings = getGeneralSection();
    public final ModuleSetting<Boolean> onCrystal = settings.add(createBoolSetting()
            .name("on-crystal").description("Trigger while holding a crystal").def(true).build());
    public final ModuleSetting<Boolean> onSword = settings.add(createBoolSetting()
            .name("on-sword").description("Trigger while holding a sword").def(false).build());
    public final ModuleSetting<Boolean> onPickaxe = settings.add(createBoolSetting()
            .name("on-pickaxe").description("Trigger while holding a pickaxe").def(false).build());

    public CrystAnchor() {
        super("crystal-anchor", Categories.CRYSTAL,
                "Place an anchor on a targeted block, then select glowstone when available");
    }

    @Override protected void onEnable() { system.addListener(this); }
    @Override protected void onDisable() { system.removeListener(this); }

    @EventHandler
    private void onClickBlock(PacketSendEvent event) {
        if (!(event.getPacket() instanceof ServerboundUseItemOnPacket packet)) return;
        if (PlayerUtils.invalid() || mc.screen != null) return;
        if (!(mc.hitResult instanceof BlockHitResult hit)) return;
        if (!hit.getBlockPos().equals(packet.getHitResult().getBlockPos())) return;
        if (BlockUtils.canCrystalOn(hit.getBlockPos())) return;
        // Never cancel a player's click or swap their weapon when supplies are missing.
        if (!HotbarUtils.has(Items.RESPAWN_ANCHOR) || !HotbarUtils.has(Items.GLOWSTONE)) return;

        boolean match = (onCrystal.getVal() && HotbarUtils.isHolding(Items.END_CRYSTAL))
                || (onSword.getVal() && HotbarUtils.nameContains("sword"))
                || (onPickaxe.getVal() && HotbarUtils.nameContains("pickaxe"));
        if (!match || !HotbarUtils.search(Items.RESPAWN_ANCHOR)) return;

        event.setCancelled(true);
        BlockUtils.interact(hit);
        HotbarUtils.search(Items.GLOWSTONE);
    }
}
