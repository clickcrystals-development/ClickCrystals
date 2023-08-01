package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.optimization.NoItemBounce;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class MixinItemstack {

    @Inject(method = "setBobbingAnimationTime", at = @At("HEAD"), cancellable = true)
    public void setBobbingAnimationTime(int bobbingAnimationTime, CallbackInfo ci) {
        final Module noItemBob = Module.get(NoItemBounce.class);

        if (noItemBob.isEnabled()) ci.cancel();
    }

    @Inject(method = "getBobbingAnimationTime", at = @At("RETURN"), cancellable = true)
    public void getBobbingAnimationTime(CallbackInfoReturnable<Integer> cir) {
        final Module noItemBob = Module.get(NoItemBounce.class);

        if (noItemBob.isEnabled()) cir.setReturnValue(0);
    }
}
