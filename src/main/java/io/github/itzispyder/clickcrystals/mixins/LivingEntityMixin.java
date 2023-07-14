package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.events.events.EntityAttackEntityEvent;
import io.github.itzispyder.clickcrystals.events.events.PlayerWasAttackedEvent;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.misc.SlowSwing;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.itzispyder.clickcrystals.ClickCrystals.system;

/**
 * Mixin for living entities
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "getHandSwingDuration", at = @At("RETURN"), cancellable = true)
    public void getHandSwingDuration(CallbackInfoReturnable<Integer> cir) {
        Module slowHandSwing = Module.get(SlowSwing.class);
        if (slowHandSwing.isEnabled()) cir.setReturnValue(12);
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity victim = (Entity)(Object)this;
        Entity attacker = source.getAttacker();

        if (victim == null || attacker == null) return;

        EntityAttackEntityEvent entityEvent = new EntityAttackEntityEvent(attacker, victim);
        system.eventBus.pass(entityEvent);
        if (entityEvent.isCancelled()) cir.cancel();

        if (victim instanceof PlayerEntity player) {
            PlayerWasAttackedEvent playerEvent = new PlayerWasAttackedEvent(attacker, player);
            system.eventBus.pass(playerEvent);
            if (playerEvent.isCancelled()) cir.cancel();;
        }
    }
}
