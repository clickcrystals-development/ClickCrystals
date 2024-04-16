package io.github.itzispyder.clickcrystals.modules.modules.anchoring;

import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.PostActionable;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.BooleanSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.NbtUtils;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;

public class BowSwap extends Module implements Listener {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Boolean> pull = scGeneral.add(BooleanSetting.create()
            .name("pull-bow")
            .description("Pulls the bow after.")
            .def(false)
            .build()
    );
    public final PostActionable.PostAction postAction = (client, player) -> {
        if (HotbarUtils.has(BowSwap::isFlameBow)) {
            HotbarUtils.search(BowSwap::isFlameBow);

            if (pull.getVal()) {
                mc.options.useKey.setPressed(true);
            }
        }
    };

    public BowSwap() {
        super("bow-swap", Categories.PVP, "Hotkey flame bow after placing cart. Requires TntSwap and its \"instant\" setting to be active");
    }

    @Override
    protected void onEnable() {
        system.addListener(this);
        Module.acceptFor(TntSwap.class, tntSwap -> tntSwap.post(postAction));
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
        Module.acceptFor(TntSwap.class, tntSwap -> tntSwap.remove(postAction));
    }

    /*
    @EventHandler
    private void onInteract(PacketSendEvent e) {
        if (e.getPacket() instanceof PlayerInteractBlockC2SPacket packet) {
            ClientPlayerEntity p = PlayerUtils.player();
            World world = p.getWorld();
            BlockHitResult hit = packet.getBlockHitResult();
            BlockPos pos = hit.getBlockPos();
            BlockState state = world.getBlockState(pos);
            Item item = state.getBlock().asItem();

            ChatUtils.sendPrefixMessage(HotbarUtils.getHand(packet.getHand()).getItem().getName().getString());

            if (RailSwap.isRail(item) && HotbarUtils.has(BowSwap::isFlameBow)) {
                HotbarUtils.search(BowSwap::isFlameBow);

                if (pull.getVal()) {
                    mc.options.useKey.setPressed(true);
                }
            }
        }
    }
     */

    public static boolean isFlameBow(ItemStack item) {
        return NbtUtils.getEnchantLvL(item, Enchantments.FLAME) >= 1;
    }
}
