package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.modules.misc.NoInteractions;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity {
    @Inject(method = "openEditSignScreen", at = @At("HEAD"), cancellable = true)
    private void cancelOpenContainer(SignBlockEntity sign, boolean front, CallbackInfo ci) {
        NoInteractions noInteractions = new NoInteractions();
        if (noInteractions.isEnabled() && !noInteractions.AllowSign.getVal()) {
            ci.cancel();
        }
    }
}
