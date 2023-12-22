package io.github.itzispyder.clickcrystals.modules.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.client.PlayerAttackEntityEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class CrystPerSec extends Module implements Listener {

    private static final List<Integer> cpsQueue = new ArrayList<>();
    private final static int updateSpeed = 7;
    private static int tempCps = 0;
    private static double cps = 0;
    private static int timer = 0;

    public CrystPerSec() {
        super("crystal-speed", Categories.MISC, "Crystals per second counter.");
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
    private void onAttackEntity(PlayerAttackEntityEvent e) {
        final Entity ent = e.getEntity();
        if (ent == null) return;
        if (!canUpdateCounter(ent)) return;

        tempCps ++;
    }

    @EventHandler
    private void onTick(ClientTickEndEvent e) {
        if (timer ++ >= 20 / updateSpeed) {
            cpsQueue.add(0, tempCps);
            if (cpsQueue.size() > 15) {
                cpsQueue.remove(cpsQueue.size() - 1);
            }

            cps = MathUtils.round(MathUtils.avg(cpsQueue) * updateSpeed,100);

            tempCps = 0;
            timer = 0;
        }
    }

    public static double getCrystalPerSecond() {
        return cps;
    }

    private boolean canUpdateCounter(Entity ent) {
        final EntityType<?> type = ent.getType();
        return type == EntityType.END_CRYSTAL || type == EntityType.MAGMA_CUBE;
    }
}
