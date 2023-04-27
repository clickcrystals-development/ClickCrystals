package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ActionResult;

import java.util.ArrayList;
import java.util.List;

public class CrystalPerSecondHud extends Module implements Listener {

    private static int tempCps = 0;
    private static double cps = 0;
    private static int timer = 0;
    private static final List<Integer> ints = new ArrayList<>();

    public CrystalPerSecondHud() {
        super("CrystalPerSecondHud", Categories.OTHER, "Crystals per second counter.");
    }

    @Override
    protected void onEnable() {
        system.addListener(this);

        AttackEntityCallback.EVENT.register((player, world, hand, ent, hitResult) -> {
            if (ent == null) return ActionResult.PASS;
            if (ent.getType() != EntityType.END_CRYSTAL) return ActionResult.PASS;

            tempCps ++;
            return ActionResult.PASS;
        });
    }

    @Override
    protected void onDisable() {
        system.removeListener(this);
    }

    @EventHandler
    private void onTick(ClientTickEndEvent e) {
        if (timer ++ >= 20) {
            ints.add(0, tempCps);

            if (ints.size() >= 2) {
                ints.remove(ints.size() - 1);
                cps = MathUtils.round(MathUtils.avg(ints),100);
            }

            tempCps = 0;
            timer = 0;
        }
    }

    public static double getCrystalPerSecond() {
        return cps;
    }
}
