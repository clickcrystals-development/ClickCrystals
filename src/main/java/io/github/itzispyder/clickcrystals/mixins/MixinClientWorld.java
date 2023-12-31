package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.optimization.TimeChanger;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public abstract class MixinClientWorld {

    @Shadow @Final private ClientWorld.Properties clientWorldProperties;

    @Inject(method = "setTimeOfDay", at = @At("TAIL"), cancellable = true)
    public void getTimeOfDay(long timeOfDay, CallbackInfo ci) {
        TimeChanger tc = Module.get(TimeChanger.class);

        if (tc.isEnabled()) {
            ci.cancel();
            this.clientWorldProperties.setTimeOfDay(tc.timeMode.getVal().getTime());
        }
    }
}