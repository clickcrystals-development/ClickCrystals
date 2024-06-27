package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.SlowSwing;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.ViewModel;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(HeldItemRenderer.class)
public abstract class MixinHeldItemRenderer implements Global {

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    public void renderFirstPersonItemInvoke(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack m, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        ViewModel vm = Module.get(ViewModel.class);
        if (!vm.isEnabled())
            return;

        m.push();

        if (hand == Hand.MAIN_HAND) {
            double mainRotX = vm.mainRotX.getVal();
            double mainRotY = vm.mainRotY.getVal();
            double mainRotZ = vm.mainRotZ.getVal();
            double mainPosX = vm.mainPosX.getVal();
            double mainPosY = vm.mainPosY.getVal();
            double mainPosZ = vm.mainPosZ.getVal();

            m.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float)mainRotX));
            m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)mainRotY));
            m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)mainRotZ));
            m.translate((float)mainPosX, (float)mainPosY, (float)mainPosZ);
        }
        else {
            double offRotX = vm.offRotX.getVal();
            double offRotY = vm.offRotY.getVal();
            double offRotZ = vm.offRotZ.getVal();
            double offPosX = vm.offPosX.getVal();
            double offPosY = vm.offPosY.getVal();
            double offPosZ = vm.offPosZ.getVal();

            m.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float)offRotX));
            m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)offRotY));
            m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)offRotZ));
            m.translate((float)offPosX, (float)offPosY, (float)offPosZ);
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At("TAIL"))
    public void renderFirstPersonItemTail(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack m, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (Module.isEnabled(ViewModel.class))
            m.pop();
    }

    @ModifyArgs(method = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    public void renderItem(Args args) {
        args.set(6, Module.isEnabled(SlowSwing.class) ? 0.0F : args.get(6));
    }
}
