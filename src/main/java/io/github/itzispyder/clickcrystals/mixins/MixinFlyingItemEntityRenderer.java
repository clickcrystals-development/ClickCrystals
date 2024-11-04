package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.PearlCustomizer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(FlyingItemEntityRenderer.class)
public class MixinFlyingItemEntityRenderer<T extends Entity> implements Global {

    @Mutable @Shadow @Final private float scale;

    @Unique private final Set<Entity> notifiedEntities = new HashSet<>();

    @Inject(method = "render", at = @At("TAIL"))
    public void renderUpdated(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (entity instanceof EnderPearlEntity pearlEntity) {
            PearlCustomizer pc = Module.get(PearlCustomizer.class);

            if (pc.pearlSize.getVal() != 0)
                this.scale = pc.pearlSize.getVal().floatValue() * 2;

            if (pearlEntity.age <= 2 && notifiedEntities.add(pearlEntity)) {
                SoundEvent soundEvent = pc.pearlSound.getVal() ? SoundEvents.EVENT_MOB_EFFECT_RAID_OMEN : SoundEvents.ENTITY_ENDERMAN_TELEPORT;
                mc.player.playSound(soundEvent, 1.0F, 1.0F);
            }
        } else {
            this.scale = 1.0f;
        }
    }
}
