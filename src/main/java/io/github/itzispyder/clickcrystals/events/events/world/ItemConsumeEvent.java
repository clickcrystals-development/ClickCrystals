package io.github.itzispyder.clickcrystals.events.events.world;

import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemConsumeEvent extends Event {

    private final World world;
    private final LivingEntity user;
    private final ItemStack item;

    public ItemConsumeEvent(World world, LivingEntity user, ItemStack item) {
        this.world = world;
        this.user = user;
        this.item = item;
    }

    public World getWorld() {
        return world;
    }

    public LivingEntity getUser() {
        return user;
    }

    public ItemStack getItem() {
        return item;
    }
}
