package io.github.itzispyder.clickcrystals.util.minecraft;

import io.github.itzispyder.clickcrystals.Global;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class EntityRenderStateUtils implements Global {

    public static ArrayList<ItemStack> getArmorItems(EntityRenderState state) {
        ArrayList<ItemStack> is = new ArrayList<>();
        if (state instanceof PlayerEntityRenderState living) {
            is.add(living.equippedHeadStack);
            is.add(living.equippedChestStack);
            is.add(living.equippedLegsStack);
            is.add(living.equippedFeetStack);
        }
        return is;
    }

    public static float getHealth(EntityRenderState state) {
        if (state instanceof LivingEntityRenderState living && get(living) instanceof LivingEntity le) {
            return le.getHealth();
        }
        return 0;
    }

    public static Entity get(EntityRenderState state) {
        if (PlayerUtils.invalid()) return null;
        ClientWorld w = PlayerUtils.player().clientWorld;
        for (Entity entity: w.getEntities()) {
            if (state.squaredDistanceToCamera == entity.getPos().squaredDistanceTo(mc.gameRenderer.getCamera().getPos())) {
                return entity;
            }
        }
        return null;
    }
}
