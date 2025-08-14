package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.PearlCustomizer;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.state.FlyingItemEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(FlyingItemEntityRenderer.class)
public class MixinFlyingItemEntityRenderer implements Global {

    @Mutable @Shadow @Final private float scale;
    @Unique private final Set<Entity> notifiedPearls = new HashSet<>();

    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/FlyingItemEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
    public void renderUpdated(FlyingItemEntityRenderState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        PearlCustomizer pc = Module.get(PearlCustomizer.class);
        Entity entity = EntityUtils.getRenderStateOwner(state);

        if (!pc.isEnabled())
            return;

        if (!(entity instanceof EnderPearlEntity pearl))
            return;

        if (pc.pearlSize.getVal() != 0) {
            this.scale = pc.pearlSize.getVal().floatValue() * 2;
        } else {
            this.scale = 1.0f;
        }

        if (pearl.age <= 2 && notifiedPearls.add(pearl) && pc.pearlSound.getVal())
            ChatUtils.pingPlayer();

        if (pearl.age > 200)
            notifiedPearls.remove(pearl);
    }
}