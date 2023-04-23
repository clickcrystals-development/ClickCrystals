package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.InteractionUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class ClickCrystalReverse extends Module implements Listener {

    public ClickCrystalReverse() {
        super("ClickCrystalReverse", Categories.CRYSTALLING, "Binds end crystal break to right click.");
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
    public void onPacketSend(ClientTickEndEvent e) {
        if (mc.options.useKey.isPressed()) {
            HitResult hit = mc.crosshairTarget;
            if (hit == null) return;
            if (hit.getType() != HitResult.Type.ENTITY) return;
            Entity ent = ((EntityHitResult) hit).getEntity();
            if (ent.getType() != EntityType.END_CRYSTAL) return;
            InteractionUtils.doAttack();
            mc.options.useKey.setPressed(false);
        }
    }
}
