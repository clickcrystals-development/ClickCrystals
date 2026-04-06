package io.github.itzispyder.clickcrystals.events.events.world;

import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemConsumeEvent extends Event {

    private final Level world;
    private final LivingEntity user;
    private final ItemStack item;

    public ItemConsumeEvent(Level world, LivingEntity user, ItemStack item) {
        this.world = world;
        this.user = user;
        this.item = item;
    }

    public Level getWorld() {
        return world;
    }

    public LivingEntity getUser() {
        return user;
    }

    public ItemStack getItem() {
        return item;
    }
}
