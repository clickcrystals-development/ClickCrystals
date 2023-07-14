package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.PlayerWasAttackedEvent;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class HurtDirection extends Module implements Listener {

    public HurtDirection() {
        super("hurt-direction", Categories.RENDERING, "Displays the direction from which you were hurt from.");
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
    private void onAttacked(PlayerWasAttackedEvent e) {
        Entity attacker = e.getAttacker();
        PlayerEntity player = e.getPlayer();
        Vec3d vec = attacker.getPos().subtract(player.getPos()).normalize();
        double rot = player.getYaw() - attacker.getYaw();
        Direction dir = Direction.fromRotation(rot);

        ChatUtils.sendPrefixMessage("direction: " + dir.name());
    }
}
