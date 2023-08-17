package io.github.itzispyder.clickcrystals.events.events.world;

import io.github.itzispyder.clickcrystals.events.Cancellable;
import io.github.itzispyder.clickcrystals.events.Event;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;

public class ItemUseEvent extends Event implements Cancellable {

    private final Item item;
    private final Hand hand;
    private boolean cancelled;
    private final UseType useType;

    public ItemUseEvent(Item item, Hand hand, UseType useType) {
        this.item = item;
        this.hand = hand;
        this.cancelled = false;
        this.useType = useType;
    }

    public Item getItem() {
        return item;
    }

    public Hand getHand() {
        return hand;
    }

    public UseType getUseType() {
        return useType;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public enum UseType {
        BLOCK,
        ENTITY,
        MISS;

        public boolean isBlock() {
            return this == BLOCK;
        }

        public boolean isEntity() {
            return this == ENTITY;
        }

        public boolean isMiss() {
            return this == MISS;
        }
    }
}
