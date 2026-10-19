package io.github.itzispyder.clickcrystals.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.SlowSwing;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.ViewModel;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public abstract class MixinItemInHandRenderer implements Global {

    @Inject(method = "submitArmWithItem", at = @At("HEAD"))
    public void renderFirstPersonItemInvoke(PlayerRenderState player, FirstPersonHandsAndItemsRenderState renderState, float tickProgress, float pitch, InteractionHand hand, float swingProgress, ItemStack item, float equipProgress, PoseStack matrices, SubmitNodeCollector orderedRenderCommandQueue, int light, CallbackInfo ci) {
        ViewModel vm = Module.get(ViewModel.class);
        if (!vm.isEnabled())
            return;

        matrices.pushPose();

        if (hand == InteractionHand.MAIN_HAND) {
            float mainRotX = vm.mainRotX.getVal().floatValue();
            float mainRotY = vm.mainRotY.getVal().floatValue();
            float mainRotZ = vm.mainRotZ.getVal().floatValue();
            float mainPosX = vm.mainPosX.getVal().floatValue();
            float mainPosY = vm.mainPosY.getVal().floatValue();
            float mainPosZ = vm.mainPosZ.getVal().floatValue();

            matrices.rotate(Axis.XP.rotationDegrees(mainRotX));
            matrices.rotate(Axis.YP.rotationDegrees(mainRotY));
            matrices.rotate(Axis.ZP.rotationDegrees(mainRotZ));
            matrices.translate(mainPosX, mainPosY, mainPosZ);
        }
        else {
            float offRotX = vm.offRotX.getVal().floatValue();
            float offRotY = vm.offRotY.getVal().floatValue();
            float offRotZ = vm.offRotZ.getVal().floatValue();
            float offPosX = vm.offPosX.getVal().floatValue();
            float offPosY = vm.offPosY.getVal().floatValue();
            float offPosZ = vm.offPosZ.getVal().floatValue();

            matrices.rotate(Axis.XP.rotationDegrees(offRotX));
            matrices.rotate(Axis.YP.rotationDegrees(offRotY));
            matrices.rotate(Axis.ZP.rotationDegrees(offRotZ));
            matrices.translate(offPosX, offPosY, offPosZ);
        }
    }

    @Inject(method = "submitArmWithItem", at = @At("TAIL"))
    public void renderFirstPersonItemTail(PlayerRenderState player, FirstPersonHandsAndItemsRenderState renderState, float tickProgress, float pitch, InteractionHand hand, float swingProgress, ItemStack item, float equipProgress, PoseStack matrices, SubmitNodeCollector orderedRenderCommandQueue, int light, CallbackInfo ci) {
        if (Module.isEnabled(ViewModel.class))
            matrices.popPose();
    }

    @ModifyArgs(
            method = "submitArmWithItem(Lnet/minecraft/client/renderer/state/level/PlayerRenderState;Lnet/minecraft/client/renderer/state/level/FirstPersonHandsAndItemsRenderState;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/FirstPersonHandsAndItemsRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V")
    )
    public void onApplyItemArmTransform(Args args) {
        if (Module.isEnabled(SlowSwing.class))
            args.set(2, 0.0F);
    }
}
