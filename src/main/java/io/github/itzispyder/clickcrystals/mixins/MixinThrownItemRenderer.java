package io.github.itzispyder.clickcrystals.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.PearlCustomizer;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.state.ThrownItemRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;

@Mixin(ThrownItemRenderer.class)
public class MixinThrownItemRenderer implements Global {

    @Mutable @Shadow @Final private float scale;
    @Unique private final Set<Entity> notifiedPearls = new HashSet<>();

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/ThrownItemRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At("TAIL"))
    public void renderUpdated(ThrownItemRenderState flyingItemEntityRenderState, PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo ci) {
        PearlCustomizer pc = Module.get(PearlCustomizer.class);
        Entity entity = EntityUtils.getRenderStateOwner(flyingItemEntityRenderState);

        if (pc == null || !pc.isEnabled())
            return;

        if (!(entity instanceof ThrownEnderpearl pearl))
            return;

        if (pc.pearlSize.getVal() != 0) {
            this.scale = pc.pearlSize.getVal().floatValue() * 2;
        } else {
            this.scale = 1.0f;
        }

        if (pearl.tickCount <= 2 && notifiedPearls.add(pearl) && pc.pearlSound.getVal())
            ChatUtils.pingPlayer();

        if (pearl.tickCount > 200)
            notifiedPearls.remove(pearl);
    }
}