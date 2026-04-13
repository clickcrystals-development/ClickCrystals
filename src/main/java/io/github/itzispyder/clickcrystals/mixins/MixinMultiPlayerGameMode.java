package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.misc.NoBreakDelay;
import io.github.itzispyder.clickcrystals.modules.modules.misc.NoInteractions;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class MixinMultiPlayerGameMode implements Global {

    @Shadow private int destroyDelay;

    @Inject(method = "startDestroyBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;destroyDelay:I"))
    public void attackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (mc != null && Module.isEnabled(NoBreakDelay.class)) {
            destroyDelay = 0;
        }
    }

    @Inject(method = "continueDestroyBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;destroyDelay:I", ordinal = 1))
    public void updateBlockBreakingProgress(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (mc != null && Module.isEnabled(NoBreakDelay.class)) {
            destroyDelay = 0;
        }
    }

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void interactBlock(LocalPlayer player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (mc == null || mc.level == null || hitResult == null || hitResult.getBlockPos() == null) {
            return;
        }

        Block block = mc.level.getBlockState(hitResult.getBlockPos()).getBlock();
        NoInteractions nci = Module.get(NoInteractions.class);

        if (nci.isEnabled() && !nci.canInteract(block)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}