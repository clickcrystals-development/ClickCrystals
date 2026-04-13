package io.github.itzispyder.clickcrystals.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.SlowSwing;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.ViewModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ItemInHandRenderer.class)
public abstract class MixinItemInHandRenderer implements Global {

    @Inject(method = "renderArmWithItem", at = @At("HEAD"))
    public void renderFirstPersonItemInvoke(AbstractClientPlayer player, float tickProgress, float pitch, InteractionHand hand, float swingProgress, ItemStack item, float equipProgress, PoseStack matrices, SubmitNodeCollector orderedRenderCommandQueue, int light, CallbackInfo ci) {
        ViewModel vm = Module.get(ViewModel.class);
        if (!vm.isEnabled())
            return;

        matrices.pushPose();

        if (hand == InteractionHand.MAIN_HAND) {
            double mainRotX = vm.mainRotX.getVal();
            double mainRotY = vm.mainRotY.getVal();
            double mainRotZ = vm.mainRotZ.getVal();
            double mainPosX = vm.mainPosX.getVal();
            double mainPosY = vm.mainPosY.getVal();
            double mainPosZ = vm.mainPosZ.getVal();

            matrices.mulPose(Axis.XP.rotationDegrees((float)mainRotX));
            matrices.mulPose(Axis.YP.rotationDegrees((float)mainRotY));
            matrices.mulPose(Axis.ZP.rotationDegrees((float)mainRotZ));
            matrices.translate((float)mainPosX, (float)mainPosY, (float)mainPosZ);
        }
        else {
            double offRotX = vm.offRotX.getVal();
            double offRotY = vm.offRotY.getVal();
            double offRotZ = vm.offRotZ.getVal();
            double offPosX = vm.offPosX.getVal();
            double offPosY = vm.offPosY.getVal();
            double offPosZ = vm.offPosZ.getVal();

            matrices.mulPose(Axis.XP.rotationDegrees((float)offRotX));
            matrices.mulPose(Axis.YP.rotationDegrees((float)offRotY));
            matrices.mulPose(Axis.ZP.rotationDegrees((float)offRotZ));
            matrices.translate((float)offPosX, (float)offPosY, (float)offPosZ);
        }
    }

    @Inject(method = "renderArmWithItem", at = @At("TAIL"))
    public void renderFirstPersonItemTail(AbstractClientPlayer player, float tickProgress, float pitch, InteractionHand hand, float swingProgress, ItemStack item, float equipProgress, PoseStack matrices, SubmitNodeCollector orderedRenderCommandQueue, int light, CallbackInfo ci) {
        if (Module.isEnabled(ViewModel.class))
            matrices.popPose();
    }

    @ModifyArgs(
            method = "renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V")
    )
    public void onApplyItemArmTransform(Args args) {
        if (Module.isEnabled(SlowSwing.class))
            args.set(2, 0.0F);
    }
}