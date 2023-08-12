package io.github.itzispyder.clickcrystals.modules.modules.minecart;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.NbtUtils;
import io.github.itzispyder.clickcrystals.util.PlayerUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BowSwap extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> pull = scGeneral.add(BooleanSetting.create()
            .name("pull-bow")
            .description("Pulls the bow after.")
            .def(false)
            .build()
    );

    public BowSwap() {
        super("bow-swap", Categories.MINECART, "Switch to flame enchanted bows after placing minecart tnt.");
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
    private void onInteract(PacketSendEvent e) {
        if (e.getPacket() instanceof PlayerInteractBlockC2SPacket packet) {
            ClientPlayerEntity p = PlayerUtils.player();
            World world = p.getWorld();
            BlockHitResult hit = packet.getBlockHitResult();
            BlockPos pos = hit.getBlockPos();
            BlockState state = world.getBlockState(pos);
            Item item = state.getBlock().asItem();

            if (HotbarUtils.isHolding(Items.TNT_MINECART, packet.getHand()) && RailSwap.isRail(item) && HotbarUtils.has(BowSwap::isFlameBow)) {
                HotbarUtils.search(BowSwap::isFlameBow);

                if (pull.getVal()) {
                    mc.options.useKey.setPressed(true);
                }
            }
        }
    }

    public static boolean isFlameBow(ItemStack item) {
        return NbtUtils.getEnchantLvL(item, Enchantments.FLAME) >= 1;
    }
}
