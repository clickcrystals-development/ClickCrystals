package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.world.ItemConsumeEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ItemUseEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class MixinItem implements Global {

    private long itemUseCooldown = 0L;
    private long itemConsumeCooldown = 0L;
    @Shadow public abstract ActionResult use(World world, PlayerEntity user, Hand hand);

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (isItemUseOnCooldown()) {
            return;
        }
        resetItemUseCooldown();
        ItemUseEvent event = new ItemUseEvent((Item)(Object)this, hand, ItemUseEvent.UseType.MISS);
        system.eventBus.passWithCallbackInfo(cir, event);
    }

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void use(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (isItemUseOnCooldown()) {
            return;
        }
        resetItemUseCooldown();
        ItemUseEvent event = new ItemUseEvent((Item)(Object)this, context.getHand(), ItemUseEvent.UseType.BLOCK);
        system.eventBus.passWithCallbackInfo(cir, event);
    }

    @Inject(method = "useOnEntity", at = @At("HEAD"), cancellable = true)
    public void use(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (isItemUseOnCooldown()) {
            return;
        }
        resetItemUseCooldown();
        ItemUseEvent event = new ItemUseEvent((Item)(Object)this, hand, ItemUseEvent.UseType.ENTITY);
        system.eventBus.passWithCallbackInfo(cir, event);
    }

    @Inject(method = "finishUsing", at = @At("HEAD"), cancellable = true)
    public void use(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (isItemConsumeOnCooldown()) {
            return;
        }
        resetItemConsumeCooldown();
        ItemConsumeEvent event = new ItemConsumeEvent(world, user, stack);
        system.eventBus.passWithCallbackInfo(cir, event);
    }

    private boolean isItemUseOnCooldown() {
        return itemUseCooldown + 50L > System.currentTimeMillis();
    }

    private boolean isItemConsumeOnCooldown() {
        return itemConsumeCooldown + 50L > System.currentTimeMillis();
    }

    private void resetItemUseCooldown() {
        itemUseCooldown = System.currentTimeMillis() + 50L;
    }

    private void resetItemConsumeCooldown() {
        itemConsumeCooldown = System.currentTimeMillis() + 50L;
    }
}
