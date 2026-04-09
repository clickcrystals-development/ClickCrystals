package io.github.itzispyder.clickcrystals.mixins;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.optimization.TimeChanger;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class MixinClientWorld {

    @Shadow @Final private ClientLevel.ClientLevelData clientLevelData;

    @Inject(method = "setTimeFromServer", at = @At("TAIL"), cancellable = true)
    public void getTimeOfDay(long gameTime, CallbackInfo ci) {
        TimeChanger tc = Module.get(TimeChanger.class);

        if (tc.isEnabled()) {
            ci.cancel();
            this.clientLevelData.setGameTime(tc.getTime());
        }
    }
}