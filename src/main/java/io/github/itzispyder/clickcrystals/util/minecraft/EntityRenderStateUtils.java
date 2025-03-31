package io.github.itzispyder.clickcrystals.util.minecraft;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;

public class EntityRenderStateUtils {
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
        if (state instanceof LivingEntityRenderState living) {
            return ((LivingEntity) get(living)).getHealth();
        }
        return 0;
    }

    public static Entity get(EntityRenderState state) {
        if (PlayerUtils.invalid())
            return null;
        ClientWorld w = PlayerUtils.player().clientWorld;
        for (Entity entity: w.getEntities()) {
            Vec3d p = entity.getPos();
            if (state.squaredDistanceToCamera == p.squaredDistanceTo(MinecraftClient.getInstance().gameRenderer.getCamera().getPos())) {
                return entity;
            }
        }
        return null;
    }

}
